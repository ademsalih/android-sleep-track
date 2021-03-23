package com.example.fitbit_tracker.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "first_name")
    @NonNull
    private String first_name;

    @ColumnInfo(name = "last_name")
    @NonNull
    private String last_name;

    @ColumnInfo(name = "dob_timestamp")
    @NonNull
    private long dob_timestamp;

    @ColumnInfo(name = "height")
    private long height;

    @ColumnInfo(name = "weight")
    private long weight;

    public User(@NonNull String first_name, @NonNull String last_name, long dob_timestamp, long height, long weight) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob_timestamp = dob_timestamp;
        this.height = height;
        this.weight = weight;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getFirst_name() {
        return first_name;
    }

    @NonNull
    public String getLast_name() {
        return last_name;
    }

    public long getDob_timestamp() {
        return dob_timestamp;
    }

    public long getHeight() {
        return height;
    }

    public long getWeight() {
        return weight;
    }

}
