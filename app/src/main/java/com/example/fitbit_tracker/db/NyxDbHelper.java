package com.example.fitbit_tracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.fitbit_tracker.db.DatabaseContract.*;

public class NyxDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 18;
    public static final String DATABASE_NAME = "SessionStore.db";

    private static final String SQL_CREATE_TABLE_SESSION = "CREATE TABLE " +
            Session.TABLE_NAME + " (" +
            Session._ID + " INTEGER PRIMARY KEY," +
            Session.UUID + " TEXT," +
            Session.START_TIME + " INTEGER," +
            Session.END_TIME + " INTEGER," +
            Session.DEVICE_MODEL + " TEXT," +
            Session.READINGS_COUNT + " INTEGER)";

    private static final String SQL_CREATE_TABLE_ACCELEROMETER_READING = "CREATE TABLE " +
            AccelerometerReading.TABLE_NAME + " (" +
            AccelerometerReading._ID + " INTEGER PRIMARY KEY," +
            AccelerometerReading.SESSION_ID + " TEXT," +
            AccelerometerReading.TIME_STAMP + " INTEGER," +
            AccelerometerReading.X_ACCELERATION + " REAL," +
            AccelerometerReading.Y_ACCELERATION + " REAL," +
            AccelerometerReading.Z_ACCELERATION + " REAL)";

    private static final String SQL_CREATE_TABLE_HEARTRATE_READING = "CREATE TABLE " +
            HeartRateReading.TABLE_NAME + " (" +
            HeartRateReading._ID + " INTEGER PRIMARY KEY," +
            HeartRateReading.SESSION_ID + " TEXT," +
            HeartRateReading.TIME_STAMP + " INTEGER," +
            HeartRateReading.HEARTRATE + " INTEGER)";

    private static final String SQL_CREATE_TABLE_BATTERY_READING = "CREATE TABLE " +
            BatteryReading.TABLE_NAME + " (" +
            BatteryReading._ID + " INTEGER PRIMARY KEY," +
            BatteryReading.SESSION_ID + " TEXT," +
            BatteryReading.TIME_STAMP + " INTEGER," +
            BatteryReading.BATTERY_LEVEL + " INTEGER)";

    private static final String SQL_CREATE_TABLE_GYROSCOPE_READING = "CREATE TABLE " +
            GyroscopeReading.TABLE_NAME + " (" +
            GyroscopeReading._ID + " INTEGER PRIMARY KEY," +
            GyroscopeReading.SESSION_ID + " TEXT," +
            GyroscopeReading.TIME_STAMP + " INTEGER," +
            GyroscopeReading.X_VELOCITY + " REAL," +
            GyroscopeReading.Y_VELOCITY + " REAL," +
            GyroscopeReading.Z_VELOCITY + " REAL)";

    private static final String SQL_DELETE_TABLE_SESSION = "DROP TABLE IF EXISTS " + Session.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_ACCELEROMETER_READING = "DROP TABLE IF EXISTS " + AccelerometerReading.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_HEARTRATE_READING = "DROP TABLE IF EXISTS " + HeartRateReading.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_BATTERY_READING = "DROP TABLE IF EXISTS " + BatteryReading.TABLE_NAME;
    private static final String SQL_DELETE_TABLE_GYROSCOPE_READING = "DROP TABLE IF EXISTS " + GyroscopeReading.TABLE_NAME;

    public NyxDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_SESSION);
        db.execSQL(SQL_CREATE_TABLE_ACCELEROMETER_READING);
        db.execSQL(SQL_CREATE_TABLE_HEARTRATE_READING);
        db.execSQL(SQL_CREATE_TABLE_BATTERY_READING);
        db.execSQL(SQL_CREATE_TABLE_GYROSCOPE_READING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE_SESSION);
        db.execSQL(SQL_DELETE_TABLE_ACCELEROMETER_READING);
        db.execSQL(SQL_DELETE_TABLE_HEARTRATE_READING);
        db.execSQL(SQL_DELETE_TABLE_BATTERY_READING);
        db.execSQL(SQL_DELETE_TABLE_GYROSCOPE_READING);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

}
