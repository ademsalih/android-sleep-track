package com.example.fitbit_tracker.repository;

import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.model.Sensor;
import com.example.fitbit_tracker.model.SessionSensor;
import com.example.fitbit_tracker.utils.RealmLiveData;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SessionSensorRepository {

    private final Realm realm;

    public SessionSensorRepository() {
        this.realm = Realm.getDefaultInstance();
    }

    public RealmLiveData<SessionSensor> getSessionSensors(long sessionId) {
        RealmResults<SessionSensor> realmResults = realm.where(SessionSensor.class).equalTo("sessionId", sessionId).findAllAsync();
        return new RealmLiveData<>(realmResults);
    }

    public List<SessionSensor> getSessionSensorsAsList(long sessionId) {
        List<SessionSensor> realmResults = realm.where(SessionSensor.class).equalTo("sessionId", sessionId).findAll();
        return realmResults;
    }

}
