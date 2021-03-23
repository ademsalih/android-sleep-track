package com.example.fitbit_tracker.database;

import com.example.fitbit_tracker.model.reading.AccelerometerReading;
import com.example.fitbit_tracker.model.reading.BatteryReading;
import com.example.fitbit_tracker.model.reading.GyroscopeReading;
import com.example.fitbit_tracker.model.reading.HeartrateReading;
import com.example.fitbit_tracker.model.reading.Reading;

/*
* Configuration class used to define models that are to be created as
* tables in the Room Database
* */
public class Configuration {

    /*
    * Define all models classes in this array to save data in the database,
    * show a graph of readings and export data.
    * */
    public static Class<? extends Reading>[] READING_CLASSES = new Class[]{
            AccelerometerReading.class,
            BatteryReading.class,
            GyroscopeReading.class,
            HeartrateReading.class
    };

}
