package com.example.fitbit_tracker.handlers;

public interface SessionCallback {
    void onSessionStart();
    void onSessionEnd();
    void onInfo(String model, int battery);
}
