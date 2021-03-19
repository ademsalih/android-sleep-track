package com.example.fitbit_tracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class HeartrateReading extends Reading {

    @ColumnInfo(name = "heart_rate")
    private int heartRate;

    public HeartrateReading(String uuid, long timeStamp) {
        super(uuid, timeStamp);
    }

    public HeartrateReading(String uuid, long timestamp, int heartRate) {
        super(uuid,timestamp);
        this.heartRate = heartRate;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

}
