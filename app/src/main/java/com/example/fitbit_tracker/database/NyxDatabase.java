package com.example.fitbit_tracker.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.fitbit_tracker.dao.ReadingDao;
import com.example.fitbit_tracker.dao.ReadingTypeDao;
import com.example.fitbit_tracker.dao.SensorDao;
import com.example.fitbit_tracker.dao.SessionDao;
import com.example.fitbit_tracker.model.ReadingType;
import com.example.fitbit_tracker.model.Sensor;
import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        User.class,
        Session.class,
        Reading.class,
        ReadingType.class,
        Sensor.class
}, version = 1)
public abstract class NyxDatabase extends RoomDatabase {

    public abstract SessionDao sessionDao();
    public abstract ReadingDao readingDao();
    public abstract ReadingTypeDao readingTypeDao();
    public abstract SensorDao sensorDao();

    private static volatile NyxDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    private static final int NUMBER_OF_READ_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static final ExecutorService databaseReadExecutor = Executors.newFixedThreadPool(NUMBER_OF_READ_THREADS);


    public static NyxDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NyxDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NyxDatabase.class, "sessions_database")
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);

                            db.execSQL("INSERT INTO Sensor ('sensorName') VALUES ('ACCELEROMETER')");
                            db.execSQL("INSERT INTO ReadingType ('sensorId', 'type') VALUES (1, 'x')");
                            db.execSQL("INSERT INTO ReadingType ('sensorId', 'type') VALUES (1, 'y')");
                            db.execSQL("INSERT INTO ReadingType ('sensorId', 'type') VALUES (1, 'z')");

                            db.execSQL("INSERT INTO Sensor ('sensorName') VALUES ('GYROSCOPE')");
                            db.execSQL("INSERT INTO ReadingType ('sensorId', 'type') VALUES (2, 'x')");
                            db.execSQL("INSERT INTO ReadingType ('sensorId', 'type') VALUES (2, 'y')");
                            db.execSQL("INSERT INTO ReadingType ('sensorId', 'type') VALUES (2, 'z')");

                            db.execSQL("INSERT INTO Sensor ('sensorName') VALUES ('HEARTRATE')");
                            db.execSQL("INSERT INTO ReadingType ('sensorId', 'type') VALUES (3, 'bpm')");

                            db.execSQL("INSERT INTO Sensor ('sensorName') VALUES ('BATTERY')");
                            db.execSQL("INSERT INTO ReadingType ('sensorId', 'type') VALUES (4, 'percent')");
                        }
                    }).build();
                }
            }
        }
        return INSTANCE;
    }


}