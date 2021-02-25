package com.example.fitbit_tracker.views;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.db.NyxDatabase;
import com.example.fitbit_tracker.model.HeartrateReading;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class SessionDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_details);

        Bundle b = getIntent().getExtras();
        String sessionUUID = b.getString("SESSION_UUID");

        NyxDatabase db = new NyxDatabase(this);

        List<HeartrateReading> hrReadings = db.getAllHeartrates(sessionUUID);

        LineChart chart = (LineChart) findViewById(R.id.chart);

        List<Entry> entryList = new ArrayList<Entry>();

        int i = 0;

        for (HeartrateReading hrReading: hrReadings) {
            entryList.add(new Entry(i,hrReading.getHeartRate()));
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entryList, "Heart rate");
        dataSet.setColor(Color.rgb(1,1,1));
        dataSet.setValueTextColor(Color.rgb(1,1,1));

        LineData lineData = new LineData(dataSet);

        chart.setData(lineData);

        chart.invalidate();
    }
}