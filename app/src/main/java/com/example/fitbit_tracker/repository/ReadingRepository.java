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

    public RealmLiveData<Reading> getDistinctReadings(long sessionId) {
        RealmResults<Reading> results = realm.where(Reading.class).equalTo("sessionId", sessionId).distinct("sensorName").findAllAsync();
        return new RealmLiveData(results);
    }

    public RealmResults<Reading> getReadingForSessionAndSensor(long sessionId, String sensorName) {
        return realm.where(Reading.class).equalTo("sessionId", sessionId).equalTo("sensorName", sensorName).findAll();
    }

    public void insert(Reading reading) {
        realm.insert(reading);
    }

}
