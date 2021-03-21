package com.example.fitbit_tracker.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fitbit_tracker.dao.ReadingDao;
import com.example.fitbit_tracker.dao.SessionDao;
import com.example.fitbit_tracker.model.AccelerometerReading;
import com.example.fitbit_tracker.model.BatteryReading;
import com.example.fitbit_tracker.model.GyroscopeReading;
import com.example.fitbit_tracker.model.HeartrateReading;
import com.example.fitbit_tracker.model.Session;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        Session.class,
        AccelerometerReading.class,
        HeartrateReading.class,
        BatteryReading.class,
        GyroscopeReading.class
}, version = 1)
public abstract class NyxDatabase extends RoomDatabase {

    public abstract SessionDao sessionDao();
    public abstract ReadingDao readingDao();

    private static volatile NyxDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    private static final int NUMBER_OF_READ_THREADS = 100;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static final ExecutorService databaseReadExecutor = Executors.newFixedThreadPool(NUMBER_OF_READ_THREADS);

    public static NyxDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NyxDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NyxDatabase.class, "sessions_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}