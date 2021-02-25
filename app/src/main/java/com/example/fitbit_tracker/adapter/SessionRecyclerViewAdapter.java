package com.example.fitbit_tracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.db.NyxDatabase;
import com.example.fitbit_tracker.model.HeartrateReading;
import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.views.SessionDetailsActivity;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SessionRecyclerViewAdapter extends RecyclerView.Adapter<SessionRecyclerViewAdapter.SessionsViewHolder> {
    private final List<Session> sessions;
    protected Context context;

    public SessionRecyclerViewAdapter(Context context, List<Session> sessions) {
        this.context = context;
        this.sessions = sessions;
    }

    @NonNull
    @Override
    public SessionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item, parent, false);
        return new SessionsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionsViewHolder holder, final int position) {
        final Session session = sessions.get(position);
        holder.sessionUUIDTextView.setText(session.getUuid());

        Date time = new Date(session.getEndTime());

        holder.sessionTimeTextView.setText(time.toString());
        holder.readingCountTextView.setText(session.getNumberOfReadings() + " readings");

        long duration = session.getEndTime() - session.getStartTime();

        holder.sessionDurationTextView.setText("Duration: " + duration/1000 + " s");

        holder.sessionContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SessionDetailsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("SESSION_UUID", session.getUuid());

                intent.putExtras(bundle);

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    class SessionsViewHolder extends RecyclerView.ViewHolder {
        TextView sessionUUIDTextView;
        TextView sessionTimeTextView;
        TextView readingCountTextView;
        TextView sessionDurationTextView;
        ConstraintLayout sessionContainer;

        SessionsViewHolder(View view) {
            super(view);
            sessionUUIDTextView = view.findViewById(R.id.sessionUUIDTextView);
            sessionTimeTextView = view.findViewById(R.id.sessionTimeTextView);
            readingCountTextView = view.findViewById(R.id.readingCountTextView);
            sessionDurationTextView = view.findViewById(R.id.sessionDurationTextView);
            sessionContainer = (ConstraintLayout) view;
        }
    }
}
