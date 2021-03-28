package com.example.fitbit_tracker.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Reading extends RealmObject {

    @PrimaryKey
    private long readingId;

    private long sessionId;

    private long timeStamp;

    private String sensorName;

    private String readingType;

    private float data;

    public Reading() {

    }

    /** Getters and Setters **/

    public long getReadingId() {
        return readingId;
    }

    public void setReadingId(long readingId) {
        this.readingId = readingId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
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
