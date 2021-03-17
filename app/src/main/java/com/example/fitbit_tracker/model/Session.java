package com.example.fitbit_tracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Session {

    @PrimaryKey
    private long _id;

    @ColumnInfo(name = "uuid")
    private String uuid;

    @ColumnInfo(name = "start_time")
    private long startTime;

    @ColumnInfo(name = "end_time")
    private long endTime;

    @ColumnInfo(name = "device_model")
    private String deviceModel;

    @ColumnInfo(name = "readings_count")
    private int readingsCount;

}
