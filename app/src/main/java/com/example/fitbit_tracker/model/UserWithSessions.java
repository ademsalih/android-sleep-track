package com.example.fitbit_tracker.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserWithSessions {

    @Embedded
    public User user;
    @Relation(
            parentColumn = "id",
            entityColumn = "userId"
    )
    public List<Session> sessions;

}
