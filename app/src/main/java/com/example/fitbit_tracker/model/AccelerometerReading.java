package com.example.fitbit_tracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(indices = {@Index("session_id")})
public class AccelerometerReading extends Reading {

    @ColumnInfo(name = "x_acceleration")
    private double x;

    @ColumnInfo(name = "y_acceleration")
    private double y;

    @ColumnInfo(name = "z_acceleration")
    private double z;

    public AccelerometerReading() {

    }

    public AccelerometerReading(int sessionId, long timeStamp, double x, double y, double z) {
        super(sessionId, timeStamp);
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
