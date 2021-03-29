package com.example.fitbit_tracker.wsserver;

import com.example.fitbit_tracker.model.Session;

import io.realm.Realm;

public class RealmNyxSessionStore implements NyxSessionStore {

    @Override
    public void initSession() {

    }

    @Override
    public void startSession(String sessionIdentifier, long startTime) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Session session = realm.where(Session.class).equalTo("uuid", sessionIdentifier).findFirst();
                session.setStartTime(startTime);
            }
        });
    }

    @Override
    public void stopSession(String sessionIdentifier, long endTime, int readingsCount) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Session session = realm.where(Session.class).equalTo("uuid", sessionIdentifier).findFirst();
                session.setEndTime(endTime);
                session.setReadingsCount(readingsCount);
            }
        });
    }

    @Override
    public void addReading() {

    }

    @Override
    public void addBatchReading() {

    }

}
