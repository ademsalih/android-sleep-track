package com.example.fitbit_tracker.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SessionSensor extends RealmObject {

    @PrimaryKey
    private int id;

    private Session session;

    private Sensor sensor;

    private float frequency;

    public SessionSensor() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }
}
