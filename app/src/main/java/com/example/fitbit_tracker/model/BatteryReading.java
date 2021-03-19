package com.example.fitbit_tracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(indices = {@Index("session_id")})
public class BatteryReading extends Reading {

    @ColumnInfo(name = "battery_percentage")
    private int batteryPercentage;

    public BatteryReading(int sessionId, long timeStamp, int batteryPercentage) {
        super(sessionId, timeStamp);
        this.batteryPercentage = batteryPercentage;
    }

    public int getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(int batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

}
