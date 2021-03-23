package com.example.fitbit_tracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fitbit_tracker.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

public class ReadingViewHolder extends RecyclerView.ViewHolder {

    private final TextView chartLabel;
    private final LineChart lineChart;

    public ReadingViewHolder(View itemView) {
        super(itemView);
        chartLabel = itemView.findViewById(R.id.readingTypeTextView);
        lineChart = itemView.findViewById(R.id.chart);
    }

    public void bindChartLabel(String label) {
        chartLabel.setText(label);
    }

    public void bindChartData(LineData lineData) {
        lineChart.setData(lineData);
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    public static ReadingViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reading_item, parent, false);
        return new ReadingViewHolder(view);
    }

}
