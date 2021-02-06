package com.example.fitbit_tracker;

import java.util.concurrent.Executor;

public class CustomWebSocketServerTask implements Runnable {

    @Override
    public void run() {
        CustomWebSocketServer customWebSocketServer = new CustomWebSocketServer(8887);
        customWebSocketServer.start();
    }
}
