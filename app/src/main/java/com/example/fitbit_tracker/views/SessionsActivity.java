package com.example.fitbit_tracker.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.SessionRecyclerViewAdapter;
import com.example.fitbit_tracker.db.NyxDatabase;
import com.example.fitbit_tracker.model.Session;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class SessionsActivity extends AppCompatActivity implements LifecycleOwner {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions);

        List<Session> sessions = new NyxDatabase(this).getAllSessions();

        RecyclerView recyclerView = findViewById(R.id.sessionsRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SessionRecyclerViewAdapter(this, sessions));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


    }

}