package com.example.fitbit_tracker;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class CustomWebSocketServer extends WebSocketServer {

    private final String TAG = this.getClass().getSimpleName();

    public CustomWebSocketServer(String host,int port) {
        super(new InetSocketAddress(host, port));

    }

    public CustomWebSocketServer(int port) {
        super(new InetSocketAddress(port));

    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d(TAG, "A client has connected: " + conn.getRemoteSocketAddress());
        conn.send("Welcome to the Android server!");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d(TAG, "Connection to " + conn.getRemoteSocketAddress() + " closed due to:" + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.d(TAG, "Received message from " + conn.getRemoteSocketAddress() + ": " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.d(TAG, "ERROR with connection" + ex);

    }

    @Override
    public void onStart() {
        Log.d(TAG, "WebSocket server started");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

}
