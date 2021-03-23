package com.example.fitbit_tracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "session_id")})
public class Reading {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "sessionId")
    private int sessionId;

    @ColumnInfo(name = "sensorId")
    private int sensorId;

    @ColumnInfo(name = "timestamp")
    private long timeStamp;

    @ColumnInfo(name = "data")
    private float data;

    public Reading() {

    }

    public Reading(int sessionId, int sensorId, long timeStamp, float data) {
        this.sessionId = sessionId;
        this.sensorId = sensorId;
        this.timeStamp = timeStamp;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float getData() {
        return data;
    }

    public void setData(float data) {
        this.data = data;
    }

}
