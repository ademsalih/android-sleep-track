package com.example.fitbit_tracker.domain_model;

public class ReadingDM {

    private long timestamp;
    private float data;
    private String sensorName;
    private String type;

    public ReadingDM() {
    }

    public ReadingDM(long timestamp, float data, String sensorName, String type) {
        this.timestamp = timestamp;
        this.data = data;
        this.sensorName = sensorName;
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getData() {
        return data;
    }

    public void setData(float data) {
        this.data = data;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
