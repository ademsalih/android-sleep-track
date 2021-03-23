package com.example.fitbit_tracker.model.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.model.Session;

import java.util.List;

public class SessionWithReading {

    @Embedded
    public Session session;

    @Relation(
            parentColumn = "id",
            entityColumn = "userId"
    )
    public List<Reading> readings;

}
