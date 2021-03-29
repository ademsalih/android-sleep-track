package com.example.fitbit_tracker.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Sensor extends RealmObject {

    @PrimaryKey
    private int sensorId;

    private String sensorName;

    private RealmList<SessionSensor> sessionSensors;

    private RealmList<Reading> sensorReadings;

    public Sensor() {
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public RealmList<SessionSensor> getSessionSensors() {
        return sessionSensors;
    }

    public void setSessionSensors(RealmList<SessionSensor> sessionSensors) {
        this.sessionSensors = sessionSensors;
    }

    public RealmList<Reading> getSensorReadings() {
        return sensorReadings;
    }

    public void setSensorReadings(RealmList<Reading> sensorReadings) {
        this.sensorReadings = sensorReadings;
    }
}
