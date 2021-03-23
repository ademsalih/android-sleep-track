package com.example.fitbit_tracker.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitbit_tracker.dao.SessionDao;
import com.example.fitbit_tracker.database.NyxDatabase;
import com.example.fitbit_tracker.model.Session;

import java.util.List;

public class SessionRepository {

    private SessionDao sessionDao;
    private LiveData<List<Session>> allSessions;

    public SessionRepository(Application application) {
        NyxDatabase db = NyxDatabase.getDatabase(application);
        sessionDao = db.sessionDao();
        allSessions = sessionDao.getAll();
    }

    public LiveData<List<Session>> getAllSessions() {
        return allSessions;
    }

    public void insert(Session session) {
        NyxDatabase.databaseWriteExecutor.execute(() -> {
            sessionDao.insert(session);
        });
    }

}
