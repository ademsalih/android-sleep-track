package com.example.fitbit_tracker.wsserver;


import android.util.Log;

import com.example.fitbit_tracker.handlers.WebSocketCallback;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class CustomWebSocketServer extends WebSocketServer {
    private final String TAG = this.getClass().getSimpleName();

    private final WebSocketCallback webSocketCallback;

    public CustomWebSocketServer(WebSocketCallback webSocketCallback, int port) {
        super(new InetSocketAddress(port));
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
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d(TAG,"Reason:" + reason + ", Code: " + code);
        webSocketCallback.onClose(code);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.d(TAG, "ERROR: " + ex);
    }

}
