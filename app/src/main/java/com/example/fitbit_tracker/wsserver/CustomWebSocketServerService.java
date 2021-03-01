package com.example.fitbit_tracker.wsserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.fitbit_tracker.handlers.WebSocketCallback;
import com.example.fitbit_tracker.wsserver.CustomWebSocketServer;

public class CustomWebSocketServerService extends Service {

    private WebSocketCallback webSocketCallback;

    public CustomWebSocketServerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        CustomWebSocketServer customWebSocketServer = new CustomWebSocketServer(webSocketCallback, 8887);
        customWebSocketServer.setReuseAddr(true);
        customWebSocketServer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
