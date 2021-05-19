package com.example.fitbit_tracker.model;

import androidx.annotation.NonNull;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class User extends RealmObject {

    @PrimaryKey
    private long id;

    @Required
    private String first_name;

    private String last_name;

    private long dob_timestamp;

    private int height;

    private int weight;

    private String notes;


    public User() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public long getDob_timestamp() {
        return dob_timestamp;
    }

    public void setDob_timestamp(long dob_timestamp) {
        this.dob_timestamp = dob_timestamp;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
