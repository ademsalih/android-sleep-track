package com.example.fitbit_tracker.wsserver;

public interface NyxSessionStore {

    void initSession();

    void startSession(String sessionIdentifier, long startTime);

    void stopSession(String sessionIdentifier, long endTime, int readingsCount);

    void addReading();

    void addBatchReading();

}
