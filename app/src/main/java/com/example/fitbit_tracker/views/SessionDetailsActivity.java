package com.example.fitbit_tracker.views;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.db.NyxDatabase;
import com.example.fitbit_tracker.model.AccelerometerReading;
import com.example.fitbit_tracker.model.HeartrateReading;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
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

        LineChart heartrateChart = (LineChart) findViewById(R.id.heartrateChart);

        List<Entry> entryList = new ArrayList<Entry>();

        int i = 0;

        for (HeartrateReading hrReading: hrReadings) {
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

        // ACCELEROMETER

        List<AccelerometerReading> accelerometerReadings = db.getAllAccelerometerReadings(sessionUUID);

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
}