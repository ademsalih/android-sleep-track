package com.example.fitbit_tracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.repository.ReadingRepository;

import java.util.List;

public class SessionReadingViewModel extends AndroidViewModel {

    private ReadingRepository readingRepository;
    private final List<Reading> allReading;

    public SessionReadingViewModel(Application application) {
        super(application);
        readingRepository = new ReadingRepository(application);
        allReading = null;
    }

    public List<Reading> getAllReadingBatches() {
        return allReading;
    }

}
