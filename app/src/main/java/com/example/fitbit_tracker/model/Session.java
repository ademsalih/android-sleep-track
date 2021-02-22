package com.example.fitbit_tracker.model;

public class Session {

    private long _id;
    private String uuid;
    private String startTime;
    private String endTime;
    private int numberOfReadings;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getNumberOfReadings() {
        return numberOfReadings;
    }

    public void setNumberOfReadings(int numberOfReadings) {
        this.numberOfReadings = numberOfReadings;
    }
}
