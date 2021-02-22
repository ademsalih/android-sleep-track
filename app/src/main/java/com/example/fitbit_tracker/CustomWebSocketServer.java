package com.example.fitbit_tracker;

import android.util.Log;

import com.example.fitbit_tracker.handlers.WebSocketCallback;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class CustomWebSocketServer extends WebSocketServer {

    private final String TAG = this.getClass().getSimpleName();
    private final WebSocketCallback webSocketOpenCallback;

    public CustomWebSocketServer(WebSocketCallback cb, String host, int port) {
        super(new InetSocketAddress(host, port));
        this.webSocketOpenCallback = cb;

    }

    public CustomWebSocketServer(WebSocketCallback cb, int port) {
        super(new InetSocketAddress(port));
        this.webSocketOpenCallback = cb;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d(TAG, "A client has connected: " + conn.getRemoteSocketAddress());
        conn.send("Welcome to the Android server!");
        webSocketOpenCallback.onOpen();
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d(TAG, "Connection to " + conn.getRemoteSocketAddress() + " closed due to:" + reason);
        webSocketOpenCallback.onClose();
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.d(TAG, "Received message from " + conn.getRemoteSocketAddress() + ": " + message);

        webSocketOpenCallback.onMessage(message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.d(TAG, "ERROR with connection" + ex);

    }

    @Override
    public void onStart() {
        Log.d(TAG, "WebSocket server started");
    }

}
