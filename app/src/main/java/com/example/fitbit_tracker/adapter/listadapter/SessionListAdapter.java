package com.example.fitbit_tracker.adapter.listadapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.fitbit_tracker.adapter.viewholder.SessionViewHolder;
import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.view.SessionDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.fitbit_tracker.utils.TimeLabelUtils.formattedTimeLabel;
import static com.example.fitbit_tracker.utils.TimeLabelUtils.timeDeltaLabel;

public class SessionListAdapter extends ListAdapter<Session, SessionViewHolder> {

    private Context context;

    public SessionListAdapter(@NonNull DiffUtil.ItemCallback<Session> diffCallback, Context baseContext) {
        super(diffCallback);
        this.context = baseContext;
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

        long timeDelta = System.currentTimeMillis() - session.getEndTime();

        long duration = session.getEndTime() - session.getStartTime();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SessionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("sessionId", session.getSessionId());
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        };

        holder.bindDeviceModel(session.getDeviceModel());
        holder.bindStartEndTime(sessionStartFormatted + " – " + sessionEndFormatted);
        holder.bindTimeDelta(timeDeltaLabel(timeDelta));
        holder.bindReadingsCount(session.getReadingsCount() + " readings");
        holder.bindDuration(formattedTimeLabel(duration));
        holder.bindOnClickListener(onClickListener);
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
