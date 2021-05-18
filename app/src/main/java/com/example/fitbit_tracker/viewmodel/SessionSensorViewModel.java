package com.example.fitbit_tracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitbit_tracker.model.SessionSensor;
import com.example.fitbit_tracker.repository.SessionSensorRepository;
import com.example.fitbit_tracker.utils.RealmLiveData;

public class SessionSensorViewModel extends AndroidViewModel {

    private SessionSensorRepository sessionSensorRepository;

    public SessionSensorViewModel(Application application) {
        super(application);
        sessionSensorRepository = new SessionSensorRepository();
    }

    public RealmLiveData<SessionSensor> getAllSessionSensors(long sessionId) {
        return sessionSensorRepository.getSessionSensors(sessionId);
    }

}
