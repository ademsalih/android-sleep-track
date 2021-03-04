package com.example.fitbit_tracker.handlers;

public interface ServiceCallback {
    void onOpen();
    void onClose();
    void onMessage(String message);
}
