package com.example.fitbit_tracker.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitbit_tracker.model.reading.AccelerometerReading;
import com.example.fitbit_tracker.model.reading.BatteryReading;
import com.example.fitbit_tracker.model.reading.GyroscopeReading;
import com.example.fitbit_tracker.model.reading.HeartrateReading;

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

    @Query("SELECT * FROM accelerometerreading WHERE session_id = :id AND id % :n = 0")
    List<AccelerometerReading> getAccelerometerReadings(int id, int n);

    @Query("SELECT * FROM heartratereading WHERE session_id = :id AND id % :n = 0")
    List<HeartrateReading> getHeartrateReadings(int id, int n);

    @Query("SELECT * FROM batteryreading WHERE session_id = :id AND id % :n = 0")
    List<BatteryReading> getBatteryReadings(int id, int n);

    @Query("SELECT * FROM gyroscopereading WHERE session_id = :id AND id % :n = 0")
    List<GyroscopeReading> getGyroscopeReadings(int id, int n);

}
