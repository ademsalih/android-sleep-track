package com.example.fitbit_tracker.handlers;

public interface WebSocketCallback {
    void onOpen();
    void onClose();
    void onSessionStart();
    void onSessionEnd();
}
