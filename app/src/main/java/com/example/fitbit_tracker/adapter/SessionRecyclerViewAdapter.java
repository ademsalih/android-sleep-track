package com.example.fitbit_tracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitbit_tracker.R;
import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.view.SessionDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.fitbit_tracker.utils.TimeUtils.*;

public class SessionRecyclerViewAdapter extends RecyclerView.Adapter<SessionRecyclerViewAdapter.SessionViewHolder> {
    private final List<Session> sessions;
    protected Context context;

    public SessionRecyclerViewAdapter(Context context, List<Session> sessions) {
        this.context = context;
        this.sessions = sessions;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item_2, parent, false);
        return new SessionViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, final int position) {
        Session session = sessions.get(position);

        Date sessionStartDate = new Date(session.getStartTime());
        Date sessionEndDate = new Date(session.getEndTime());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        String sessionStartFormatted = simpleDateFormat.format(sessionStartDate);
        String sessionEndFormatted = simpleDateFormat.format(sessionEndDate);

        holder.sessionStartEndTextView.setText(sessionStartFormatted + " – " + sessionEndFormatted);

        long timeDelta = System.currentTimeMillis() - session.getEndTime();
        holder.timeDeltaTextView.setText(timeDeltaLabel(timeDelta));

        holder.readingCountTextView.setText(session.getReadingsCount() + " readings");

        long duration = session.getEndTime() - session.getStartTime();

        holder.sessionDurationTextView.setText(formattedTimeLabel(duration));

        holder.deviceModelTextView.setText("Fitbit " + session.getDeviceModel());

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

    class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView sessionStartEndTextView;
        TextView timeDeltaTextView;
        TextView sessionDurationTextView;
        TextView deviceModelTextView;
        TextView readingCountTextView;

        ConstraintLayout sessionContainer;

        SessionViewHolder(View view) {
            super(view);
            sessionStartEndTextView = view.findViewById(R.id.sessionStartEndTextView);
            timeDeltaTextView = view.findViewById(R.id.timeDeltaTextView);
            sessionDurationTextView = view.findViewById(R.id.sessionDurationTextView);
            deviceModelTextView = view.findViewById(R.id.deviceModelTextView);
            readingCountTextView = view.findViewById(R.id.readingCountTextView);
            sessionContainer = (ConstraintLayout) view;
        }

    }
}
