package com.example.fitbit_tracker.repository;

import com.example.fitbit_tracker.model.Sensor;
import com.example.fitbit_tracker.model.SessionSensor;

import java.util.List;

import io.realm.Realm;

public class SensorRepository {

    private final Realm realm;

    public SensorRepository() {
        this.realm = Realm.getDefaultInstance();
    }

    public Sensor getSensor(long sensorId) {
        return realm.where(Sensor.class).equalTo("sensorId", sensorId).findFirst();
    }

    public List<Sensor> getSensors() {
        return realm.where(Sensor.class).findAll();
    }
}
