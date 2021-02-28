package com.example.fitbit_tracker.db;

import android.provider.BaseColumns;

public class DatabaseContract {

    private DatabaseContract() {}

    public static class Session implements BaseColumns {
        public static final String TABLE_NAME = "session";
        public static final String UUID = "uuid";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String DEVICE_MODEL = "device_model";
        public static final String READINGS_COUNT = "readings_count";
    }

    public static abstract class Reading {
        public static final String SESSION_ID = "session_id";
        public static final String TIME_STAMP = "timestamp";
    }

    public static class AccelerometerReading extends Reading implements BaseColumns {
        public static final String TABLE_NAME = "accelerometer_reading";
        public static final String X_ACCELERATION = "x_acceleration";
        public static final String Y_ACCELERATION = "y_acceleration";
        public static final String Z_ACCELERATION = "z_acceleration";
    }

    public static class HeartRateReading extends Reading implements BaseColumns {
        public static final String TABLE_NAME = "heartrate_reading";
        public static final String HEARTRATE = "heartrate";
    }

    
}

