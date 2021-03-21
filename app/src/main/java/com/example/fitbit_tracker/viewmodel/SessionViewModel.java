package com.example.fitbit_tracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.repository.SessionRepository;

import java.util.List;

public class SessionViewModel extends AndroidViewModel {

    private SessionRepository sessionRepository;

    private final LiveData<List<Session>> allSessions;

    public SessionViewModel(Application application) {
        super(application);
        sessionRepository = new SessionRepository(application);
        allSessions = sessionRepository.getAllSessions();
    }

    public LiveData<List<Session>> getAllSessions() {
        return allSessions;
    }

    public void insert(Session session) {
        sessionRepository.insert(session);
    }

}
