package com.example.fitbit_tracker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.adapter.reading.ReadingListAdapter;
import com.example.fitbit_tracker.domain_model.ReadingBatchDM;
import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.viewmodel.ReadingBatchDMViewModel;
import com.example.fitbit_tracker.viewmodel.SessionViewModel;

import java.util.List;

public class SessionDetailActivity extends AppCompatActivity {

    private ReadingBatchDMViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_detail);
        getSupportActionBar().setTitle("Session Details");

        Bundle b = getIntent().getExtras();
        long sessionId = b.getLong("sessionId");

        ReadingListAdapter sessionReadingListAdapater = new ReadingListAdapter(new ReadingListAdapter.ReadingDiff(), getBaseContext());

        RecyclerView recyclerView = findViewById(R.id.readingRecyclerView);
        recyclerView.setAdapter(sessionReadingListAdapater);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ViewModelProvider.AndroidViewModelFactory androidViewModelFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, androidViewModelFactory);


        viewModel = viewModelProvider.get(ReadingBatchDMViewModel.class);
        viewModel.getAllReadingDomainModels(sessionId).observe(this, new Observer<List<ReadingBatchDM>>() {
            @Override
            public void onChanged(List<ReadingBatchDM> readingBatchDMS) {
                sessionReadingListAdapater.submitList(readingBatchDMS);
            }
        });

    }

}
