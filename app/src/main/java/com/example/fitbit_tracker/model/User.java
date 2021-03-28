package com.example.fitbit_tracker.model;

import androidx.annotation.NonNull;


public class User {

    private long id;

    private String first_name;

    private String last_name;

    private long dob_timestamp;

    private long height;

    private long weight;

    public User(String first_name, String last_name, long dob_timestamp, long height, long weight) {
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

    public String getFirst_name() {
        return first_name;
    }

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
