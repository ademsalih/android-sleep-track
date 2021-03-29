package com.example.fitbit_tracker.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Reading extends RealmObject {

    private Session session;

    private Sensor sensor;

    private long timeStamp;

    private String readingType;

    private float data;

    public Reading() {
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getReadingType() {
        return readingType;
    }

    public void setReadingType(String readingType) {
        this.readingType = readingType;
    }

    public float getData() {
        return data;
    }

    public void setData(float data) {
        this.data = data;
    }

}
