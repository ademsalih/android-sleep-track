package com.example.fitbit_tracker.wsserver;

import android.content.Context;
import android.util.Log;

import com.example.fitbit_tracker.db.NyxDatabase;
import com.example.fitbit_tracker.handlers.WebSocketCallback;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;

public class CustomWebSocketServer extends WebSocketServer {
    private final String TAG = this.getClass().getSimpleName();

    private NyxDatabase nyxDatabase;
    private WebSocketCallback webSocketCallback;

    public CustomWebSocketServer(WebSocketCallback webSocketCallback, Context context, int port) {
        super(new InetSocketAddress(port));
        this.nyxDatabase = new NyxDatabase(context);
        this.webSocketCallback = webSocketCallback;
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

                    String timeStamp = dataObject.getString("timeStamp");

                    switch (sensorIdentifier) {
                        case "HEARTRATE":
                            JSONObject bpmData = dataObject.getJSONObject("data");
                            int bpm = bpmData.getInt("bpm");
                            nyxDatabase.addHeartrateReading(sessionIdentifier, timeStamp, bpm);
                            break;
                        case "ACCELEROMETER":
                            JSONObject accelerometerData = dataObject.getJSONObject("data");
                            double xAcceleration = accelerometerData.getDouble("x");
                            double yAcceleration = accelerometerData.getDouble("y");
                            double zAcceleration = accelerometerData.getDouble("z");
                            nyxDatabase.addAccelerometerReading(sessionIdentifier, timeStamp, xAcceleration, yAcceleration, zAcceleration);
                        case "BATTERY":
                            JSONObject batteryData = dataObject.getJSONObject("data");
                            int batteryLevel = batteryData.getInt("batteryLevel");
                            nyxDatabase.addBatteryeReading(sessionIdentifier, timeStamp, batteryLevel);
                        default:
                            break;
                    }

                    break;
                case "INIT_SESSION":
                    String deviceModel = dataObject.getString("deviceModel");
                    nyxDatabase.createSession(sessionIdentifier,null,null, deviceModel, 0);
                    break;
                case "START_SESSION":
                    long startTime = dataObject.getLong("startTime");
                    nyxDatabase.updateSessionStartTime(sessionIdentifier, startTime);
                    webSocketCallback.onSessionStart();
                    break;
                case "STOP_SESSION":
                    long endTime = dataObject.getLong("endTime");
                    int readingCount = dataObject.getInt("readingsCount");
                    nyxDatabase.updateSessionEndTime(sessionIdentifier, endTime, readingCount);
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
        Log.d(TAG, "ERROR with connection" + ex);
    }

}
