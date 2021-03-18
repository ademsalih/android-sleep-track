package com.example.fitbit_tracker.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.view.SessionDetailsActivity;
import com.example.fitbit_tracker.viewholder.SessionViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.fitbit_tracker.utils.TimeUtils.formattedTimeLabel;
import static com.example.fitbit_tracker.utils.TimeUtils.timeDeltaLabel;

public class SessionListAdapter extends ListAdapter<Session, SessionViewHolder> {

    public SessionListAdapter(@NonNull DiffUtil.ItemCallback<Session> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SessionViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        Session session = getItem(position);

        Date sessionStartDate = new Date(session.getStartTime());
        Date sessionEndDate = new Date(session.getEndTime());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.US);
        String sessionStartFormatted = simpleDateFormat.format(sessionStartDate);
        String sessionEndFormatted = simpleDateFormat.format(sessionEndDate);

        /*holder.sessionStartEndTextView.setText(sessionStartFormatted + " – " + sessionEndFormatted);

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
        });*/

        holder.bind(session.getDeviceModel());
    }

    public static class SessionDiff extends DiffUtil.ItemCallback<Session> {

        @Override
        public boolean areItemsTheSame(@NonNull Session oldItem, @NonNull Session newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Session oldItem, @NonNull Session newItem) {
            return oldItem.getUuid().equals(newItem.getUuid());
        }
    }

}
