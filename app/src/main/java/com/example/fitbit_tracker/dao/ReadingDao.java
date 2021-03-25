package com.example.fitbit_tracker.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitbit_tracker.domain_model.ReadingDM;
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

    @Query("select r.timestamp, r.data, s.sensorName, rt.type from reading as r, readingtype as rt, sensor as s where sessionId = :sessionId and s.id = :sensorId and s.id = rt.sensorId and r.readingTypeId = rt.id")
    List<ReadingDM> getAllReadingsForSensor(long sessionId, long sensorId);

    @Query("SELECT * FROM reading AS r, readingtype AS rt WHERE sessionId = :sessionId AND r.readingTypeId = rt.id AND rt.sensorId = :sensorId AND rt.type = :readingType")
    List<Reading> getReadingsForSensorAndReadingType(long sessionId, long sensorId, String readingType);

}
