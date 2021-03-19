package com.example.fitbit_tracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class AccelerometerReading extends Reading {

    @ColumnInfo(name = "x_acceleration")
    private double x;

    @ColumnInfo(name = "y_acceleration")
    private double y;

    @ColumnInfo(name = "z_acceleration")
    private double z;

    public AccelerometerReading(String uuid, long timeStamp, double x, double y, double z) {
        super(uuid, timeStamp);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

}
