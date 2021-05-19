package com.example.fitbit_tracker.repository;

import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.utils.RealmLiveData;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

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
                    .equalTo("sensorId", sensorId).sort("timeStamp", Sort.ASCENDING)
                    .findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return readings;
    }

    public RealmResults<Reading> getReadingForSession(long sessionId) {
        RealmResults<Reading> readings = null;

        try {
            readings = realm.where(Reading.class)
                    .equalTo("sessionId", sessionId)
                    .sort("timeStamp", Sort.ASCENDING)
                    .findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return readings;
    }

}
