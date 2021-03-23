package com.example.fitbit_tracker.repository;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.fitbit_tracker.dao.ReadingBaseDao;
import com.example.fitbit_tracker.database.NyxDatabase;
import com.example.fitbit_tracker.model.reading.Reading;

import java.util.List;

public abstract class BaseRepository<T> {

    ReadingBaseDao<? extends Reading> readingBaseDao;

    List<? extends Reading> findAll() {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM " + NyxDatabase.getTableName(this.getClass()));
        return readingBaseDao.findAll(query);
    }
}
