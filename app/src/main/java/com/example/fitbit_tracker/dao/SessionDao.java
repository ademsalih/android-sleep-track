package com.example.fitbit_tracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitbit_tracker.model.Session;

import java.util.List;

@Dao
public interface SessionDao {

    @Query("SELECT * FROM Session ORDER BY start_time DESC")
    LiveData<List<Session>> getAll();

    @Query("SELECT * FROM Session WHERE uuid = :uuid")
    Session getSession(String uuid);

    @Insert
    void insert(Session session);

    @Insert
    void insertAll(Session... sessions);

    @Delete
    void delete(Session session);

    @Update
    void update(Session session);

}
