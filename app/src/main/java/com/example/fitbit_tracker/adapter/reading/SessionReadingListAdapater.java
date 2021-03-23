package com.example.fitbit_tracker.adapter.reading;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.fitbit_tracker.model.Reading;

public class SessionReadingListAdapater extends ListAdapter<Reading, ReadingViewHolder> {

    private Context context;

    public SessionReadingListAdapater(@NonNull DiffUtil.ItemCallback<Reading> diffCallback, Context baseContext) {
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

        holder.bindChartLabel(null);
        holder.bindChartData(null);
    }

    public static class ReadingDiff extends DiffUtil.ItemCallback<Reading> {

        @Override
        public boolean areItemsTheSame(@NonNull Reading oldItem, @NonNull Reading newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Reading oldItem, @NonNull Reading newItem) {
            return oldItem.getSensorId() == newItem.getSensorId();
        }

    }

}
