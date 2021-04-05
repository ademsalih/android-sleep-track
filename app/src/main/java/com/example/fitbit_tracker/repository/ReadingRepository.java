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

    public RealmResults<Reading> getReadingForSessionAndSensor(long sessionId, long sensorId) {
        return realm.where(Reading.class).equalTo("sessionId", sessionId).equalTo("sensorId", sensorId).findAll();
    }

    public void insert(Reading reading) {
        realm.insert(reading);
    }

}
