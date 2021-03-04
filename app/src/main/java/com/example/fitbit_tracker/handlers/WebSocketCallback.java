package com.example.fitbit_tracker.handlers;

public interface WebSocketCallback {
    void onOpen();
    void onClose();
    void onMessage(String message);
    void onSessionStart();
    void onSessionEnd();
}
