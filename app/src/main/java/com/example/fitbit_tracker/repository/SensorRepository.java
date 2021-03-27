package com.example.fitbit_tracker.repository;

import android.app.Application;

import com.example.fitbit_tracker.dao.SensorDao;
import com.example.fitbit_tracker.database.NyxDatabase;
import com.example.fitbit_tracker.model.Sensor;

import java.util.List;

public class SensorRepository {

    private SensorDao sensorDao;
    private List<Sensor> sensors;

    public SensorRepository(Application application) {
        NyxDatabase db = NyxDatabase.getDatabase(application);
        this.sensorDao = db.sensorDao();
        this.sensors = sensorDao.getAllSensors();
    }

    public List<Sensor> getAllSensors() {
        return this.sensors;
    }
}
