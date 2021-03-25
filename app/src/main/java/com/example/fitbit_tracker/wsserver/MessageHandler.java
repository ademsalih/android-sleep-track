package com.example.fitbit_tracker.wsserver;

import android.content.Context;

import com.example.fitbit_tracker.dao.ReadingDao;
import com.example.fitbit_tracker.dao.ReadingTypeDao;
import com.example.fitbit_tracker.dao.SensorDao;
import com.example.fitbit_tracker.dao.SessionDao;
import com.example.fitbit_tracker.database.NyxDatabase;
import com.example.fitbit_tracker.handlers.WebSocketCallback;
import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.model.Sensor;
import com.example.fitbit_tracker.model.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageHandler {

    private final SessionDao sessionDao;
    private final ReadingDao readingDao;
    private final ReadingTypeDao readingTypeDao;
    private final SensorDao sensorDao;
    private final WebSocketCallback webSocketCallback;

    public MessageHandler(WebSocketCallback webSocketCallback, Context context) {
        NyxDatabase db = NyxDatabase.getDatabase(context);
        this.sessionDao = db.sessionDao();
        this.readingDao = db.readingDao();
        this.readingTypeDao = db.readingTypeDao();
        this.sensorDao = db.sensorDao();
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

                    long sessionId = 0;
                    if (sessionDao.getSession(sessionIdentifier) != null) {
                        sessionId = sessionDao.getSession(sessionIdentifier).getId();
                    }

                    JSONArray data = payload.getJSONArray("data");
                    boolean batchReading = payload.getBoolean("batchReading");

                    Sensor sensor = sensorDao.getSensor(sensorIdentifier);
                    long sensorId = sensor.getId();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject dataItem = data.getJSONObject(i);

                        String type = dataItem.getString("type");
                        double item = dataItem.getDouble("item");

                        long readingType = readingTypeDao.getReadingType(sensorId, type);

                        if (batchReading) {
                        } else {
                            long timestamp = payload.getLong("timeStamp");
                            Reading reading = new Reading();
                            reading.setTimeStamp(timestamp);
                            reading.setSessionId(sessionId);
                            reading.setData((float)item);
                            reading.setReadingTypeId(readingType);
                            readingDao.insert(reading);
                        }
                    }
                    break;
                case INIT_SESSION:
                    String deviceModel = payload.getString("deviceModel");
                    sessionIdentifier = payload.getString("sessionIdentifier");

                    Session session = new Session();
                    session.setEndTime(0);
                    session.setStartTime(0);
                    session.setReadingsCount(0);
                    session.setUuid(sessionIdentifier);
                    session.setDeviceModel(deviceModel);
                    session.setUserId(1);
                    sessionDao.insert(session);
                    break;
                case START_SESSION:
                    long startTime = payload.getLong("startTime");
                    Session tempSession = sessionDao.getSession(payload.getString("sessionIdentifier"));
                    tempSession.setStartTime(startTime);
                    sessionDao.update(tempSession);

                    webSocketCallback.onSessionStart();
                    break;

                case STOP_SESSION:
                    long endTime = payload.getLong("endTime");
                    int readingCount = payload.getInt("readingsCount");
                    sessionIdentifier = payload.getString("sessionIdentifier");

                    session = sessionDao.getSession(sessionIdentifier);
                    session.setEndTime(endTime);
                    session.setReadingsCount(readingCount);
                    sessionDao.update(session);

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
