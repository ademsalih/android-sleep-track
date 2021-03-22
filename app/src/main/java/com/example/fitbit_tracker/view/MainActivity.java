package com.example.fitbit_tracker.view;

import androidx.appcompat.app.AppCompatActivity;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fitbit_tracker.handlers.ServiceCallback;
import com.example.fitbit_tracker.wsserver.CustomWebSocketService;
import com.example.fitbit_tracker.R;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements ServiceCallback {
    private final String TAG = this.getClass().getSimpleName();
    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;
    private Context context;
    private Intent websocketServerServiceIntent;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Welcome to Nyx");

        context = this;
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);

        websocketServerServiceIntent = new Intent(context, CustomWebSocketService.class);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                startService(websocketServerServiceIntent);
            }
        });

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected");
                CustomWebSocketService.LocalBinder binder = (CustomWebSocketService.LocalBinder) service;
                binder.getService().registerCallBack((ServiceCallback) context);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d(TAG, "onServiceDisconnected");
            }
        };

        bindService(websocketServerServiceIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onOpen() {
        runOnUiThread(() -> {
            imageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            textView.setText("Connected to Hypnos");
        });
    }

    @Override
    public void onClose() {
        runOnUiThread(() -> {
            imageView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            textView.setText(R.string.NOT_CONNECTED_TEXT);
        });
    }

    @Override
    public void onMessage(String message) {

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
        unbindService(serviceConnection);
    }

}
