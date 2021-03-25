package com.example.fitbit_tracker.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitbit_tracker.model.Sensor;

import java.util.List;

@Dao
public interface SensorDao {

    @Insert
    void insert(Sensor sensor);

    @Query("SELECT * FROM sensor WHERE sensorName = :sensorName")
    Sensor getSensor(String sensorName);

    @Query("SELECT * FROM sensor")
    List<Sensor> getAllSensors();
}
