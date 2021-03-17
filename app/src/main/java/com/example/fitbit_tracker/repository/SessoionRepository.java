package com.example.fitbit_tracker.repository;

import android.app.Application;

import com.example.fitbit_tracker.dao.SessionDao;
import com.example.fitbit_tracker.db.NyxDatabase;
import com.example.fitbit_tracker.model.Session;

import java.util.List;

public class SessoionRepository {

    private SessionDao sessionDao;
    private List<Session> allSessions;

    SessoionRepository(Application application) {
        NyxDatabase db = NyxDatabase.getDatabase(application);
        sessionDao = db.sessionDao();
        allSessions = sessionDao.getAll();
    }

    List<Session> getAllSessions() {
        return allSessions;
    }

    void insert(Session session) {
        NyxDatabase.databaseWriteExecutor.execute(() -> {
            sessionDao.insert(session);
        });
    }
}
