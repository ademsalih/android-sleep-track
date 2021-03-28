package com.example.fitbit_tracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.repository.SessionRepository;
import com.example.fitbit_tracker.utils.RealmLiveData;

import io.realm.Realm;

public class SessionViewModel extends AndroidViewModel {

    private SessionRepository sessionRepository;

    private final RealmLiveData<Session> allSessions;

    public SessionViewModel(Application application) {
        super(application);
        sessionRepository = new SessionRepository(Realm.getDefaultInstance());
        allSessions = sessionRepository.getAllSessions();
    }

    public RealmLiveData<Session> getAllSessions() {
        return allSessions;
    }

    public void insert(Session session) {
        sessionRepository.insert(session);
    }

}
