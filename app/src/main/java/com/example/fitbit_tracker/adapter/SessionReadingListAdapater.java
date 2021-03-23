package com.example.fitbit_tracker.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.fitbit_tracker.model.ReadingBatch;
import com.example.fitbit_tracker.model.reading.Reading;

import java.util.List;

public class SessionReadingListAdapater extends ListAdapter<ReadingBatch, ReadingViewHolder> {

    private Context context;

    public SessionReadingListAdapater(@NonNull DiffUtil.ItemCallback<ReadingBatch> diffCallback, Context baseContext) {
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
        ReadingBatch readingBatch = getItem(position);

        holder.bindChartLabel(readingBatch.getSensorName());


        holder.bindChartData(null);
    }

    public static class ReadingDiff extends DiffUtil.ItemCallback<ReadingBatch> {

        @Override
        public boolean areItemsTheSame(@NonNull ReadingBatch oldItem, @NonNull ReadingBatch newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ReadingBatch oldItem, @NonNull ReadingBatch newItem) {
            return oldItem.getSensorName().equals(newItem.getSensorName());
        }

    }

}
