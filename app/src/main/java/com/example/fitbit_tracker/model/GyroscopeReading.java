package com.example.fitbit_tracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class GyroscopeReading extends Reading {

    @ColumnInfo(name = "x_velocity")
    private double x;

    @ColumnInfo(name = "y_velocity")
    private double y;

    @ColumnInfo(name = "z_velocity")
    private double z;

    public GyroscopeReading(String uuid, long timeStamp, double x, double y, double z) {
        super(uuid, timeStamp);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
