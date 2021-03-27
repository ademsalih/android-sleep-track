package com.example.fitbit_tracker.repository;

import android.app.Application;

import com.example.fitbit_tracker.dao.ReadingTypeDao;
import com.example.fitbit_tracker.dao.SensorDao;
import com.example.fitbit_tracker.database.NyxDatabase;
import com.example.fitbit_tracker.model.ReadingType;

import java.util.List;

public class ReadingTypeRepository {

    private ReadingTypeDao readingTypeDao;
    private SensorDao sensorDao;

    public ReadingTypeRepository(Application application) {
        NyxDatabase db = NyxDatabase.getDatabase(application);
        readingTypeDao = db.readingTypeDao();
        sensorDao = db.sensorDao();
    }

    public List<String> getReadingTypesForSensor(long sensorId) {
        return readingTypeDao.getReadingTypes(sensorId);
    }

}
