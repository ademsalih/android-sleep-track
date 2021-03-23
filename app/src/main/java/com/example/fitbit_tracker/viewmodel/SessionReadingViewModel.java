package com.example.fitbit_tracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitbit_tracker.model.ReadingBatch;
import com.example.fitbit_tracker.repository.ReadingRepository;

import java.util.List;

public class SessionReadingViewModel extends AndroidViewModel {

    private ReadingRepository readingRepository;
    private final List<ReadingBatch> allReadingBatches;

    public SessionReadingViewModel(Application application) {
        super(application);
        readingRepository = new ReadingRepository(application);
        allReadingBatches = readingRepository.getAllSessionReadings();
    }

    public List<ReadingBatch> getAllReadingBatches() {
        return allReadingBatches;
    }

}
