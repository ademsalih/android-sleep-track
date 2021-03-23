package com.example.fitbit_tracker.model.reading;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(indices = {@Index("session_id")})
public class HeartrateReading extends Reading {

    @ColumnInfo(name = "heart_rate")
    private int heartRate;

    public HeartrateReading(int sessionId, long timeStamp, int heartRate) {
        super(sessionId, timeStamp);
        this.heartRate = heartRate;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

}
