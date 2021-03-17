package com.example.fitbit_tracker.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fitbit_tracker.dao.SessionDao;
import com.example.fitbit_tracker.model.Session;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Session.class}, version = 1)
public abstract class NyxDatabase extends RoomDatabase {

    public abstract SessionDao sessionDao();

    private static volatile NyxDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static NyxDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NyxDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NyxDatabase.class, "session_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}