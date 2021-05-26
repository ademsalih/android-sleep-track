package com.example.fitbit_tracker.view;

import androidx.appcompat.app.AppCompatActivity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fitbit_tracker.wsserver.CustomWebSocketService;
import com.example.fitbit_tracker.R;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private ProgressBar progressBar;
    private TextView textView;
    private TextView deviceInfoTextView;
    private ImageView imageView;
    private Context context;
    private Intent websocketServerServiceIntent;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Welcome to Nyx");

        setupBroadcastReceiver();

        context = this;
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);
        deviceInfoTextView = findViewById(R.id.deviceInfoTextView);
        imageView = findViewById(R.id.imageView);

        websocketServerServiceIntent = new Intent(context, CustomWebSocketService.class);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                startService(websocketServerServiceIntent);
            }
        });
    }

    public void setupBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (intent.getAction()) {
                    case "DISCONNECT":
                        imageView.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                        textView.setText(R.string.NOT_CONNECTED_TEXT);
                        deviceInfoTextView.setText("");
                        break;
                    case "CONNECT":
                        imageView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        textView.setText(R.string.connected_text);
                        break;
                    case "INFO":
                        String model = intent.getStringExtra("model");
                        int battery = intent.getIntExtra("battery",0);
                        deviceInfoTextView.setText("on Fitbit " + model + " (" + battery + "%)");
                        break;
                    default:
                        break;
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction("DISCONNECT");
        intentFilter.addAction("CONNECT");
        intentFilter.addAction("INFO");

        registerReceiver(broadcastReceiver, intentFilter);
    }

    public void onSessionsButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, SessionsActivity.class);
        startActivity(intent);
    }

    public void onSettingsButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService(websocketServerServiceIntent);
        unregisterReceiver(broadcastReceiver);
    }

}
