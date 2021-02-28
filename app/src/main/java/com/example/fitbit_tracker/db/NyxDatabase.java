package com.example.fitbit_tracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.fitbit_tracker.model.AccelerometerReading;
import com.example.fitbit_tracker.model.HeartrateReading;
import com.example.fitbit_tracker.model.Session;

import java.util.ArrayList;
import java.util.List;

public class NyxDatabase {

    private SQLiteOpenHelper dbHelper;
    private Context context;
    private SQLiteDatabase writableDb;
    private SQLiteDatabase readableDb;

    public NyxDatabase(Context context) {
        this.context = context;
        initReadableAndWritableDb();
    }

    private void initReadableAndWritableDb() {
        this.dbHelper = new NyxDbHelper(this.context);
        this.readableDb = this.dbHelper.getReadableDatabase();
        this.writableDb = this.dbHelper.getWritableDatabase();
    }

    public void createSession(String uuid, String startTime, String endTime, String deviceModel, int readingsCount) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.Session.UUID, uuid);
        cv.put(DatabaseContract.Session.END_TIME, startTime);
        cv.put(DatabaseContract.Session.START_TIME, endTime);
        cv.put(DatabaseContract.Session.DEVICE_MODEL, deviceModel);
        cv.put(DatabaseContract.Session.READINGS_COUNT, readingsCount);
        writableDb.insert(DatabaseContract.Session.TABLE_NAME, null, cv);
    }

    public int updateSessionStartTime(String uuid, long startTime) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.Session.START_TIME, startTime);

        String selection = DatabaseContract.Session.UUID + " LIKE ?";
        String[] selectionArgs = { uuid };

        int count = writableDb.update(
                DatabaseContract.Session.TABLE_NAME,
                cv,
                selection,
                selectionArgs
        );

        return count;
    }

    public int updateSessionEndTime(String uuid, long endTime, int readingCount) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.Session.END_TIME, endTime);
        cv.put(DatabaseContract.Session.READINGS_COUNT, readingCount);

        String selection = DatabaseContract.Session.UUID + " LIKE ?";
        String[] selectionArgs = { uuid };

        int count = writableDb.update(
                DatabaseContract.Session.TABLE_NAME,
                cv,
                selection,
                selectionArgs
        );

        return count;
    }

    public List<Session> getAllSessions() {
        List<Session> sessions = new ArrayList<>();

        String[] projection = {
                DatabaseContract.Session._ID,
                DatabaseContract.Session.UUID,
                DatabaseContract.Session.START_TIME,
                DatabaseContract.Session.END_TIME,
                DatabaseContract.Session.READINGS_COUNT
        };

        String sortOrder = DatabaseContract.Session.START_TIME + " ASC";

        Cursor cursor = readableDb.query(
                DatabaseContract.Session.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while(cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.Session._ID));
            String UUID = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Session.UUID));
            long startTime = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.Session.START_TIME));
            long endTime = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.Session.END_TIME));
            int numberOfReadings = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Session.READINGS_COUNT));

            Session session = new Session();
            session.set_id(id);
            session.setUuid(UUID);
            session.setStartTime(startTime);
            session.setEndTime(endTime);
            session.setReadingsCount(numberOfReadings);

            sessions.add(session);
        }
        cursor.close();

        return sessions;
    }

    public void addHeartrateReading(String sessionIdentifier, String timeStamp, int heartrate) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.HeartRateReading.SESSION_ID, sessionIdentifier);
        cv.put(DatabaseContract.HeartRateReading.TIME_STAMP, timeStamp);
        cv.put(DatabaseContract.HeartRateReading.HEARTRATE, heartrate);
        writableDb.insert(DatabaseContract.HeartRateReading.TABLE_NAME,null, cv);
    }

    public void addAccelerometerReading(String sessionIdentifier, String timeStamp, double x, double y, double z) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.AccelerometerReading.SESSION_ID, sessionIdentifier);
        cv.put(DatabaseContract.AccelerometerReading.TIME_STAMP, timeStamp);
        cv.put(DatabaseContract.AccelerometerReading.X_ACCELERATION, x);
        cv.put(DatabaseContract.AccelerometerReading.Y_ACCELERATION, y);
        cv.put(DatabaseContract.AccelerometerReading.Z_ACCELERATION, z);
        writableDb.insert(DatabaseContract.AccelerometerReading.TABLE_NAME,null, cv);
    }

    public List<HeartrateReading> getAllHeartrates(String uuid) {
        List<HeartrateReading> heartrateReadings = new ArrayList<>();

        String[] projection = {
                DatabaseContract.HeartRateReading._ID,
                DatabaseContract.HeartRateReading.SESSION_ID,
                DatabaseContract.HeartRateReading.TIME_STAMP,
                DatabaseContract.HeartRateReading.HEARTRATE
        };

        String sortOrder = DatabaseContract.HeartRateReading.TIME_STAMP + " ASC";

        String selection = DatabaseContract.HeartRateReading.SESSION_ID + " LIKE ?";
        String[] selectionArgs = { uuid };

        Cursor cursor = readableDb.query(
                DatabaseContract.HeartRateReading.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        while(cursor.moveToNext()) {
            long timeStamp = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.HeartRateReading.TIME_STAMP));
            int heartrate = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.HeartRateReading.HEARTRATE));

            HeartrateReading heartrateReading = new HeartrateReading();
            heartrateReading.setTimeStamp(timeStamp);
            heartrateReading.setHeartRate(heartrate);

            heartrateReadings.add(heartrateReading);
        }
        cursor.close();

        return heartrateReadings;
    }

    public List<AccelerometerReading> getAllAccelerometerReadings(String uuid) {
        List<AccelerometerReading> accelerometerReadings = new ArrayList<>();

        String[] projection = {
                DatabaseContract.AccelerometerReading._ID,
                DatabaseContract.AccelerometerReading.SESSION_ID,
                DatabaseContract.AccelerometerReading.TIME_STAMP,
                DatabaseContract.AccelerometerReading.X_ACCELERATION,
                DatabaseContract.AccelerometerReading.Y_ACCELERATION,
                DatabaseContract.AccelerometerReading.Z_ACCELERATION,
        };

        String sortOrder = DatabaseContract.AccelerometerReading.TIME_STAMP + " ASC";

        String selection = DatabaseContract.AccelerometerReading.SESSION_ID + " LIKE ?";
        String[] selectionArgs = { uuid };

        Cursor cursor = readableDb.query(
                DatabaseContract.AccelerometerReading.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        while(cursor.moveToNext()) {
            long timeStamp = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.AccelerometerReading.TIME_STAMP));
            double x = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.AccelerometerReading.X_ACCELERATION));
            double y = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.AccelerometerReading.Y_ACCELERATION));
            double z = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.AccelerometerReading.Z_ACCELERATION));

            AccelerometerReading accelerometerReading = new AccelerometerReading();
            accelerometerReading.setTimeStamp(timeStamp);
            accelerometerReading.setX(x);
            accelerometerReading.setY(y);
            accelerometerReading.setZ(z);

            accelerometerReadings.add(accelerometerReading);
        }
        cursor.close();

        return accelerometerReadings;
    }


}
