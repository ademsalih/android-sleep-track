package com.example.fitbit_tracker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.adapter.listadapter.SessionListAdapter;
import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.viewmodel.SessionViewModel;

import java.util.List;

public class SessionsActivity extends AppCompatActivity {

    private SessionViewModel sessionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions);

        SessionListAdapter sessionListAdapter = new SessionListAdapter(new SessionListAdapter.SessionDiff(), getBaseContext());

        RecyclerView recyclerView = findViewById(R.id.sessionRecyclerView);
        recyclerView.setAdapter(sessionListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ViewModelProvider.AndroidViewModelFactory androidViewModelFactory = new ViewModelProvider.AndroidViewModelFactory(getApplication());
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, androidViewModelFactory);

        sessionViewModel = viewModelProvider.get(SessionViewModel.class);
        sessionViewModel.getAllSessions().observe(this, new Observer<List<Session>>() {
            @Override
            public void onChanged(List<Session> sessions) {

                sessionListAdapter.submitList(sessions);
                getSupportActionBar().setTitle("Sessions (" + sessions.size() + ")");
            }
        });
    }

}