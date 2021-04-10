package com.example.fitbit_tracker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.handlers.SessionEndCallback;
import com.example.fitbit_tracker.wsserver.CustomWebSocketService;

public class RecordingSessionActivity extends AppCompatActivity implements SessionEndCallback {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_session);

        // Hide Activity Toolbar
        getSupportActionBar().hide();

        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*
         * Hide bottom navigation bar
         */
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        Intent intent = new Intent(RecordingSessionActivity.this, CustomWebSocketService.class);

        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected");
                CustomWebSocketService.LocalBinder binder = (CustomWebSocketService.LocalBinder) service;
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