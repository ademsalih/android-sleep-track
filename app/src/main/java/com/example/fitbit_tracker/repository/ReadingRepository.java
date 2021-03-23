package com.example.fitbit_tracker.repository;

import android.app.Application;

import com.example.fitbit_tracker.dao.ReadingDao;
import com.example.fitbit_tracker.database.NyxDatabase;

import java.util.List;

public class ReadingRepository {

    private ReadingDao readingDao;

    public ReadingRepository(Application application) {
        NyxDatabase db = NyxDatabase.getDatabase(application);
        readingDao = db.readingDao();
    }

    public List<ReadingBatch> getAllSessionReadings() {

        return null;
    }

}
