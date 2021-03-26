package com.example.fitbit_tracker.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.SkipQueryVerification;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.fitbit_tracker.database.NyxDatabase;
import com.example.fitbit_tracker.model.reading.AccelerometerReading;
import com.example.fitbit_tracker.model.reading.Reading;

import java.util.List;


public interface ReadingBaseDao<T extends Reading>  {

    @Insert
    void insert(T object);

    @Delete
    void delete(T object);

    @Update
    void update(T object);

    @RawQuery
    List<? extends Reading> findAll(SupportSQLiteQuery query);

}
