package com.example.fitbit_tracker.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitbit_tracker.model.Reading;

import java.util.List;

@Dao
public interface ReadingDao {

    @Insert
    void insert(Reading... readings);

    @Update
    void update(Reading... readings);

    @Delete
    void delete(Reading... readings);

    @Query("SELECT * FROM reading WHERE sessionId = :sessionId")
    List<Reading> getAllReadings(long sessionId);

    @Query("SELECT * FROM reading WHERE sessionId = :sessionId AND sensorId = :sensorId")
    List<Reading> getAllReadingsForSensor(long sessionId, long sensorId );

    @Query("SELECT * FROM reading WHERE sessionId = :sessionId AND sensorId = :sensorId AND id % :n = 0")
    List<Reading> getSparseReadingsForSensor(long sessionId, long sensorId, int n);

}
