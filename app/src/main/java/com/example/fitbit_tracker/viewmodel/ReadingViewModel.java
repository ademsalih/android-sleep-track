package com.example.fitbit_tracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.repository.ReadingRepository;
import com.example.fitbit_tracker.utils.RealmLiveData;

import io.realm.Realm;

public class ReadingViewModel extends AndroidViewModel {

    private ReadingRepository readingRepository;

    public ReadingViewModel(Application application) {
        super(application);
        readingRepository = new ReadingRepository(Realm.getDefaultInstance());
    }

    public RealmLiveData<Reading> getAllReadings(long sessionId) {
        return readingRepository.getDistinctReadings(sessionId);
    }

}
