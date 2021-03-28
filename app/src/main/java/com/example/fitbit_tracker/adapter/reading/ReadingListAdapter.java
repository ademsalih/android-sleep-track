package com.example.fitbit_tracker.adapter.reading;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.fitbit_tracker.model.Reading;
import com.example.fitbit_tracker.view.BatchActivity;


public class ReadingListAdapter extends ListAdapter<Reading, ReadingViewHolder> {

    private Context context;

    public ReadingListAdapter(@NonNull DiffUtil.ItemCallback<Reading> diffCallback, Context baseContext) {
        super(diffCallback);
        this.context = baseContext;
    }

    @NonNull
    @Override
    public ReadingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ReadingViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingViewHolder holder, int position) {
        Reading reading = getItem(position);
        holder.bindChartLabel(reading.getSensorName());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BatchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("sessionId", reading.getSessionId());
                bundle.putString("sensorName", reading.getSensorName());
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        };

        holder.bindOnClickListener(onClickListener);
    }

    public static class ReadingDiff extends DiffUtil.ItemCallback<Reading> {

        @Override
        public boolean areItemsTheSame(@NonNull Reading oldItem, @NonNull Reading newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Reading oldItem, @NonNull Reading newItem) {
            return oldItem.getSensorName().equals(newItem.getSensorName());
        }

    }

}
