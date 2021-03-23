package com.example.fitbit_tracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fitbit_tracker.dao.ReadingBaseDao;
import com.example.fitbit_tracker.dao.ReadingDao;
import com.example.fitbit_tracker.dao.SessionDao;
import com.example.fitbit_tracker.model.reading.AccelerometerReading;
import com.example.fitbit_tracker.model.reading.BatteryReading;
import com.example.fitbit_tracker.model.reading.GyroscopeReading;
import com.example.fitbit_tracker.model.reading.HeartrateReading;
import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.model.reading.Reading;

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
    public abstract ReadingBaseDao<?> readingBaseDao();

    private static volatile NyxDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    private static final int NUMBER_OF_READ_THREADS = 100;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static final ExecutorService databaseReadExecutor = Executors.newFixedThreadPool(NUMBER_OF_READ_THREADS);

    public static Class<?> getEntityClass(String tableName) {
        try {
            return Class.forName(tableName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Reading.class;
    }

    public static String getTableName(Class<?> entityClass) {
        return entityClass.getName();
    }

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