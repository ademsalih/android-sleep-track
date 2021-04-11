package com.example.fitbit_tracker.view;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.repository.ReadingRepository;
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
        long sessionId = b.getLong("sessionId");
        long sensorId = b.getLong("sensorId");

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ReadingRepository readingRepository = new ReadingRepository();
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

        int[] colors = new int[]{
                Color.RED,
                Color.GREEN,
                Color.BLUE
        };

        long firstTimestamp = 0;
        List<String> keyList = new ArrayList<>(keys);
        for (int j = 0; j < keyList.size(); j++) {
            List<Reading> batch = batches.get(keyList.get(j));
            List<Entry> entryList = new ArrayList<>();


            firstTimestamp = batch.get(0).getTimeStamp();
            for (Reading reading: batch) {
                entryList.add(new Entry(reading.getTimeStamp()-firstTimestamp, reading.getData()));
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