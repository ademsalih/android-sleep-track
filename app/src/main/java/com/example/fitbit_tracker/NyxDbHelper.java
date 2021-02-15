package com.example.fitbit_tracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.fitbit_tracker.DatabaseContract.Session;

public class NyxDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SessionStore.db";

    private static final String SQL_CREATE_TABLE_SESSION =
            "CREATE TABLE " + Session.TABLE_NAME + " (" +
                    Session._ID + " INTEGER PRIMARY KEY," +
                    Session.START_TIME + " TEXT," +
                    Session.END_TIME + " TEXT)";

    private static final String SQL_CREATE_TABLE_READING =
            "CREATE TABLE " + Session.TABLE_NAME + " (" +
                    Session._ID + " INTEGER PRIMARY KEY," +
                    Session.START_TIME + " TEXT," +
                    Session.END_TIME + " TEXT)";

    private static final String SQL_DELETE_TABLE_SESSION = "DROP TABLE IF EXISTS " + Session.TABLE_NAME;

    public NyxDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_SESSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE_SESSION);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
