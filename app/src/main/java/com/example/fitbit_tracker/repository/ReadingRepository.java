package com.example.fitbit_tracker.repository;

import android.app.Application;

import com.example.fitbit_tracker.dao.ReadingBaseDao;
import com.example.fitbit_tracker.dao.ReadingDao;
import com.example.fitbit_tracker.database.Configuration;
import com.example.fitbit_tracker.database.NyxDatabase;
import com.example.fitbit_tracker.model.ReadingBatch;
import com.example.fitbit_tracker.model.reading.AccelerometerReading;
import com.example.fitbit_tracker.model.reading.Reading;

import java.util.ArrayList;
import java.util.List;

public class ReadingRepository {

    private ReadingDao readingDao;
    private ReadingBaseDao<AccelerometerReading> readingBaseDao;
    private List<ReadingBatch> readingBatches;

    private AccelerometerRepository accelerometerRepository;

    public ReadingRepository(Application application) {
        NyxDatabase db = NyxDatabase.getDatabase(application);
        readingDao = db.readingDao();
        readingBatches = new ArrayList<>();

        for (Class<? extends Reading> readingClass : Configuration.READING_CLASSES) {

            String a = NyxDatabase.getTableName(readingClass);

        }

        accelerometerRepository.findAll();
    }

    public List<ReadingBatch> getAllSessionReadings() {

        // These elements should be automatically created based on the Configuration
        // The name should be the same as the model name minus Reading and the readings
        // data should be fetched using the findAll() generic method.

        /*List<List<? extends Reading>> nall = new ArrayList<>();

        List<AccelerometerReading> all = accelerometerRepository.findAll();

        nall.add(all);

        readingBatches.add(new ReadingBatch("Accelerometer", nall));*/

        return readingBatches;
    }

}
