package com.example.fitbit_tracker.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Reading extends RealmObject {

    private long sessionId;

    private long sensorId;

    private long timeStamp;

    private String readingType;

    private float data;

    public Reading() {
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
