package com.example.fitbit_tracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.views.SessionDetailsActivity;

import java.util.List;

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
        holder.sessionTimeTextView.setText("Time: " + session.getEndTime());
        holder.readingCountTextView.setText(session.getNumberOfReadings() + " readings");

        long duration = session.getEndTime() - session.getStartTime();
        holder.sessionDurationTextView.setText("Duration: " + duration + " ms");
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    class SessionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView sessionUUIDTextView;
        TextView sessionTimeTextView;
        TextView readingCountTextView;
        TextView sessionDurationTextView;

        SessionsViewHolder(View view) {
            super(view);
            sessionUUIDTextView = view.findViewById(R.id.sessionUUIDTextView);
            sessionTimeTextView = view.findViewById(R.id.sessionTimeTextView);
            readingCountTextView = view.findViewById(R.id.readingCountTextView);
            sessionDurationTextView = view.findViewById(R.id.sessionDurationTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, SessionDetailsActivity.class);
            context.startActivity(intent);
        }
    }
}
