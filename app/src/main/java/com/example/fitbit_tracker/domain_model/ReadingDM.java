package com.example.fitbit_tracker.domain_model;

public class ReadingDM {

    private long timestamp;
    private float data;
    private String type;

    public ReadingDM() {
    }

    public ReadingDM(long timestamp, float data, String type) {
        this.timestamp = timestamp;
        this.data = data;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
