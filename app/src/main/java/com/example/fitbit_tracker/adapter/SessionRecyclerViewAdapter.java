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

    public String durationPrint(long ms) {
        long seconds = ms/1000;

        if (seconds < 60) {
            return seconds + " seconds";
        } else if (seconds < 3600) {
            int minute = (int) seconds / 60;
            int remainderSeconds = (int) seconds % 60;
            String duration = String.valueOf(minute);

            if (minute > 1) {
                duration += " minutes";
            } else {
                duration += " minute";
            }

            if (remainderSeconds > 1) {
                duration += ", " + remainderSeconds + " seconds";
            } else if (remainderSeconds > 0) {
                duration += ", " + remainderSeconds + " second";
            }

            return duration;
        } else {
            int hour = (int) seconds/3600;
            int remainderSeconds = (int) seconds % 3600;
            int remainderMinutes = (int) remainderSeconds / 60;
            String duration = String.valueOf(hour);

            if (hour > 1 ) {
                duration += " hours";
            } else {
                duration += " hour";
            }

            if (remainderMinutes > 1) {
                duration += ", " + remainderMinutes + " minutes";
            } else if (remainderMinutes > 0) {
                duration += ", " + remainderMinutes + " minute";
            }

            return duration;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SessionsViewHolder holder, final int position) {
        final Session session = sessions.get(position);

        Date time = new Date(session.getEndTime());

        holder.sessionTimeTextView.setText("5 minutes ago");
        holder.readingCountTextView.setText(session.getReadingsCount() + " readings");

        long duration = session.getEndTime() - session.getStartTime();

        holder.sessionDurationTextView.setText(durationPrint(30000_000));

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
        TextView sessionTimeTextView;
        TextView readingCountTextView;
        TextView sessionDurationTextView;
        ConstraintLayout sessionContainer;

        SessionsViewHolder(View view) {
            super(view);
            sessionTimeTextView = view.findViewById(R.id.sessionTimeTextView);
            readingCountTextView = view.findViewById(R.id.readingCountTextView);
            sessionDurationTextView = view.findViewById(R.id.sessionDurationTextView);
            sessionContainer = (ConstraintLayout) view;
        }

    }
}
