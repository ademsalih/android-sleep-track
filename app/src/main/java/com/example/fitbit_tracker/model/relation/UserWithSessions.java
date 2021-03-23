package com.example.fitbit_tracker.model.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.model.User;

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
