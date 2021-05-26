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
import android.widget.Toast;

import com.example.fitbit_tracker.R;


public class RecordingSessionActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private BroadcastReceiver broadcastReceiver;
    private TextView recordingTextView;
    //private TextView debugTextView;
    private boolean connected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_session);

        recordingTextView = findViewById(R.id.recordingTextView);
        //debugTextView = findViewById(R.id.debugTextView);

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

                switch (intent.getAction()) {
                    case "SESSION_ENDED":
                        finish();
                        break;
                    case "SESSION_FINALIZING":
                        recordingTextView.setText(R.string.FINALIZING_TEXT);
                        break;
                    case "DISCONNECT":
                        recordingTextView.setText(R.string.CONNECTION_LOST_TEXT);
                        connected = false;
                        break;
                    case "CONNECT":
                        recordingTextView.setText(R.string.recordingText);
                        connected = true;
                        break;
                    default:
                        break;
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
        if (!connected) {
            finish();
        } else {
            Toast.makeText(this, R.string.SESSION_ACTIVE_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }

}