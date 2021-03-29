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
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class MessageHandler {

    private final WebSocketCallback webSocketCallback;
    private final RealmNyxSessionStore realmSessionStore;

    public MessageHandler(WebSocketCallback webSocketCallback, Context context) {
        this.webSocketCallback = webSocketCallback;
        this.realmSessionStore = new RealmNyxSessionStore();
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
                    String sensorIdentifier = payload.getString("sensorIdentifier");
                    String sessionIdentifier = payload.getString("sessionIdentifier");

                    JSONArray data = payload.getJSONArray("data");
                    boolean batchReading = payload.getBoolean("batchReading");

                    // Iterate over the reading types (e.g. "x", "y", "z")
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject dataItem = data.getJSONObject(i);

                        String type = dataItem.getString("type");

                        if (batchReading) {
                            JSONArray items = dataItem.getJSONArray("items");
                            JSONArray timestamps = payload.getJSONArray("timeStamps");

                            RealmList<Reading> readingToInsert = new RealmList<>();

                            // Iterate over the items in the reading array (e.g. "items": [9.23, 1.023, 2.30])
                            for (int j = 0; j < items.length(); j++) {
                                double item = items.getDouble(j);
                                long timestamp = timestamps.getLong(j);

                                Reading reading = new Reading();
                                reading.setTimeStamp(timestamp);
                                reading.setData((float) item);
                                reading.setReadingType(type);
                                readingToInsert.add(reading);
                            }

                            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Session session = realm
                                            .where(Session.class)
                                            .equalTo("uuid", sessionIdentifier)
                                            .findFirst();

                                    Sensor sensor = realm.where(Sensor.class).equalTo("sensorName",sensorIdentifier).findFirst();

                                    for (Reading r: readingToInsert) {
                                        r.setSession(session);
                                        r.setSensor(sensor);
                                    }

                                    session.getSessionReadings().addAll(readingToInsert);
                                    sensor.getSensorReadings().addAll(readingToInsert);
                                }
                            });
                        } else {
                            double item = dataItem.getDouble("item");
                            long timestamp = payload.getLong("timeStamp");

                            Reading reading = new Reading();
                            reading.setTimeStamp(timestamp);
                            reading.setData((float) item);
                            reading.setReadingType(type);

                            // Insert session into Realm DB
                            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    // TODO: Factor this part out, it's appearing twice...
                                    Session session = realm
                                            .where(Session.class)
                                            .equalTo("uuid", sessionIdentifier)
                                            .findFirst();

                                    // TODO: And this...
                                    Sensor sensor = realm
                                            .where(Sensor.class)
                                            .equalTo("sensorName",sensorIdentifier)
                                            .findFirst();

                                    reading.setSession(session);
                                    reading.setSensor(sensor);

                                    session.getSessionReadings().add(reading);
                                    sensor.getSensorReadings().add(reading);
                                }
                            });
                        }
                    }
                    break;
                case INIT_SESSION:
                    String deviceModel = payload.getString("deviceModel");
                    sessionIdentifier = payload.getString("sessionIdentifier");
                    JSONArray activeSensors = payload.getJSONArray("activeSensors");

                    List<String> sensorNames = new ArrayList<>();
                    for (int i = 0; i < activeSensors.length(); i++) {
                        sensorNames.add(activeSensors.getString(i));
                    }

                    // Insert session into Realm DB
                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Number sessionMaxId = realm.where(Session.class).max("sessionId");
                            int sessionNextId = (sessionMaxId == null) ? 1 : sessionMaxId.intValue() + 1;

                            Session session = new Session();
                            session.setSessionId(sessionNextId);
                            session.setEndTime(0);
                            session.setStartTime(0);
                            session.setReadingsCount(0);
                            session.setUuid(sessionIdentifier);
                            session.setDeviceModel(deviceModel);
                            session.setSessionSensors(new RealmList<>());

                            for (int i = 0; i < sensorNames.size(); i++) {
                                SessionSensor sessionSensor = new SessionSensor();

                                Sensor sensor = realm.where(Sensor.class).equalTo("sensorName", sensorNames.get(i)).findFirst();

                                if (sensor == null) {
                                    Log.d("MessageHandler", "Adding sensor");

                                    Number sensorMaxId = realm.where(Sensor.class).max("sensorId");
                                    int sensorNextId = (sensorMaxId == null) ? 1 : sensorMaxId.intValue() + 1;

                                    sensor = new Sensor();
                                    sensor.setSensorId(sensorNextId + i);
                                    sensor.setSensorName(sensorNames.get(i));
                                    sensor.setSessionSensors(new RealmList<>());
                                }

                                Number sessionSensorMaxId = realm.where(Sensor.class).max("sensorId");
                                int sessionSensorNextId = (sessionSensorMaxId == null) ? 1 : sessionSensorMaxId.intValue() + 1;

                                sessionSensor.setId(sessionSensorNextId + i);
                                sessionSensor.setSensor(sensor);
                                sessionSensor.setSession(session);

                                sensor.getSessionSensors().add(sessionSensor);
                                session.getSessionSensors().add(sessionSensor);
                            }

                            Log.d("MessageHandler", "Max: " + sessionMaxId + " Next: " + sessionNextId);


                            User user = realm.where(User.class).findFirst();

                            session.setUser(user);

                            user.getUserSessions().add(session);

                            Log.d("MessageHandler", "INIT successfully");
                        }
                    });

                    break;
                case START_SESSION:
                    long startTime = payload.getLong("startTime");
                    sessionIdentifier = payload.getString("sessionIdentifier");

                    realmSessionStore.startSession(sessionIdentifier, startTime);

                    webSocketCallback.onSessionStart();
                    break;

                case STOP_SESSION:
                    long endTime = payload.getLong("endTime");
                    int readingCount = payload.getInt("readingsCount");
                    sessionIdentifier = payload.getString("sessionIdentifier");

                    realmSessionStore.stopSession(sessionIdentifier, endTime, readingCount);

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
