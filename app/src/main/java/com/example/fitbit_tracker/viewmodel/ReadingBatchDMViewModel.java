package com.example.fitbit_tracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitbit_tracker.domain_model.ReadingBatchDM;
import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.repository.ReadingRepository;

import java.util.List;

public class ReadingBatchDMViewModel extends AndroidViewModel {

    private ReadingRepository readingRepository;

    public ReadingBatchDMViewModel(Application application) {
        super(application);
        readingRepository = new ReadingRepository(application);
    }

    public LiveData<List<ReadingBatchDM>> getAllReadingDomainModels(long sessionId) {
        return readingRepository.getReadings(sessionId);
    }

}
