package com.example.fitbit_tracker.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.model.Sensor;
import com.example.fitbit_tracker.repository.ReadingRepository;
import com.example.fitbit_tracker.repository.SensorRepository;
import com.example.fitbit_tracker.repository.SessionSensorRepository;
import com.example.fitbit_tracker.utils.XAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import io.realm.Realm;
import io.realm.RealmResults;

public class BatchActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch);

        Bundle b = getIntent().getExtras();
        long sessionId = b.getLong("sessionId");
        long sensorId = b.getLong("sensorId");

        SensorRepository sensorRepository = new SensorRepository();
        Sensor sensor = sensorRepository.getSensor(sensorId);
        getSupportActionBar().setTitle(sensor.getSensorName());

        lineChart = findViewById(R.id.chart);

        Executor executor = Executors.newFixedThreadPool(50);
        executor.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                ReadingRepository readingRepository = new ReadingRepository();
                SessionSensorRepository sessionSensorRepository = new SessionSensorRepository();
                long start = System.currentTimeMillis();
                List<Reading> readingList = readingRepository.getReadingForSessionAndSensor(sessionId, sensorId);
                long time = System.currentTimeMillis() - start;
                Log.d(TAG, "TIME: " + time);

                HashMap<String, List<Float>> hashMap = new HashMap<>();
                List<Long> timestamps = new ArrayList<>();

                String firstReadingType = "";

                for (Reading reading : readingList) {
                    String readingType = reading.getReadingType();

                    if (!hashMap.containsKey(readingType)) {
                        if (hashMap.keySet().isEmpty()) {
                            firstReadingType = reading.getReadingType();
                        }
                        hashMap.put(readingType, new ArrayList<>());
                    }

                    hashMap.get(readingType).add(reading.getData());

                    if (readingType.equals(firstReadingType)) {
                        timestamps.add(reading.getTimeStamp());
                    }
                }

                float frequency = sessionSensorRepository.getSessionSensorFrequency(sessionId,sensorId);
                updateChart(timestamps,hashMap,frequency);
            }
        });
    }

    public void updateChart(List<Long> timestamps, HashMap<String, List<Float>> batches, float frequency) {
        int prefSamplingThreshold = getApplicationContext().getSharedPreferences("PREFERENCES", MODE_PRIVATE).getInt("prefSamplingThreshold", 50000);
        boolean prefSampling = getApplicationContext().getSharedPreferences("PREFERENCES", MODE_PRIVATE).getBoolean("prefSampling", true);

        Set<String> keys = batches.keySet();

        LineData lineData = new LineData();

        int[] colors = new int[]{
                Color.RED,
                Color.GREEN,
                Color.BLUE
        };

        long firstTimestamp = 0;
        List<String> keyList = new ArrayList<>(keys);
        for (int j = 0; j < keyList.size(); j++) {
            List<Float> batch = batches.get(keyList.get(j));
            List<Entry> entryList = new ArrayList<>();


            firstTimestamp = timestamps.get(0);

            int batchSize = batch.size();

            if (prefSampling && (batchSize > prefSamplingThreshold) && frequency > 1) {
                int freq = (int) (frequency/5);

                for (int i = 0; i < batchSize; i++) {
                    if (i % freq == 0) {
                        entryList.add(new Entry(timestamps.get(i) - firstTimestamp, batch.get(i)));
                    }
                }
            } else {
                for (int i = 0; i < batchSize; i++) {
                    entryList.add(new Entry(timestamps.get(i) - firstTimestamp, batch.get(i)));
                }
            }


            LineDataSet dataSet = new LineDataSet(entryList, keyList.get(j));
            dataSet.setColor(Color.rgb(1,1,1));
            dataSet.setValueTextColor(Color.rgb(1,1,1));

            dataSet.setColor(colors[j]);
            dataSet.setDrawCircles(false);
            dataSet.setDrawCircleHole(false);

            dataSet.setCircleColor(colors[j]);

            lineData.addDataSet(dataSet);
        }

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1.5f);

        lineChart.getDescription().setEnabled(false);
        lineChart.setData(lineData);

        xAxis.setValueFormatter(new XAxisValueFormatter(firstTimestamp));

        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

}