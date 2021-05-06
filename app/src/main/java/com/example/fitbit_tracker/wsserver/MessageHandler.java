package com.example.fitbit_tracker.wsserver;


import android.util.Log;

import com.example.fitbit_tracker.handlers.SessionCallback;
import com.example.fitbit_tracker.model.Reading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

import io.realm.RealmList;

public class MessageHandler {
    private final String TAG = this.getClass().getSimpleName();

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
                    String sessionIdentifier = payload.getString("sessionIdentifier");
                    String sensorIdentifier = payload.getString("sensorIdentifier");
                    JSONArray timestamps = payload.getJSONArray("timestamps");
                    JSONArray data = payload.getJSONArray("data");

                    List<Reading> readingToInsert = new RealmList<>();

                    for (int k = 0; k < data.length(); k++) {

                        JSONObject valuesObject = (JSONObject) data.get(k);

                        String type = valuesObject.getString("type");
                        JSONArray items = valuesObject.getJSONArray("items");

                        for (int j = 0; j < items.length(); j++) {
                            double item = items.getDouble(j);

                            Reading reading = new Reading();
                            reading.setTimeStamp(timestamps.getLong(j));
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

                    sessionCallback.onSessionStart();
                    realmSessionStore.startSession(sessionIdentifier, startTime);
                    break;

                case STOP_SESSION:
                    sessionCallback.onSessionFinalize();

                    long endTime = payload.getLong("endTime");
                    int readingCount = payload.getInt("readingsCount");
                    sessionIdentifier = payload.getString("sessionIdentifier");

                    Timer sessionEndTimer = new Timer();

                    sessionEndTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            sessionCallback.onSessionEnd();
                            realmSessionStore.stopSession(sessionIdentifier, endTime, readingCount);
                            sessionEndTimer.cancel();
                        }
                    }, 5000);

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
