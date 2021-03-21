package com.example.fitbit_tracker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.dao.ReadingDao;
import com.example.fitbit_tracker.db.NyxDatabase;
import com.example.fitbit_tracker.model.AccelerometerReading;
import com.example.fitbit_tracker.model.BatteryReading;
import com.example.fitbit_tracker.model.GyroscopeReading;
import com.example.fitbit_tracker.model.HeartrateReading;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SessionDetailsActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private int sessionID;
    private ReadingDao readingDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_details);
        readingDao = NyxDatabase.getDatabase(getBaseContext()).readingDao();

        Bundle b = getIntent().getExtras();
        sessionID = b.getInt("SESSION_ID");

        int threads = 100;

        List<HeartrateReading> hrReadings = new ArrayList<>();
        Executor executor = Executors.newFixedThreadPool(threads);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();

                List<HeartrateReading> hrReadings = readingDao.getHeartrateReadings(sessionID, 1);

                long time = System.currentTimeMillis() - start;
                Log.d(TAG, "Heartrate (" + hrReadings.size() +  ") : " + time + " ms");
                updateHeartrateChart(hrReadings);
            }
        });



        Executor executor2 = Executors.newFixedThreadPool(threads);
        executor2.execute(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();

                List<BatteryReading> batteryReadings = readingDao.getBatteryReadings(sessionID, 1);

                long time = System.currentTimeMillis() - start;
                Log.d(TAG, "Battery (" + batteryReadings.size() +  ") : " + time + " ms");

                updateBatteryChart(batteryReadings);
            }
        });

        Executor executor3 = Executors.newFixedThreadPool(threads);
        executor3.execute(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();

                List<AccelerometerReading> accelerometerReadings = readingDao.getAccelerometerReadings(sessionID, 25);

                long time = System.currentTimeMillis() - start;
                Log.d(TAG, "Accelerometer (" + accelerometerReadings.size() +  ") : " + time + " ms");


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long start2 = System.currentTimeMillis();
                        updateAccelerometerChart(accelerometerReadings);
                        long time2 = System.currentTimeMillis() - start2;
                        Log.d(TAG, "Accelerometer Graph : " + time2 + " ms");
                    }
                });

            }
        });

        Executor executor4 = Executors.newFixedThreadPool(threads);
        executor4.execute(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();

                List<GyroscopeReading> gyroscopeReadings = readingDao.getGyroscopeReadings(sessionID, 25);

                long time = System.currentTimeMillis() - start;
                Log.d(TAG, "Gyroscope (" + gyroscopeReadings.size() +  ") : " + time + " ms");

                updateGyroscopeChart(gyroscopeReadings);
            }
        });

    }

    public void updateHeartrateChart(List<HeartrateReading> heartrateReadings) {
        LineChart heartrateChart = findViewById(R.id.heartrateChart);

        List<Entry> entryList = new ArrayList<Entry>();

        int i = 0;

        for (HeartrateReading hrReading: heartrateReadings) {
            entryList.add(new Entry(i,hrReading.getHeartRate()));
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entryList, "Heart rate");
        dataSet.setColor(Color.rgb(1,1,1));
        dataSet.setValueTextColor(Color.rgb(1,1,1));
        dataSet.setCircleHoleRadius(1f);
        dataSet.setDrawCircleHole(false);
        dataSet.setColor(Color.RED);
        dataSet.setCircleColor(Color.RED);
        dataSet.setCircleRadius(2f);

        LineData lineData = new LineData(dataSet);

        heartrateChart.setData(lineData);

        heartrateChart.invalidate();
    }

    public void updateAccelerometerChart(List<AccelerometerReading> accelerometerReadings) {
        LineChart accelerometerChart = findViewById(R.id.accelerometerChart);

        accelerometerChart.setTouchEnabled(true);
        accelerometerChart.setDragEnabled(true);
        accelerometerChart.setScaleEnabled(true);
        accelerometerChart.setDrawGridBackground(false);
        accelerometerChart.setPinchZoom(true);

        List<Entry> accelerometerXEntries = new ArrayList<>();
        List<Entry> accelerometerYEntries = new ArrayList<>();
        List<Entry> accelerometerZEntries = new ArrayList<>();

        for (int i = 0; i < accelerometerReadings.size(); i++) {
            accelerometerXEntries.add(new Entry(i, (float) accelerometerReadings.get(i).getX()));
            accelerometerYEntries.add(new Entry(i, (float) accelerometerReadings.get(i).getY()));
            accelerometerZEntries.add(new Entry(i, (float) accelerometerReadings.get(i).getZ()));
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
        accelerometerZDataSet.setCircleHoleRadius(1f);
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
        batteryDataSet.setCircleHoleRadius(1f);
        batteryDataSet.setDrawCircleHole(false);
        batteryDataSet.setColor(0xFFFF9300);
        batteryDataSet.setCircleColor(0xFFFF9300);
        batteryDataSet.setCircleRadius(2f);

        LineData batteryLineData = new LineData(batteryDataSet);

        batteryChart.setData(batteryLineData);

        batteryChart.invalidate();
    }

    public void updateGyroscopeChart(List<GyroscopeReading> gyroscopeReadings) {
        LineChart gyroscopeChart = (LineChart) findViewById(R.id.gyroscopeChart);

        List<Entry> gyroscopeXEntries = new ArrayList<>();
        List<Entry> gyroscopeYEntries = new ArrayList<>();
        List<Entry> gyroscopeZEntries = new ArrayList<>();


        for (int i = 0; i < gyroscopeReadings.size(); i++) {
            gyroscopeXEntries.add(new Entry(i, (float) gyroscopeReadings.get(i).getX()));
            gyroscopeYEntries.add(new Entry(i, (float) gyroscopeReadings.get(i).getY()));
            gyroscopeZEntries.add(new Entry(i, (float) gyroscopeReadings.get(i).getZ()));
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
        gyroscopeZDataSet.setCircleHoleRadius(1f);
        gyroscopeZDataSet.setDrawCircleHole(false);
        gyroscopeZDataSet.setColor(Color.BLUE);
        gyroscopeZDataSet.setCircleColor(Color.BLUE);
        gyroscopeZDataSet.setCircleRadius(2f);

        LineData gyroscopeLineData = new LineData();
        gyroscopeLineData.addDataSet(gyroscopeXDataSet);
        gyroscopeLineData.addDataSet(gyroscopeYDataSet);
        gyroscopeLineData.addDataSet(gyroscopeZDataSet);

        gyroscopeChart.setData(gyroscopeLineData);

        gyroscopeChart.invalidate();
    }

    /*public void onExportButtonClicked(View view) {
        List<HeartrateReading> heartrateReadings = db.getAllHeartrates(sessionUUID);
        List<AccelerometerReading> accelerometerReadings = db.getAllAccelerometerReadings(sessionUUID);
        List<BatteryReading> batteryReadings = db.getAllBatteryLevels(sessionUUID);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sessionUUID", sessionUUID);

            JSONArray accArray = new JSONArray();
            for (AccelerometerReading accelerometerReading : accelerometerReadings) {
                JSONObject acc = new JSONObject();
                acc.put("x", accelerometerReading.getX());
                acc.put("y", accelerometerReading.getY());
                acc.put("z", accelerometerReading.getZ());
                acc.put("timestamp", accelerometerReading.getTimeStamp());
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
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            String title = "Share session";

            startActivity(Intent.createChooser(sendIntent, title));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}