package com.example.fitbit_tracker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index("uuid")})
public class Session {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "uuid")
    private String uuid;

    @ColumnInfo(name = "start_time")
    private long startTime;

    @ColumnInfo(name = "end_time")
    private long endTime;

    @ColumnInfo(name = "device_model")
    private String deviceModel;

    /**
     * This is reported from the connected Fitbit device and
     * may vary from the collection of Reading-items for a
     * given session.
     */
    @ColumnInfo(name = "readings_count")
    private int readingsCount;

    private long userId;

}
