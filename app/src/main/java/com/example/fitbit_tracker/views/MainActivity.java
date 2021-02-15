package com.example.fitbit_tracker.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fitbit_tracker.CustomWebSocketServer;
import com.example.fitbit_tracker.NyxDbHelper;
import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.DatabaseContract;
import com.example.fitbit_tracker.handlers.WebSocketCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements WebSocketCallback {
    private final String TAG = this.getClass().getSimpleName();
    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;
    private CustomWebSocketServer customWebSocketServer;
    private NyxDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebSocketCallback webSocketCallback = this;

        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);

        dbHelper = new NyxDbHelper(getApplicationContext());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.Session.END_TIME, "1");
        cv.put(DatabaseContract.Session.START_TIME, "1");
        db.insert(DatabaseContract.Session.TABLE_NAME, null, cv);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                customWebSocketServer = new CustomWebSocketServer(webSocketCallback, 8887);
                customWebSocketServer.setReuseAddr(true);
                customWebSocketServer.start();
            }
        });
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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(newMessage);

                    String model = json.getString("modelName");
                    textView.setText(textView.getText() + "\n on Fitbit " + model);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
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


}