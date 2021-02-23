package com.example.fitbit_tracker.model;

public class Session {

    private long _id;
    private String uuid;
    private long startTime;
    private long endTime;
    private int numberOfReadings;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getNumberOfReadings() {
        return numberOfReadings;
    }

    public void setNumberOfReadings(int numberOfReadings) {
        this.numberOfReadings = numberOfReadings;
    }
}
