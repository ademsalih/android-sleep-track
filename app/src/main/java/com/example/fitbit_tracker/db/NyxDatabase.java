package com.example.fitbit_tracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void createSession(String uuid, String startTime, String endTime, int numberOfReadings) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseContract.Session.UUID, uuid);
        cv.put(DatabaseContract.Session.END_TIME, startTime);
        cv.put(DatabaseContract.Session.START_TIME, endTime);
        cv.put(DatabaseContract.Session.NUMBER_OF_READINGS, numberOfReadings);
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
        cv.put(DatabaseContract.Session.NUMBER_OF_READINGS, readingCount);

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
                DatabaseContract.Session.NUMBER_OF_READINGS
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
            int numberOfReadings = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Session.NUMBER_OF_READINGS));

            Session session = new Session();
            session.set_id(id);
            session.setUuid(UUID);
            session.setStartTime(startTime);
            session.setEndTime(endTime);
            session.setNumberOfReadings(numberOfReadings);

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


}
