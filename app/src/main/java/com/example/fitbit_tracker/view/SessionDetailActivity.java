package com.example.fitbit_tracker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.adapter.SessionReadingListAdapater;
import com.example.fitbit_tracker.viewmodel.SessionReadingViewModel;

public class SessionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_detail);
        getSupportActionBar().setTitle("Session Details");

        SessionReadingListAdapater sessionReadingListAdapater = new SessionReadingListAdapater(new SessionReadingListAdapater.ReadingDiff(), getBaseContext());

        RecyclerView recyclerView = findViewById(R.id.readingRecyclerView);
        recyclerView.setAdapter(sessionReadingListAdapater);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ViewModelProvider.AndroidViewModelFactory androidViewModelFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, androidViewModelFactory);

        SessionReadingViewModel readingViewModel = viewModelProvider.get(SessionReadingViewModel.class);
        sessionReadingListAdapater.submitList(readingViewModel.getAllReadingBatches());
    }

}
