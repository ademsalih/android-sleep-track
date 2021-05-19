package com.example.fitbit_tracker.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SessionSensor extends RealmObject {

    @PrimaryKey
    private long id;

    private long sessionId;

    private long sensorId;

    private float frequency;

    public SessionSensor() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }
}
