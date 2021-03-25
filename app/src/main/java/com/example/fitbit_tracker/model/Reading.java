package com.example.fitbit_tracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "sessionId")})
public class Reading {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "sessionId")
    private long sessionId;

    @ColumnInfo(name = "readingTypeId")
    private long readingTypeId;

    @ColumnInfo(name = "timestamp")
    private long timeStamp;

    @ColumnInfo(name = "data")
    private float data;

    public Reading() {

    }

    public Reading(long sessionId, long readingTypeId, long timeStamp, float data) {
        this.sessionId = sessionId;
        this.readingTypeId = readingTypeId;
        this.timeStamp = timeStamp;
        this.data = data;
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

    public long getReadingTypeId() {
        return readingTypeId;
    }

    public void setReadingTypeId(long readingTypeId) {
        this.readingTypeId = readingTypeId;
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
