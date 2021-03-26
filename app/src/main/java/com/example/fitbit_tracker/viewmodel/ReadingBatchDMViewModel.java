package com.example.fitbit_tracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitbit_tracker.domain_model.Batch;
import com.example.fitbit_tracker.repository.ReadingRepository;

import java.util.List;

public class ReadingBatchDMViewModel extends AndroidViewModel {

    private ReadingRepository readingRepository;

    public ReadingBatchDMViewModel(Application application) {
        super(application);
        readingRepository = new ReadingRepository(application);
    }

    public LiveData<List<Batch>> getAllReadingDomainModels(long sessionId) {
        return readingRepository.getReadings(sessionId);
    }

}
