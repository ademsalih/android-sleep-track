package com.example.fitbit_tracker.wsserver;

import com.example.fitbit_tracker.model.Reading;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public interface NyxSessionStore {

    void initSession(String deviceModel, String sessionIdentifier, HashMap<String, Float> sensorConfigs);

    void startSession(String sessionIdentifier, long startTime);

    void stopSession(String sessionIdentifier, long endTime, int readingsCount);

    void addReading(String sessionIdentifier, String sensorIdentifier, List<Reading> readings);

}
