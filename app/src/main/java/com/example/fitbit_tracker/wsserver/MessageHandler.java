package com.example.fitbit_tracker.wsserver;

import android.content.Context;
import android.util.Log;

import com.example.fitbit_tracker.handlers.WebSocketCallback;
import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.model.Sensor;
import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.model.SessionSensor;
import com.example.fitbit_tracker.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;

public class MessageHandler {

    private final WebSocketCallback webSocketCallback;
    //private final RealmNyxSessionStore realmSessionStore;

    public MessageHandler(WebSocketCallback webSocketCallback, Context context) {
        this.webSocketCallback = webSocketCallback;
        //this.realmSessionStore = new RealmNyxSessionStore();
    }

    public void handleMessage(String message) {
        String parsedMessage = convertToJSONString(message);

        try {
            JSONObject json = new JSONObject(parsedMessage);

            String rawCommand = json.getString("command");
            FitbitMessageCommand command = FitbitMessageCommand.valueOf(rawCommand);

            JSONObject payload = json.getJSONObject("payload");

            switch (command) {
                case ADD_READING:
                    long start = System.currentTimeMillis();
                    String sensorIdentifier = payload.getString("sensorIdentifier");
                    String sessionIdentifier = payload.getString("sessionIdentifier");

                    JSONArray data = payload.getJSONArray("data");
                    boolean batchReading = payload.getBoolean("batchReading");

                    // Readings that should be added to realm are collected first
                    // independent of whether the reading is a batch or not
                    RealmList<Reading> readingToInsert = new RealmList<>();

                    Realm realm = Realm.getDefaultInstance();
                    long sessionId = realm.where(Session.class).equalTo("uuid", sessionIdentifier).findFirst().getSessionId();
                    long sensorId = realm.where(Sensor.class).equalTo("sensorName",sensorIdentifier).findFirst().getSensorId();

                    // Iterate over the reading types (e.g. "x", "y", "z", "bpm")
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject dataItem = data.getJSONObject(i);
                        String type = dataItem.getString("type");

                        long timestamp = payload.getLong("timestamp");

                        if (batchReading) {
                            // If batch reading, each reading type represents an array of readings
                            JSONArray items = dataItem.getJSONArray("items");
                            JSONArray timestamps = payload.getJSONArray("timestamps");

                            // Iterate over the items in the reading array (e.g. "items": [9.23, 1.023, 2.30])
                            for (int j = 0; j < items.length(); j++) {
                                double item = items.getDouble(j);
                                long delta = timestamps.getLong(j);

                                Reading reading = new Reading();
                                reading.setTimeStamp(timestamp + delta);
                                reading.setData((float) item);
                                reading.setReadingType(type);
                                reading.setSessionId(sessionId);
                                reading.setSensorId(sensorId);
                                readingToInsert.add(reading);
                            }
                        } else {
                            // If not batch reading, get the single value item for the reading type
                            double item = dataItem.getDouble("item");

                            Reading reading = new Reading();
                            reading.setTimeStamp(timestamp);
                            reading.setData((float) item);
                            reading.setReadingType(type);
                            reading.setSessionId(sessionId);
                            reading.setSensorId(sensorId);
                            readingToInsert.add(reading);
                        }
                    }

                    try {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                //realm.insert(readingToInsert);
                                realm.copyToRealm(readingToInsert);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        realm.close();
                    }

                    long time = System.currentTimeMillis() - start;
                    Log.d("INSERT_TIME", "Insertion[" + sensorIdentifier + "]: " + time + " ms");
                    break;
                case INIT_SESSION:
                    String deviceModel = payload.getString("deviceModel");
                    sessionIdentifier = payload.getString("sessionIdentifier");
                    JSONArray activeSensors = payload.getJSONArray("activeSensors");

                    HashMap<String, Float> sensorNames = new HashMap<>();
                    for (int i = 0; i < activeSensors.length(); i++) {
                        JSONObject sensorObject = activeSensors.getJSONObject(i);
                        String sensorName = sensorObject.getString("name");
                        float frequency = (float) sensorObject.getDouble("frequency");
                        sensorNames.put(sensorName, frequency);
                    }

                    // Insert session into Realm DB
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            User user = realm.where(User.class).findFirst();

                            Number sessionMaxId = realm.where(Session.class).max("sessionId");
                            long sessionNextId = (sessionMaxId == null) ? 1 : sessionMaxId.longValue() + 1;

                            Log.d("LOG", "Session id next: " + sessionNextId);

                            Session session = new Session();
                            session.setSessionId(sessionNextId);
                            session.setEndTime(0);
                            session.setStartTime(0);
                            session.setReadingsCount(0);
                            session.setUuid(sessionIdentifier);
                            session.setDeviceModel(deviceModel);
                            session.setUserId(user.getId());
                            realm.insert(session);

                            Number sensorMaxId = realm.where(Sensor.class).max("sensorId");
                            long sensorNextId = (sensorMaxId == null) ? 1 : sensorMaxId.longValue() + 1;

                            Number sessionSensorMaxId = realm.where(SessionSensor.class).max("id");
                            long sessionSensorNextId = (sessionSensorMaxId == null) ? 1 : sessionSensorMaxId.longValue() + 1;


                            List<String> keyList = new ArrayList<>(sensorNames.keySet());

                            for (int i = 0; i < keyList.size(); i++) {
                                Sensor sensor = realm.where(Sensor.class).equalTo("sensorName", keyList.get(i)).findFirst();
                                if (sensor == null) {
                                    Log.d("LOG", keyList.get(i) + " sensor id next: " + sensorNextId + i);

                                    sensor = new Sensor();
                                    sensor.setSensorId(sensorNextId+i);
                                    sensor.setSensorName(keyList.get(i));
                                    realm.insert(sensor);
                                }

                                SessionSensor sessionSensor = new SessionSensor();
                                sessionSensor.setId(sessionSensorNextId+i);
                                sessionSensor.setFrequency(sensorNames.get(keyList.get(i)));
                                sessionSensor.setSensorId(sensor.getSensorId());
                                sessionSensor.setSessionId(session.getSessionId());
                                realm.insert(sessionSensor);
                            }
                        }
                    });
                    break;
                case START_SESSION:
                    long startTime = payload.getLong("startTime");
                    sessionIdentifier = payload.getString("sessionIdentifier");

                    //realmSessionStore.startSession(sessionIdentifier, startTime);
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Session session = realm.where(Session.class).equalTo("uuid", sessionIdentifier).findFirst();
                            session.setStartTime(startTime);
                        }
                    });

                    webSocketCallback.onSessionStart();
                    break;

                case STOP_SESSION:
                    long endTime = payload.getLong("endTime");
                    int readingCount = payload.getInt("readingsCount");
                    sessionIdentifier = payload.getString("sessionIdentifier");

                    //realmSessionStore.stopSession(sessionIdentifier, endTime, readingCount);
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Session session = realm.where(Session.class).equalTo("uuid", sessionIdentifier).findFirst();
                            session.setEndTime(endTime);
                            session.setReadingsCount(readingCount);
                        }
                    });

                    webSocketCallback.onSessionEnd();
                    break;
                default:
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String convertToJSONString(String message) {
        return message.replace('"','\"');
    }

}
