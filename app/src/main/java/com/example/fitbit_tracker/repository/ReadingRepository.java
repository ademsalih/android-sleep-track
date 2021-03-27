package com.example.fitbit_tracker.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.fitbit_tracker.dao.ReadingDao;
import com.example.fitbit_tracker.dao.ReadingTypeDao;
import com.example.fitbit_tracker.dao.SensorDao;
import com.example.fitbit_tracker.database.NyxDatabase;
import com.example.fitbit_tracker.domain_model.Batch;
import com.example.fitbit_tracker.domain_model.ReadingDM;
import com.example.fitbit_tracker.model.ReadingType;
import com.example.fitbit_tracker.model.Sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReadingRepository {

    private ReadingDao readingDao;
    private ReadingTypeDao readingTypeDao;
    private SensorDao sensorDao;

    public ReadingRepository(Application application) {
        NyxDatabase db = NyxDatabase.getDatabase(application);
        readingDao = db.readingDao();
        sensorDao = db.sensorDao();
        readingTypeDao = db.readingTypeDao();
    }

    public MutableLiveData<List<Batch>> getReadings(long sessionId) {
        MutableLiveData<List<Batch>> allBatchesLiveData = new MutableLiveData<>();

        List<Batch> allBatches = new ArrayList<>();

        NyxDatabase.databaseReadExecutor.execute(() -> {
            List<Sensor> sensors = sensorDao.getAllSensors();
            for (Sensor sensor : sensors) {
                Batch batch = new Batch();
                batch.setBatchSensorName(sensor.getSensorName());

                long start = System.currentTimeMillis();
                List<ReadingDM> readingDMs = readingDao.getAllReadingsForSensor(sessionId, sensor.getId());
                long time = System.currentTimeMillis() - start;
                Log.d("PERFORMANCE", sensor.getSensorName() + "(" + readingDMs.size() + "): " + time + " ms");

                if (readingDMs.size() > 0) {
                    HashMap<String, List<ReadingDM>> hashMap = new HashMap<>();
                    for (ReadingDM readingDM : readingDMs) {
                        String type = readingDM.getType();
                        if (!hashMap.containsKey(type)) {
                            List<ReadingDM> readingDMS = new ArrayList<>();
                            hashMap.put(type, readingDMS);
                        }
                        hashMap.get(type).add(readingDM);
                    }
                    batch.setBatchMap(hashMap);
                    allBatches.add(batch);
                }
            }

            allBatchesLiveData.postValue(allBatches);
        });

        return allBatchesLiveData;
    }

}
