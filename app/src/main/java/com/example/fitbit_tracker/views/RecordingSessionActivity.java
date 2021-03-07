package com.example.fitbit_tracker.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.handlers.ServiceCallback;
import com.example.fitbit_tracker.handlers.SessionEndCallback;
import com.example.fitbit_tracker.wsserver.CustomWebSocketServerService;

public class RecordingSessionActivity extends AppCompatActivity implements SessionEndCallback {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_session);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = new Intent(RecordingSessionActivity.this, CustomWebSocketServerService.class);

        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected");
                CustomWebSocketServerService.LocalBinder binder = (CustomWebSocketServerService.LocalBinder) service;
                binder.getService().registerSessionEndCallback(RecordingSessionActivity.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected");
                // Handle service disconnection while recording
            }
        }, BIND_AUTO_CREATE);
    }

    @Override
    public void onSessionEnd() {
        finish();
    }

    @Override
    public void onBackPressed() {

    }

}