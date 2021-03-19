package com.example.fitbit_tracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class BatteryReading extends Reading {

    @ColumnInfo(name = "battery_percentage")
    private int batteryPercentage;

    public BatteryReading(String uuid, long timeStamp, int batteryPercentage) {
        super(uuid, timeStamp);
        this.batteryPercentage = batteryPercentage;
    }

    public int getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(int batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

}
