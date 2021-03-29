package com.example.fitbit_tracker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.repository.ReadingRepository;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.realm.Realm;
import io.realm.RealmResults;

public class BatchActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch);

        lineChart = findViewById(R.id.chart);

        Bundle b = getIntent().getExtras();
        int sessionId = b.getInt("sessionId");
        int sensorId = b.getInt("sensorId");



        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ReadingRepository readingRepository = new ReadingRepository(Realm.getDefaultInstance());
                long start = System.currentTimeMillis();
                List<Reading> readingList = readingRepository.getReadingForSessionAndSensor(sessionId, sensorId);
                long time = System.currentTimeMillis() - start;
                Log.d(TAG, "TIME: " + time);


                HashMap<String, List<Reading>> hashMap = new HashMap<>();
                List<Long> timestamps = new ArrayList<>();

                for (Reading reading : readingList) {
                    String readingType = reading.getReadingType();
                    if (!hashMap.containsKey(readingType)) {
                        hashMap.put(readingType, new ArrayList<>());
                    }

                    hashMap.get(readingType).add(reading);
                }

                updateChart(timestamps,hashMap);
            }
        });
    }

    public void updateChart(List<Long> timestamps, HashMap<String, List<Reading>> batches) {
        Set<String> keys = batches.keySet();

        LineData lineData = new LineData();

        for (String key : keys) {
            List<Reading> batch = batches.get(key);
            List<Entry> entryList = new ArrayList<>();

            int i = 0;
            for (Reading reading: batch) {
                entryList.add(new Entry(i, reading.getData()));
                i++;
            }

            LineDataSet dataSet = new LineDataSet(entryList, key);
            dataSet.setColor(Color.rgb(1,1,1));
            dataSet.setValueTextColor(Color.rgb(1,1,1));
            dataSet.setCircleHoleRadius(1f);
            dataSet.setDrawCircleHole(false);
            dataSet.setColor(Color.RED);
            dataSet.setCircleColor(Color.RED);
            dataSet.setCircleRadius(2f);

            lineData.addDataSet(dataSet);
        }

        lineChart.setData(lineData);
        lineChart.notifyDataSetChanged();
        lineChart.setVisibleXRangeMaximum(5000);
        lineChart.invalidate();
    }

}