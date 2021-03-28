package com.example.fitbit_tracker.wsserver;

import android.content.Context;

import com.example.fitbit_tracker.handlers.WebSocketCallback;
import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.model.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MessageHandler {

    private final WebSocketCallback webSocketCallback;

    public MessageHandler(WebSocketCallback webSocketCallback, Context context) {
        this.webSocketCallback = webSocketCallback;
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

                            List<Reading> readingToInsert = new ArrayList<>();
                            Number maxId = Realm.getDefaultInstance().where(Reading.class).max("readingId");

                            long nextId = (maxId == null) ? 1 : maxId.intValue() + 1;

                            // Iterate over the items in the reading array (e.g. "items": [9.23, 1.023, 2.30])
                            for (int j = 0; j < items.length(); j++) {
                                double item = items.getDouble(j);
                                long timestamp = timestamps.getLong(j);

                                Reading reading = new Reading();
                                reading.setReadingId(nextId+j);
                                reading.setTimeStamp(timestamp);

                                reading.setData((float) item);
                                reading.setSensorName(sensorIdentifier);
                                reading.setReadingType(type);

                                readingToInsert.add(reading);
                            }

                            Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Number maxId = realm.where(Reading.class).max("readingId");
                                    int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;

                                    long sessionId = realm
                                            .where(Session.class)
                                            .equalTo("uuid", sessionIdentifier)
                                            .findFirst()
                                            .getSessionId();

                                    for (Reading r: readingToInsert) r.setSessionId(sessionId);

                                    realm.insert(readingToInsert);
                                }
                            });
                        } else {
                            double item = dataItem.getDouble("item");
                            long timestamp = payload.getLong("timeStamp");

                            Reading reading = new Reading();
                            reading.setTimeStamp(timestamp);
                            reading.setData((float)item);
                            reading.setSensorName(sensorIdentifier);
                            reading.setReadingType(type);

                            // Insert session into Realm DB
                            Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Number maxId = realm.where(Reading.class).max("readingId");
                                    int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
                                    reading.setReadingId(nextId);
                                    long sessionId = realm
                                            .where(Session.class)
                                            .equalTo("uuid", sessionIdentifier)
                                            .findFirst()
                                            .getSessionId();
                                    reading.setSessionId(sessionId);
                                    realm.insert(reading);
                                }
                            });
                        }
                    }
                    break;
                case INIT_SESSION:
                    String deviceModel = payload.getString("deviceModel");
                    sessionIdentifier = payload.getString("sessionIdentifier");

                    // Insert session into Realm DB
                    Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Number maxId = realm.where(Session.class).max("sessionId");
                            int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;

                            Session session = new Session();
                            session.setSessionId(nextId);
                            session.setEndTime(0);
                            session.setStartTime(0);
                            session.setReadingsCount(0);
                            session.setUuid(sessionIdentifier);
                            session.setDeviceModel(deviceModel);
                            realm.insert(session);
                        }
                    });

                    break;
                case START_SESSION:
                    long startTime = payload.getLong("startTime");
                    sessionIdentifier = payload.getString("sessionIdentifier");

                    Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
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

                    Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
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
