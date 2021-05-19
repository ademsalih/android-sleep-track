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
import com.example.fitbit_tracker.model.Sensor;
import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.model.SessionSensor;
import com.example.fitbit_tracker.view.BatchActivity;

import io.realm.Realm;


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
        SessionSensor sessionSensor = getItem(position);

        Sensor sensor = Realm.getDefaultInstance().where(Sensor.class).equalTo("sensorId", sessionSensor.getSensorId()).findFirst();
        Session session = Realm.getDefaultInstance().where(Session.class).equalTo("sessionId", sessionSensor.getSessionId()).findFirst();

        holder.bindChartLabel(sensor.getSensorName());
        holder.bindFrequencyLabel(sessionSensor.getFrequency());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BatchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("sessionId", session.getSessionId());
                bundle.putLong("sensorId", sensor.getSensorId());
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
