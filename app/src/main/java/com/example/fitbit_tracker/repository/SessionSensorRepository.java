package com.example.fitbit_tracker.repository;

import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.model.Sensor;
import com.example.fitbit_tracker.model.SessionSensor;
import com.example.fitbit_tracker.utils.RealmLiveData;

import io.realm.Realm;
import io.realm.RealmResults;

public class SessionSensorRepository {

    private Realm realm;

    public SessionSensorRepository(Realm realm) {
        this.realm = realm;
    }

    public RealmLiveData<SessionSensor> getSessionSensors(long sessionId) {
        RealmResults<SessionSensor> realmResults = realm.where(SessionSensor.class).equalTo("session.sessionId", sessionId).findAllAsync();
        return new RealmLiveData<>(realmResults);
    }

}
