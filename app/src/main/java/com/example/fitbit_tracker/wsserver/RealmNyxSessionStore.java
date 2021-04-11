package com.example.fitbit_tracker.wsserver;

import android.util.Log;

import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.model.Sensor;
import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.model.SessionSensor;
import com.example.fitbit_tracker.model.User;

import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.realm.Realm;

public class RealmNyxSessionStore implements NyxSessionStore {

    private final String TAG = getClass().getSimpleName();
    private LinkedList<Reading> queue;
    private final int BATCH_INSERT_THRESHOLD = 1000;

    public RealmNyxSessionStore() {
        this.queue = new LinkedList<>();
    }

    @Override
    public void initSession(String deviceModel, String sessionIdentifier, HashMap<String, Float> sensorConfigs) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = realm.where(User.class).findFirst();

                Number sessionMaxId = realm.where(Session.class).max("sessionId");
                long sessionNextId = (sessionMaxId == null) ? 1 : sessionMaxId.longValue() + 1;

                Log.d("LOG", "Session id next: " + sessionNextId);

                Session session = new Session();
                session.setSessionId(sessionNextId);
                session.setEndTime(0);
                session.setStartTime(0);
                session.setReadingsCount(0);
                session.setUuid(sessionIdentifier);
                session.setDeviceModel(deviceModel);
                session.setUserId(user.getId());
                realm.insert(session);

                Number sensorMaxId = realm.where(Sensor.class).max("sensorId");
                long sensorNextId = (sensorMaxId == null) ? 1 : sensorMaxId.longValue() + 1;

                Number sessionSensorMaxId = realm.where(SessionSensor.class).max("id");
                long sessionSensorNextId = (sessionSensorMaxId == null) ? 1 : sessionSensorMaxId.longValue() + 1;


                List<String> keyList = new ArrayList<>(sensorConfigs.keySet());

                for (int i = 0; i < keyList.size(); i++) {
                    Sensor sensor = realm.where(Sensor.class).equalTo("sensorName", keyList.get(i)).findFirst();
                    if (sensor == null) {
                        Log.d("LOG", keyList.get(i) + " sensor id next: " + sensorNextId + i);

                        sensor = new Sensor();
                        sensor.setSensorId(sensorNextId+i);
                        sensor.setSensorName(keyList.get(i));
                        realm.insert(sensor);
                    }

                    SessionSensor sessionSensor = new SessionSensor();
                    sessionSensor.setId(sessionSensorNextId+i);
                    sessionSensor.setFrequency(sensorConfigs.get(keyList.get(i)));
                    sessionSensor.setSensorId(sensor.getSensorId());
                    sessionSensor.setSessionId(session.getSessionId());
                    realm.insert(sessionSensor);
                }
            }
        });
    }

    @Override
    public void startSession(String sessionIdentifier, long startTime) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Session session = realm.where(Session.class).equalTo("uuid", sessionIdentifier).findFirst();
                session.setStartTime(startTime);
            }
        });
        realm.close();
    }

    @Override
    public void stopSession(String sessionIdentifier, long endTime, int readingsCount) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Session session = realm.where(Session.class).equalTo("uuid", sessionIdentifier).findFirst();
                session.setEndTime(endTime);
                session.setReadingsCount(readingsCount);

                Log.d(TAG, "Ending Session with queue size: " + queue.size());

                if (!queue.isEmpty()) {
                    realm.insert(queue.subList(0, queue.size()-1));
                    queue.clear();
                    Log.d(TAG, "Inserted remaining, queue size: " + queue.size());
                }
            }
        });


    }

    @Override
    public void addReading(String sessionIdentifier, String sensorIdentifier, List<Reading> readings) {
        Realm realm = Realm.getDefaultInstance();
        long sessionId = realm.where(Session.class).equalTo("uuid", sessionIdentifier).findFirst().getSessionId();
        long sensorId = realm.where(Sensor.class).equalTo("sensorName",sensorIdentifier).findFirst().getSensorId();

        for (Reading r : readings) {
            r.setSensorId(sensorId);
            r.setSessionId(sessionId);
            queue.add(r);
        }

        if (queue.size() > BATCH_INSERT_THRESHOLD) {

            Log.d(TAG, "Queue size: " + queue.size());

            List<Reading> toInsert = new ArrayList<>();

            for (int i = 0; i < BATCH_INSERT_THRESHOLD; i++) {
                toInsert.add(queue.poll());
            }

            long start = System.currentTimeMillis();

            try {
                realm.executeTransactionAsync(r ->
                        r.insert(toInsert)
                );
            } catch (Exception e) {
                Log.d(TAG, "Crash during insert");
                e.printStackTrace();
            } finally {
                realm.close();
            }
            long time = System.currentTimeMillis() - start;
        }
        realm.close();
    }

}
