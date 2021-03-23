package com.example.fitbit_tracker.model.reading;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "session_id")})
public class Reading {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "session_id")
    private int sessionId;

    @ColumnInfo(name = "timestamp")
    private long timeStamp;

    public Reading(int sessionId, long timeStamp) {
        this.sessionId = sessionId;
        this.timeStamp = timeStamp;
    }

    public Reading() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
