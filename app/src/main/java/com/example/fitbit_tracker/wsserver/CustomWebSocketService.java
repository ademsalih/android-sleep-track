package com.example.fitbit_tracker.wsserver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.handlers.SessionCallback;
import com.example.fitbit_tracker.handlers.WebSocketCallback;
import com.example.fitbit_tracker.view.RecordingSessionActivity;

import org.w3c.dom.Text;

import java.io.IOException;


public class CustomWebSocketService extends Service implements WebSocketCallback, SessionCallback {
    private final String TAG = this.getClass().getSimpleName();

    private WakeLock wakeLock;
    private final IBinder mBinder = new LocalBinder();
    private WebSocketCallback webSocketCallback;
    private CustomWebSocketServer customWebSocketServer;

    private final MessageHandler messageHandler;

    public class LocalBinder extends Binder {
        public CustomWebSocketService getService() {
            return CustomWebSocketService.this;
        }
    }

    public void registerCallBack(WebSocketCallback myCallback){
        this.webSocketCallback = myCallback;
    }

    public CustomWebSocketService() {
        this.messageHandler = new MessageHandler(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";

        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);

        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Nyx::WakeLock");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }

        customWebSocketServer = new CustomWebSocketServer(this,8887);
        customWebSocketServer.setReuseAddr(true);
        customWebSocketServer.start();
    }

    @SuppressLint("WakelockTimeout")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        wakeLock.acquire();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        try {
            customWebSocketServer.stop();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        wakeLock.release();
        stopForeground(true);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG, "onLowMemory");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mBinder;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        // Stop service (stopSelf()) when it's in open in the background
    }

    @Override
    public void onOpen() {
        Log.d(TAG, "onOpen");
        webSocketCallback.onOpen();

        Intent intent = new Intent("CONNECT");
        sendBroadcast(intent);
    }

    @Override
    public void onClose(int code) {
        Log.d(TAG, "onClose");
        webSocketCallback.onClose(code);

        Intent intent = new Intent("DISCONNECT");
        sendBroadcast(intent);
    }

    @Override
    public void onMessage(String message) {
        messageHandler.handleMessage(message);
        Intent intent = new Intent("ADD_READING");

        intent.putExtra("MESSAGE", message);

        sendBroadcast(intent);
    }

    @Override
    public void onSessionStart() {
        Intent recordingSessionIntent = new Intent(getApplicationContext(), RecordingSessionActivity.class);
        recordingSessionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(recordingSessionIntent);
    }

    @Override
    public void onSessionEnd() {
        Intent intent = new Intent("SESSION_ENDED");
        sendBroadcast(intent);
    }


}
