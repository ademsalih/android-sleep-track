package com.example.fitbit_tracker.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fitbit_tracker.CustomWebSocketServer;
import com.example.fitbit_tracker.db.NyxDatabase;
import com.example.fitbit_tracker.db.NyxDbHelper;
import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.db.DatabaseContract;
import com.example.fitbit_tracker.handlers.WebSocketCallback;
import com.example.fitbit_tracker.model.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements WebSocketCallback {
    private final String TAG = this.getClass().getSimpleName();
    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;
    private CustomWebSocketServer customWebSocketServer;
    private NyxDbHelper dbHelper;
    private Context c;
    private NyxDatabase nyxDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebSocketCallback webSocketCallback = this;

        c = this.getApplicationContext();

        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);

        nyxDatabase = new NyxDatabase(getApplicationContext());

        /*Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                customWebSocketServer = new CustomWebSocketServer(webSocketCallback, 8887);
                customWebSocketServer.setReuseAddr(true);
                customWebSocketServer.start();
            }
        });*/
    }

    @Override
    public void onOpen() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                textView.setText("Connected to Hypnos");
            }
        });

    }

    @Override
    public void onClose() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                textView.setText("Looking for Fitbit device");
            }
        });
    }

    @Override
    public void onMessage(String message) {
        Log.d(TAG, message);

        String newMessage = message.replace('"','\"');

        try {
            JSONObject json = new JSONObject(newMessage);
            String command = json.getString("command");

            switch (command) {
                case "ADD_READING":
                    JSONObject readingObject = json.getJSONObject("data");

                    String sensorIdentifier = readingObject.getString("sensorIdentifier");
                    String sessionIdentifier = readingObject.getString("sessionIdentifier");
                    String timeStamp = readingObject.getString("timeStamp");

                    switch (sensorIdentifier) {
                        case "HEARTRATE":
                            JSONObject bpmData = readingObject.getJSONObject("data");
                            int bpm = bpmData.getInt("bpm");
                            nyxDatabase.addHeartrateReading(sessionIdentifier, timeStamp, bpm);
                            break;
                        case "ACCELEROMETER":
                            JSONObject accelerometerData = readingObject.getJSONObject("data");
                            double xAcceleration = accelerometerData.getDouble("x");
                            double yAcceleration = accelerometerData.getDouble("y");
                            double zAcceleration = accelerometerData.getDouble("z");
                            nyxDatabase.addAccelerometerReading(sessionIdentifier, timeStamp, xAcceleration, yAcceleration, zAcceleration);
                        default:
                            break;
                    }

                    break;
                case "START_SESSION":
                    /*Intent intent = new Intent(MainActivity.this, RecordingSessionActivity.class);
                    startActivity(intent);*/
                    JSONObject sessionObject = json.getJSONObject("data");
                    String sessionId = sessionObject.getString("sessionIdentifier");

                    nyxDatabase.createSession(sessionId,"0","0",0);
                    break;
                case "STOP_SESSION":
                    break;
                default:
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
        writeFileOnInternalStorage(this,"testing.txt", message);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        dbHelper.close();
        try {
            customWebSocketServer.stop();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        File dir = new File(mcoContext.getFilesDir(), "mydir");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile, true);
            writer.append(sBody + "\n");
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onSessionsButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this,SessionsActivity.class);
        startActivity(intent);
    }
}