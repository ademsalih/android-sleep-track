package com.example.fitbit_tracker.repository;

import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.utils.RealmLiveData;

import io.realm.Realm;
import io.realm.RealmResults;

public class ReadingRepository {

    private final Realm realm;

    public ReadingRepository() {
        this.realm = Realm.getDefaultInstance();
    }

    public RealmResults<Reading> getReadingForSessionAndSensor(long sessionId, long sensorId) {
        RealmResults<Reading> readings = null;

        try {
            readings = realm.where(Reading.class)
                    .equalTo("sessionId", sessionId)
                    .equalTo("sensorId", sensorId)
                    .findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return readings;
    }

}
