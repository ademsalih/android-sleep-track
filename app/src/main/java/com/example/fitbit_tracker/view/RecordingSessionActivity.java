package com.example.fitbit_tracker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.handlers.WebSocketCallback;


public class RecordingSessionActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private BroadcastReceiver broadcastReceiver;
    private TextView recordingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_session);

        recordingTextView = findViewById(R.id.recordingTextView);

        // Hide Activity Toolbar
        getSupportActionBar().hide();

        // Hide Status Bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*
         * Hide bottom navigation bar
         */
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals("SESSION_ENDED")) {
                    finish();
                } else if (intent.getAction().equals("DISCONNECT")) {
                    recordingTextView.setText(R.string.CONNECTION_LOST_TEXT);
                } else if (intent.getAction().equals("CONNECT")) {
                    recordingTextView.setText(R.string.recordingText);
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction("SESSION_ENDED");
        intentFilter.addAction("DISCONNECT");
        intentFilter.addAction("CONNECT");

        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        /*
         * Intentionally left blank to avoid exiting in the
         * midst of a session if back button is pressed.
         */
    }

}