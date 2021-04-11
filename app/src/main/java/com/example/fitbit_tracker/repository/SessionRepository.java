package com.example.fitbit_tracker.repository;

import androidx.lifecycle.LiveData;

import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.utils.RealmLiveData;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class SessionRepository {

    private final Realm realm;

    public SessionRepository() {
        this.realm = Realm.getDefaultInstance();
    }

    public RealmLiveData getAllSessions() {
        RealmResults<Session> results = null;

        try {
            results = realm.where(Session.class)
                    .sort("endTime", Sort.DESCENDING)
                    .findAllAsync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            realm.close();
        }

        return new RealmLiveData(results);
    }

    public void insert(Session session) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm r) {
                    r.insert(session);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            realm.close();
        }
    }

}
