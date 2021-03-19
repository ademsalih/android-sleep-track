package com.example.fitbit_tracker.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.fitbit_tracker.model.AccelerometerReading;
import com.example.fitbit_tracker.model.AccelerometerViewModel;
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

    @Query("SELECT x_acceleration,y_acceleration,z_acceleration FROM accelerometerreading WHERE session_id = :id")
    List<AccelerometerViewModel> getAccelerometerReadings(int id);

    @Query("SELECT * FROM heartratereading WHERE session_id = :id")
    List<HeartrateReading> getHeartrateReadings(int id);

    @Query("SELECT * FROM batteryreading WHERE session_id = :id")
    List<BatteryReading> getBatteryReadings(int id);

    @Query("SELECT * FROM gyroscopereading WHERE session_id = :id")
    List<GyroscopeReading> getGyroscopeReadings(int id);

}
