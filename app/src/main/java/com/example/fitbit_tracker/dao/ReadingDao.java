package com.example.fitbit_tracker.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitbit_tracker.model.AccelerometerReading;
import com.example.fitbit_tracker.model.BatteryReading;
import com.example.fitbit_tracker.model.GyroscopeReading;
import com.example.fitbit_tracker.model.HeartrateReading;

import java.util.List;

@Dao
public interface ReadingDao {

    @Insert
    void insert(AccelerometerReading... accelerometerReadings);

    @Insert
    void insert(HeartrateReading... heartrateReadings);

    @Insert
    void insert(BatteryReading... batteryReadings);

    @Insert
    void insert(GyroscopeReading... gyroscopeReadings);

    @Query("SELECT * FROM accelerometerreading WHERE uuid = :uuid")
    List<AccelerometerReading> getAccelerometerReadings(String uuid);

    @Query("SELECT * FROM heartratereading WHERE uuid = :uuid")
    List<HeartrateReading> getHeartrateReadings(String uuid);

    @Query("SELECT * FROM batteryreading WHERE uuid = :uuid")
    List<BatteryReading> getBatteryReadings(String uuid);

    @Query("SELECT * FROM gyroscopereading WHERE uuid = :uuid")
    List<GyroscopeReading> getGyroscopeReadings(String uuid);

}
