package com.example.fitbit_tracker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.adapter.listadapter.SessionSensorListAdapter;
import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.model.Sensor;
import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.model.SessionSensor;
import com.example.fitbit_tracker.repository.ReadingRepository;
import com.example.fitbit_tracker.repository.SensorRepository;
import com.example.fitbit_tracker.repository.SessionRepository;
import com.example.fitbit_tracker.repository.SessionSensorRepository;
import com.example.fitbit_tracker.viewmodel.SessionSensorViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SessionDetailActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private SessionSensorViewModel sessionSensorViewModel;
    private long sessionId;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_detail);
        getSupportActionBar().setTitle("Session Details");
        context = this;

        Bundle b = getIntent().getExtras();
        sessionId = b.getLong("sessionId");

        SessionSensorListAdapter sessionReadingListAdapater = new SessionSensorListAdapter(new SessionSensorListAdapter.ReadingDiff(), getBaseContext());

        RecyclerView recyclerView = findViewById(R.id.readingRecyclerView);
        recyclerView.setAdapter(sessionReadingListAdapater);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ViewModelProvider.AndroidViewModelFactory androidViewModelFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, androidViewModelFactory);

        sessionSensorViewModel = viewModelProvider.get(SessionSensorViewModel.class);
        sessionSensorViewModel.getAllSessionSensors(sessionId).observe(this, new Observer<List<SessionSensor>>() {
            @Override
            public void onChanged(List<SessionSensor> batches) {
                sessionReadingListAdapater.submitList(batches);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exportMenuButton:
                onExportButtonClicked();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void onExportButtonClicked() {

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();

                ReadingRepository readingRepository = new ReadingRepository();
                SessionRepository sessionRepository = new SessionRepository();
                SessionSensorRepository sessionSensorRepository = new SessionSensorRepository();
                SensorRepository sensorRepository = new SensorRepository();

                Session session = sessionRepository.getSession(sessionId);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("sessionUUID", session.getUuid());
                    jsonObject.put("sessionStart", session.getStartTime());
                    jsonObject.put("sessionEnd", session.getEndTime());
                    jsonObject.put("readingsCount", session.getReadingsCount());
                    jsonObject.put("deviceModel", session.getDeviceModel());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<SessionSensor> sessionSensors = sessionSensorRepository.getSessionSensorsAsList(sessionId);

                JSONArray sensorDataArray = new JSONArray();

                for (SessionSensor sessionSensor : sessionSensors) {
                    Sensor sensor = sensorRepository.getSensor(sessionSensor.getSensorId());

                    JSONObject sensorDataObject = new JSONObject();

                    try {
                        sensorDataObject.put("sensor", sensor.getSensorName());
                        sensorDataObject.put("frequency", sessionSensor.getFrequency());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    List<Reading> readingList = readingRepository.getReadingForSessionAndSensor(sessionId, sessionSensor.getSensorId());
                    JSONArray timeStampArray = new JSONArray();

                    HashMap<String, List<Float>> hashMap = new HashMap<>();

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
                            timeStampArray.put(reading.getTimeStamp());
                        }
                    }

                    try {
                        sensorDataObject.put("timestamps", timeStampArray);

                        for (String key : hashMap.keySet()) {
                            JSONObject readingTypeObject = new JSONObject();
                            readingTypeObject.put("type", key);
                            readingTypeObject.put("items", new JSONArray(hashMap.get(key)));
                            sensorDataObject.put("readings", readingTypeObject);
                        }
                        sensorDataArray.put(sensorDataObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    jsonObject.put("data", sensorDataArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    BufferedWriter out;
                    File imagePath = new File(getFilesDir(), "json");
                    imagePath.mkdir();
                    File imageFile = new File(imagePath.getPath(), "session.json");

                    FileWriter fileWriter = new FileWriter(imageFile.getPath());
                    out = new BufferedWriter(fileWriter);
                    out.write(jsonObject.toString());
                    out.close();

                    Uri uri = FileProvider.getUriForFile(context, "com.example.fileprovider", imageFile);

                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    sendIntent.setType("text/plain");
                    sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    String title = "Share session";

                    startActivity(Intent.createChooser(sendIntent, title));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                long time = System.currentTimeMillis() - start;
                Log.d(TAG, "Export Duartion: " + time + " ms");
            }
        });
    }

}
