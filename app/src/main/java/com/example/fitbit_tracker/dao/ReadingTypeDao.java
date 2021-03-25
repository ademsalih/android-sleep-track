package com.example.fitbit_tracker.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitbit_tracker.model.ReadingType;

import java.util.List;

@Dao
public interface ReadingTypeDao {

    @Insert
    void insert(ReadingType readingType);

    @Query("SELECT id FROM readingtype WHERE sensorId = :sensorId AND type = :type")
    long getReadingType(long sensorId, String type);

    @Query("SELECT type FROM readingtype WHERE sensorId = :sensorId")
    List<String> getReadingTypes(long sensorId);

}
