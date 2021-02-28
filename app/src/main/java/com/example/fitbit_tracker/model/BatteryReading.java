package com.example.fitbit_tracker.model;

public class BatteryReading extends Reading {

    private int batteryPercentage;

    public BatteryReading() {

    }

    public int getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(int batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }
}
