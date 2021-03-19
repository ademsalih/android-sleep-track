package com.example.fitbit_tracker.model;


public class AccelerometerViewModel {

    private double x_acceleration;

    private double y_acceleration;

    private double z_acceleration;

    public AccelerometerViewModel() {

    }

    public double getX_acceleration() {
        return x_acceleration;
    }

    public void setX_acceleration(double x_acceleration) {
        this.x_acceleration = x_acceleration;
    }

    public double getY_acceleration() {
        return y_acceleration;
    }

    public void setY_acceleration(double y_acceleration) {
        this.y_acceleration = y_acceleration;
    }

    public double getZ_acceleration() {
        return z_acceleration;
    }

    public void setZ_acceleration(double z_acceleration) {
        this.z_acceleration = z_acceleration;
    }
}
