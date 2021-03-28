package com.example.fitbit_tracker.repository;

import androidx.lifecycle.LiveData;

import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.utils.RealmLiveData;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class SessionRepository {

    private Realm realm;

    public SessionRepository(Realm realm) {
        this.realm = realm;
    }

    public RealmLiveData getAllSessions() {

        RealmResults<Session> results = realm.where(Session.class).sort("endTime", Sort.DESCENDING).findAllAsync();

        return new RealmLiveData(results);
    }

    public void insert(Session session) {
        realm.insert(session);
    }

}
