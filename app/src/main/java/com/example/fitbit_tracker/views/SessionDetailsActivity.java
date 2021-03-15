package com.example.fitbit_tracker.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.db.DatabaseContract;
import com.example.fitbit_tracker.db.NyxDatabase;
import com.example.fitbit_tracker.model.AccelerometerReading;
import com.example.fitbit_tracker.model.BatteryReading;
import com.example.fitbit_tracker.model.GyroscopeReading;
import com.example.fitbit_tracker.model.HeartrateReading;
import com.example.fitbit_tracker.model.Session;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.security.AccessController.getContext;

public class SessionDetailsActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private String sessionUUID;
    private NyxDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_details);

        Bundle b = getIntent().getExtras();
        sessionUUID = b.getString("SESSION_UUID");

        db = new NyxDatabase(this);

        Executor executor = Executors.newCachedThreadPool();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<HeartrateReading> hrReadings = db.getAllHeartrates(sessionUUID);
                updateHeartrateChart(hrReadings);
            }
        });

        Executor executor2 = Executors.newCachedThreadPool();
        executor2.execute(new Runnable() {
            @Override
            public void run() {
                List<AccelerometerReading> accelerometerReadings = db.getAllAccelerometerReadings(sessionUUID);
                updateAccelerometerChart(accelerometerReadings);
            }
        });

        Executor executor3 = Executors.newCachedThreadPool();
        executor3.execute(new Runnable() {
            @Override
            public void run() {
                List<BatteryReading> batteryReadings = db.getAllBatteryLevels(sessionUUID);
                updateBatteryChart(batteryReadings);
            }
        });

        Executor executor4 = Executors.newCachedThreadPool();
        executor4.execute(new Runnable() {
            @Override
            public void run() {
                List<GyroscopeReading> gyroscopeReadings = db.getAllGyroscopeReadings(sessionUUID);
                updateGyroscopeChart(gyroscopeReadings);
            }
        });
    }

    public void updateHeartrateChart(List<HeartrateReading> heartrateReadings) {
        LineChart heartrateChart = (LineChart) findViewById(R.id.heartrateChart);

        List<Entry> entryList = new ArrayList<Entry>();

        int i = 0;

        for (HeartrateReading hrReading: heartrateReadings) {
            entryList.add(new Entry(i,hrReading.getHeartRate()));
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entryList, "Heart rate");
        dataSet.setColor(Color.rgb(1,1,1));
        dataSet.setValueTextColor(Color.rgb(1,1,1));
        dataSet.setCircleHoleRadius(0f);
        dataSet.setDrawCircleHole(false);
        dataSet.setColor(Color.RED);
        dataSet.setCircleColor(Color.RED);
        dataSet.setCircleRadius(2f);

        LineData lineData = new LineData(dataSet);

        heartrateChart.setData(lineData);

        XAxis xAxis = heartrateChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

        heartrateChart.invalidate();
    }

    public void updateAccelerometerChart(List<AccelerometerReading> accelerometerReadings) {
        LineChart accelerometerChart = (LineChart) findViewById(R.id.accelerometerChart);

        List<Entry> accelerometerXEntries = new ArrayList<>();
        List<Entry> accelerometerYEntries = new ArrayList<>();
        List<Entry> accelerometerZEntries = new ArrayList<>();

        int j = 0;

        for (AccelerometerReading r: accelerometerReadings) {
            accelerometerXEntries.add(new Entry(j, (float) r.getX()));
            accelerometerYEntries.add(new Entry(j, (float) r.getY()));
            accelerometerZEntries.add(new Entry(j, (float) r.getZ()));
            j++;
        }

        LineDataSet accelerometerXDataSet = new LineDataSet(accelerometerXEntries, "Accelerometer X");
        accelerometerXDataSet.setColor(Color.rgb(1,1,1));
        accelerometerXDataSet.setValueTextColor(Color.rgb(1,1,1));
        accelerometerXDataSet.setDrawCircleHole(false);
        accelerometerXDataSet.setColor(Color.RED);
        accelerometerXDataSet.setCircleColor(Color.RED);
        accelerometerXDataSet.setCircleRadius(2f);

        LineDataSet accelerometerYDataSet = new LineDataSet(accelerometerYEntries, "Accelerometer Y");
        accelerometerYDataSet.setColor(Color.rgb(1,1,1));
        accelerometerYDataSet.setValueTextColor(Color.rgb(1,1,1));
        accelerometerYDataSet.setDrawCircleHole(false);
        accelerometerYDataSet.setColor(Color.GREEN);
        accelerometerYDataSet.setCircleColor(Color.GREEN);
        accelerometerYDataSet.setCircleRadius(2f);

        LineDataSet accelerometerZDataSet = new LineDataSet(accelerometerZEntries, "Accelerometer Z");
        accelerometerZDataSet.setColor(Color.rgb(1,1,1));
        accelerometerZDataSet.setValueTextColor(Color.rgb(1,1,1));
        accelerometerZDataSet.setCircleHoleRadius(0f);
        accelerometerZDataSet.setDrawCircleHole(false);
        accelerometerZDataSet.setColor(Color.BLUE);
        accelerometerZDataSet.setCircleColor(Color.BLUE);
        accelerometerZDataSet.setCircleRadius(2f);

        LineData accelerometerLineData = new LineData();
        accelerometerLineData.addDataSet(accelerometerXDataSet);
        accelerometerLineData.addDataSet(accelerometerYDataSet);
        accelerometerLineData.addDataSet(accelerometerZDataSet);

        accelerometerChart.setData(accelerometerLineData);

        accelerometerChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        accelerometerChart.invalidate();
    }

    public void updateBatteryChart(List<BatteryReading> batteryReadings) {
        LineChart batteryChart = (LineChart) findViewById(R.id.batteryChart);

        List<Entry> batteryEntryList = new ArrayList<Entry>();

        int k = 0;

        for (BatteryReading batteryReading : batteryReadings) {
            batteryEntryList.add(new Entry(k, batteryReading.getBatteryPercentage()));
            k++;
        }

        LineDataSet batteryDataSet = new LineDataSet(batteryEntryList, "Battery percentage");
        batteryDataSet.setColor(Color.rgb(1,1,1));
        batteryDataSet.setValueTextColor(Color.rgb(1,1,1));
        batteryDataSet.setCircleHoleRadius(0f);
        batteryDataSet.setDrawCircleHole(false);
        batteryDataSet.setColor(0xFFFF9300);
        batteryDataSet.setCircleColor(0xFFFF9300);
        batteryDataSet.setCircleRadius(2f);

        LineData batteryLineData = new LineData(batteryDataSet);

        batteryChart.setData(batteryLineData);

        XAxis xAxisBat = batteryChart.getXAxis();
        xAxisBat.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

        batteryChart.invalidate();
    }

    public void updateGyroscopeChart(List<GyroscopeReading> gyroscopeReadings) {
        LineChart gyroscopeChart = (LineChart) findViewById(R.id.gyroscopeChart);

        List<Entry> gyroscopeXEntries = new ArrayList<>();
        List<Entry> gyroscopeYEntries = new ArrayList<>();
        List<Entry> gyroscopeZEntries = new ArrayList<>();

        int j = 0;

        for (GyroscopeReading r: gyroscopeReadings) {
            gyroscopeXEntries.add(new Entry(j, (float) r.getX()));
            gyroscopeYEntries.add(new Entry(j, (float) r.getY()));
            gyroscopeZEntries.add(new Entry(j, (float) r.getZ()));
            j++;
        }

        LineDataSet gyroscopeXDataSet = new LineDataSet(gyroscopeXEntries, "Gyroscope X");
        gyroscopeXDataSet.setColor(Color.rgb(1,1,1));
        gyroscopeXDataSet.setValueTextColor(Color.rgb(1,1,1));
        gyroscopeXDataSet.setDrawCircleHole(false);
        gyroscopeXDataSet.setColor(Color.RED);
        gyroscopeXDataSet.setCircleColor(Color.RED);
        gyroscopeXDataSet.setCircleRadius(2f);

        LineDataSet gyroscopeYDataSet = new LineDataSet(gyroscopeYEntries, "Gyroscope Y");
        gyroscopeYDataSet.setColor(Color.rgb(1,1,1));
        gyroscopeYDataSet.setValueTextColor(Color.rgb(1,1,1));
        gyroscopeYDataSet.setDrawCircleHole(false);
        gyroscopeYDataSet.setColor(Color.GREEN);
        gyroscopeYDataSet.setCircleColor(Color.GREEN);
        gyroscopeYDataSet.setCircleRadius(2f);

        LineDataSet gyroscopeZDataSet = new LineDataSet(gyroscopeZEntries, "Gyroscope Z");
        gyroscopeZDataSet.setColor(Color.rgb(1,1,1));
        gyroscopeZDataSet.setValueTextColor(Color.rgb(1,1,1));
        gyroscopeZDataSet.setCircleHoleRadius(0f);
        gyroscopeZDataSet.setDrawCircleHole(false);
        gyroscopeZDataSet.setColor(Color.BLUE);
        gyroscopeZDataSet.setCircleColor(Color.BLUE);
        gyroscopeZDataSet.setCircleRadius(2f);

        LineData gyroscopeLineData = new LineData();
        gyroscopeLineData.addDataSet(gyroscopeXDataSet);
        gyroscopeLineData.addDataSet(gyroscopeYDataSet);
        gyroscopeLineData.addDataSet(gyroscopeZDataSet);

        gyroscopeChart.setData(gyroscopeLineData);

        gyroscopeChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        gyroscopeChart.invalidate();
    }

    public void onExportButtonClicked(View view) {
        List<HeartrateReading> heartrateReadings = db.getAllHeartrates(sessionUUID);
        List<AccelerometerReading> accelerometerReadings = db.getAllAccelerometerReadings(sessionUUID);
        List<BatteryReading> batteryReadings = db.getAllBatteryLevels(sessionUUID);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionUUID", sessionUUID);

            JSONArray accArray = new JSONArray();
            for (AccelerometerReading accelerometerReading : accelerometerReadings) {
                JSONObject acc = new JSONObject();
                acc.put("x",accelerometerReading.getX());
                acc.put("y",accelerometerReading.getY());
                acc.put("z",accelerometerReading.getZ());
                acc.put("timestamp",accelerometerReading.getTimeStamp());
                accArray.put(acc);
            }
            jsonObject.put("accelerometerReadings", accArray);

            JSONArray hrArray = new JSONArray();
            for (HeartrateReading heartrateReading : heartrateReadings) {
                JSONObject hr = new JSONObject();
                hr.put("bpm", heartrateReading.getHeartRate());
                hr.put("timestamp", heartrateReading.getTimeStamp());
                hrArray.put(hr);
            }
            jsonObject.put("heartrateReadings", hrArray);

            JSONArray battArray = new JSONArray();
            for (BatteryReading batteryReading : batteryReadings) {
                JSONObject batt = new JSONObject();
                batt.put("batteryLevel", batteryReading.getBatteryPercentage());
                batt.put("timestamp", batteryReading.getTimeStamp());
                battArray.put(batt);
            }
            jsonObject.put("batteryReadings", battArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            //Create a file and write the String to it
            BufferedWriter out;
            File imagePath = new File(getFilesDir(), "json");
            imagePath.mkdir();
            File imageFile = new File(imagePath.getPath(), "session.json");


            FileWriter fileWriter = new FileWriter(imageFile.getPath());
            out = new BufferedWriter(fileWriter);
            out.write(jsonObject.toString());
            out.close();

            // Write data in your file
            Uri uri = FileProvider.getUriForFile(this, "com.example.fileprovider", imageFile);

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sendIntent.setType("text/plain");
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            String title = "Share session";

            startActivity(Intent.createChooser(sendIntent, title));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}