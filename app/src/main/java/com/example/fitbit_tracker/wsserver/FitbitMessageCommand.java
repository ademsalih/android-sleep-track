package com.example.fitbit_tracker.wsserver;

enum FitbitMessageCommand {
    START_SEARCH("START_SEARCH"),
    ADD_READING("ADD_READING"),
    INIT_SESSION("INIT_SESSION"),
    START_SESSION("START_SESSION"),
    STOP_SESSION("STOP_SESSION");

    private final String command;

    FitbitMessageCommand(final String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }

}
