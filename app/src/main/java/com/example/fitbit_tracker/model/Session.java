package com.example.fitbit_tracker.model;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Session extends RealmObject {

    @PrimaryKey
    private int sessionId;

    @Required
    private String uuid;

    private long startTime;

    private long endTime;

    private int readingsCount;

    private String deviceModel;

    private User user;

    private RealmList<SessionSensor> sessionSensors;

    private RealmList<Reading> sessionReadings;

    public Session() {
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
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

    public int getReadingsCount() {
        return readingsCount;
    }

    public void setReadingsCount(int readingsCount) {
        this.readingsCount = readingsCount;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RealmList<SessionSensor> getSessionSensors() {
        return sessionSensors;
    }

    public void setSessionSensors(RealmList<SessionSensor> sessionSensors) {
        this.sessionSensors = sessionSensors;
    }

    public RealmList<Reading> getSessionReadings() {
        return sessionReadings;
    }

    public void setSessionReadings(RealmList<Reading> sessionReadings) {
        this.sessionReadings = sessionReadings;
    }
}