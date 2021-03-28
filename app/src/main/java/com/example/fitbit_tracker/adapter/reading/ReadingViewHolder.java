package com.example.fitbit_tracker.adapter.reading;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitbit_tracker.R;

public class ReadingViewHolder extends RecyclerView.ViewHolder {

    private final TextView chartLabel;
    private final ConstraintLayout readingContainer;

    public ReadingViewHolder(View itemView) {
        super(itemView);
        chartLabel = itemView.findViewById(R.id.readingTypeTextView);
        readingContainer = itemView.findViewById(R.id.readingContainer);
    }

    public void bindChartLabel(String label) {
        chartLabel.setText(label);
    }

    public void bindOnClickListener(View.OnClickListener onClickListener) {
        readingContainer.setOnClickListener(onClickListener);
    }

    public static ReadingViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_item, parent, false);
        return new ReadingViewHolder(view);
    }


}
