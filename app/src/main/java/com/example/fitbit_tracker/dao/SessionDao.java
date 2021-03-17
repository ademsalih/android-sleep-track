package com.example.fitbit_tracker.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitbit_tracker.model.Session;

import java.util.List;

@Dao
public interface SessionDao {

    @Query("SELECT * FROM session")
    List<Session> getAll();

    @Insert
    void insert(Session session);

    @Insert
    void insertAll(Session... sessions);

    @Delete
    void delete(Session session);

}
