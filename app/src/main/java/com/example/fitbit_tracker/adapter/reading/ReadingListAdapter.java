package com.example.fitbit_tracker.adapter.reading;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.fitbit_tracker.domain_model.ReadingBatchDM;
import com.example.fitbit_tracker.domain_model.ReadingDM;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ReadingListAdapter extends ListAdapter<ReadingBatchDM, ReadingViewHolder> {

    private Context context;

    public ReadingListAdapter(@NonNull DiffUtil.ItemCallback<ReadingBatchDM> diffCallback, Context baseContext) {
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
        ReadingBatchDM readingBatchDM = getItem(position);
        holder.bindChartLabel(readingBatchDM.getName());

        HashMap<String, List<ReadingDM>> batches = readingBatchDM.getReadingList();

        Set<String> keys = batches.keySet();

        LineData lineData = new LineData();

        for (String key : keys) {
            List<ReadingDM> batch = batches.get(key);

            List<Entry> entryList = new ArrayList<Entry>();

            int i = 0;
            for (ReadingDM reading: batch) {
                entryList.add(new Entry(i, reading.getData()));
                i++;
            }

            LineDataSet dataSet = new LineDataSet(entryList, key);
            dataSet.setColor(Color.rgb(1,1,1));
            dataSet.setValueTextColor(Color.rgb(1,1,1));
            dataSet.setCircleHoleRadius(1f);
            dataSet.setDrawCircleHole(false);
            dataSet.setColor(Color.RED);
            dataSet.setCircleColor(Color.RED);
            dataSet.setCircleRadius(2f);

            lineData.addDataSet(dataSet);
        }
        holder.bindChartData(lineData);
    }

    public static class ReadingDiff extends DiffUtil.ItemCallback<ReadingBatchDM> {

        @Override
        public boolean areItemsTheSame(@NonNull ReadingBatchDM oldItem, @NonNull ReadingBatchDM newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ReadingBatchDM oldItem, @NonNull ReadingBatchDM newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

    }

}
