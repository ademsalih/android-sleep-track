package com.example.fitbit_tracker.repository;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fitbit_tracker.dao.ReadingDao;
import com.example.fitbit_tracker.dao.SensorDao;
import com.example.fitbit_tracker.dao.SessionDao;
import com.example.fitbit_tracker.database.NyxDatabase;
import com.example.fitbit_tracker.domain_model.ReadingBatchDM;
import com.example.fitbit_tracker.domain_model.ReadingDM;
import com.example.fitbit_tracker.model.Sensor;
import com.example.fitbit_tracker.model.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReadingRepository {

    private ReadingDao readingDao;
    private SensorDao sensorDao;

    public ReadingRepository(Application application) {
        NyxDatabase db = NyxDatabase.getDatabase(application);
        readingDao = db.readingDao();
        sensorDao = db.sensorDao();
    }

    public MutableLiveData<List<ReadingBatchDM>> getReadings(long sessionId) {
        MutableLiveData<List<ReadingBatchDM>> allBatchesLiveData = new MutableLiveData<List<ReadingBatchDM>>();

        List<ReadingBatchDM> allBatches = new ArrayList<>();

        NyxDatabase.databaseReadExecutor.execute(() -> {
            List<Sensor> allSensors = sensorDao.getAllSensors();
            for (Sensor sensor : allSensors) {
                ReadingBatchDM readingBatchDM = new ReadingBatchDM();
                readingBatchDM.setName(sensor.getSensorName());
                List<ReadingDM> allReadingDMs = readingDao.getAllReadingsForSensor(sessionId, sensor.getId());

                if (allReadingDMs.size() > 0) {
                    HashMap<String, List<ReadingDM>> hashMap = new HashMap<>();
                    for (ReadingDM readingDM : allReadingDMs) {
                        String type = readingDM.getType();
                        if (!hashMap.containsKey(type)) {
                            List<ReadingDM> readingDMS = new ArrayList<>();
                            hashMap.put(type, readingDMS);
                        }
                        hashMap.get(type).add(readingDM);
                    }
                    readingBatchDM.setReadingList(hashMap);
                    allBatches.add(readingBatchDM);
                }
            }

            allBatchesLiveData.postValue(allBatches);
        });

        return allBatchesLiveData;
    }

}
