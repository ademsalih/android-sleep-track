package com.example.fitbit_tracker.wsserver;

import android.content.Context;
import android.util.Log;

import com.example.fitbit_tracker.dao.ReadingDao;
import com.example.fitbit_tracker.dao.SessionDao;
import com.example.fitbit_tracker.db.NyxDatabase;
import com.example.fitbit_tracker.handlers.WebSocketCallback;
import com.example.fitbit_tracker.model.AccelerometerReading;
import com.example.fitbit_tracker.model.BatteryReading;
import com.example.fitbit_tracker.model.GyroscopeReading;
import com.example.fitbit_tracker.model.HeartrateReading;
import com.example.fitbit_tracker.model.Session;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;

public class CustomWebSocketServer extends WebSocketServer {
    private final String TAG = this.getClass().getSimpleName();

    private final WebSocketCallback webSocketCallback;
    private final SessionDao sessionDao;
    private final ReadingDao readingDao;

    public CustomWebSocketServer(WebSocketCallback webSocketCallback, Context context, int port) {
        super(new InetSocketAddress(port));
        this.webSocketCallback = webSocketCallback;
        this.sessionDao = NyxDatabase.getDatabase(context).sessionDao();
        this.readingDao = NyxDatabase.getDatabase(context).readingDao();
    }

    @Override
    public void onStart() {
        Log.d(TAG, "WebSocket server started");
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d(TAG, "A client has connected: " + conn.getRemoteSocketAddress());
        conn.send("Welcome to the Android server!");
        webSocketCallback.onOpen();
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.d(TAG, message);
        webSocketCallback.onMessage(message);

        String newMessage = message.replace('"','\"');

        try {
            JSONObject json = new JSONObject(newMessage);
            String command = json.getString("command");
            JSONObject dataObject = json.getJSONObject("data");

            String sessionIdentifier = dataObject.getString("sessionIdentifier");

            switch (command) {
                case "ADD_READING":

                    String sensorIdentifier = dataObject.getString("sensorIdentifier");
                    long timeStamp = dataObject.getLong("timeStamp");

                    switch (sensorIdentifier) {
                        case "HEARTRATE":
                            JSONObject bpmData = dataObject.getJSONObject("data");
                            int bpm = bpmData.getInt("bpm");
                            readingDao.insert(new HeartrateReading(sessionIdentifier, timeStamp, bpm));
                            break;
                        case "ACCELEROMETER":
                            JSONObject items = dataObject.getJSONObject("items");

                            JSONArray x = items.getJSONArray("x");
                            JSONArray y = items.getJSONArray("y");
                            JSONArray z = items.getJSONArray("z");
                            JSONArray timestampArray = items.getJSONArray("timestamp");

                            for (int i = 0; i < x.length(); i++) {
                                double xAcceleration = x.getDouble(i);
                                double yAcceleration = y.getDouble(i);
                                double zAcceleration = z.getDouble(i);
                                long ts = timestampArray.getLong(i);

                                readingDao.insert(new AccelerometerReading(sessionIdentifier,ts, xAcceleration,yAcceleration,zAcceleration));
                            }
                            break;
                        case "GYROSCOPE":
                            JSONObject gyroItems = dataObject.getJSONObject("items");

                            JSONArray gyroX = gyroItems.getJSONArray("x");
                            JSONArray gyroY = gyroItems.getJSONArray("y");
                            JSONArray gyroZ = gyroItems.getJSONArray("z");
                            JSONArray gyroTimestamp = gyroItems.getJSONArray("timestamp");

                            for (int i = 0; i < gyroX.length(); i++) {
                                double vX = gyroX.getDouble(i);
                                double vY = gyroY.getDouble(i);
                                double vZ = gyroZ.getDouble(i);
                                long gyrots = gyroTimestamp.getLong(i);

                                readingDao.insert(new GyroscopeReading(sessionIdentifier, gyrots, vX, vY, vZ));
                            }

                            break;
                        case "BATTERY":
                            JSONObject batteryData = dataObject.getJSONObject("data");
                            int batteryLevel = batteryData.getInt("batteryLevel");
                            readingDao.insert(new BatteryReading(sessionIdentifier,0, batteryLevel));
                            break;
                        default:
                            break;
                    }

                    break;
                case "INIT_SESSION":
                    String deviceModel = dataObject.getString("deviceModel");
                    Session session = new Session(sessionIdentifier,0,0, deviceModel,0);
                    sessionDao.insert(session);
                    break;
                case "START_SESSION":
                    long startTime = dataObject.getLong("startTime");
                    Session tempSession = sessionDao.getSession(sessionIdentifier);
                    tempSession.setStartTime(startTime);
                    sessionDao.update(tempSession);
                    webSocketCallback.onSessionStart();
                    break;
                case "STOP_SESSION":
                    long endTime = dataObject.getLong("endTime");
                    int readingCount = dataObject.getInt("readingsCount");

                    Session session1 = sessionDao.getSession(sessionIdentifier);
                    session1.setEndTime(endTime);
                    session1.setReadingsCount(readingCount);
                    sessionDao.update(session1);

                    webSocketCallback.onSessionEnd();
                    break;
                default:
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d(TAG, "Connection to " + conn.getRemoteSocketAddress() + " closed due to:" + reason);
        webSocketCallback.onClose();
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.d(TAG, "onError" + ex);
    }

}
