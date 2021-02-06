package com.example.fitbit_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private ProgressBar progressBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);

        ActivityManager am = (ActivityManager)this.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> listProcess = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo runningProInfo:listProcess){
            Log.d("Running Processes", ":"+runningProInfo.processName);
        }

        Executor executor = Executors.newSingleThreadExecutor();


        executor.execute(new CustomWebSocketServerTask());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}