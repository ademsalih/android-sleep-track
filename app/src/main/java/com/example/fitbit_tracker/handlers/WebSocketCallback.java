package com.example.fitbit_tracker.handlers;

public interface WebSocketCallback {
    void onOpen();
    void onClose(int code);
    void onMessage(String message);
}
