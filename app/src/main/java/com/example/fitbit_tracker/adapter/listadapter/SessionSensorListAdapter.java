package com.example.fitbit_tracker.adapter.listadapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.fitbit_tracker.adapter.viewholder.SessionSensorViewHolder;
import com.example.fitbit_tracker.model.SessionSensor;
import com.example.fitbit_tracker.view.BatchActivity;


public class SessionSensorListAdapter extends ListAdapter<SessionSensor, SessionSensorViewHolder> {

    private Context context;

    public SessionSensorListAdapter(@NonNull DiffUtil.ItemCallback<SessionSensor> diffCallback, Context baseContext) {
        super(diffCallback);
        this.context = baseContext;
    }

    @NonNull
    @Override
    public SessionSensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SessionSensorViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionSensorViewHolder holder, int position) {
        SessionSensor sensor = getItem(position);
        holder.bindChartLabel(sensor.getSensor().getSensorName());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BatchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("sessionId", sensor.getSession().getSessionId());
                bundle.putInt("sensorId", sensor.getSensor().getSensorId());
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        };

        holder.bindOnClickListener(onClickListener);
    }

    public static class ReadingDiff extends DiffUtil.ItemCallback<SessionSensor> {

        @Override
        public boolean areItemsTheSame(@NonNull SessionSensor oldItem, @NonNull SessionSensor newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull SessionSensor oldItem, @NonNull SessionSensor newItem) {
            return oldItem.getId() == newItem.getId();
        }

    }

}
