package com.example.fitbit_tracker.wsserver;


import com.example.fitbit_tracker.handlers.SessionCallback;
import com.example.fitbit_tracker.model.Reading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import io.realm.RealmList;

public class MessageHandler {

    private final SessionCallback sessionCallback;

    private final RealmNyxSessionStore realmSessionStore;

    public MessageHandler(SessionCallback sessionCallback) {
        this.sessionCallback = sessionCallback;
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

                    // Readings that should be added to realm are collected first
                    // independent of whether the reading is a batch or not
                    List<Reading> readingToInsert = new RealmList<>();

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
                                readingToInsert.add(reading);
                            }
                        } else {
                            // If not batch reading, get the single value item for the reading type
                            double item = dataItem.getDouble("item");

                            Reading reading = new Reading();
                            reading.setTimeStamp(timestamp);
                            reading.setData((float) item);
                            reading.setReadingType(type);
                            readingToInsert.add(reading);
                        }
                    }

                    realmSessionStore.addReading(sessionIdentifier, sensorIdentifier, readingToInsert);

                    break;
                case INIT_SESSION:
                    String deviceModel = payload.getString("deviceModel");
                    sessionIdentifier = payload.getString("sessionIdentifier");
                    JSONArray activeSensors = payload.getJSONArray("activeSensors");

                    HashMap<String, Float> sensorConfig = new HashMap<>();
                    for (int i = 0; i < activeSensors.length(); i++) {
                        JSONObject sensorObject = activeSensors.getJSONObject(i);
                        sensorConfig.put(sensorObject.getString("name"), (float) sensorObject.getDouble("frequency"));
                    }

                    // Insert session into Realm DB
                    realmSessionStore.initSession(deviceModel, sessionIdentifier, sensorConfig);

                    break;
                case START_SESSION:
                    long startTime = payload.getLong("startTime");
                    sessionIdentifier = payload.getString("sessionIdentifier");

                    realmSessionStore.startSession(sessionIdentifier, startTime);

                    sessionCallback.onSessionStart();
                    break;

                case STOP_SESSION:
                    long endTime = payload.getLong("endTime");
                    int readingCount = payload.getInt("readingsCount");
                    sessionIdentifier = payload.getString("sessionIdentifier");

                    realmSessionStore.stopSession(sessionIdentifier, endTime, readingCount);

                    sessionCallback.onSessionEnd();
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
