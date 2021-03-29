package com.example.fitbit_tracker.repository;

import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.utils.RealmLiveData;

import io.realm.Realm;
import io.realm.RealmResults;

public class ReadingRepository {

    private Realm realm;

    public ReadingRepository(Realm realm) {
        this.realm = realm;
    }

    public RealmResults<Reading> getReadingForSessionAndSensor(int sessionId, int sensorId) {
        return realm.where(Reading.class).equalTo("session.sessionId", sessionId).equalTo("sensor.sensorId", sensorId).findAll();
    }

    public void insert(Reading reading) {
        realm.insert(reading);
    }

}
