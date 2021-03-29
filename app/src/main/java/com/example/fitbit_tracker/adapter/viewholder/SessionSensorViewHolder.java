package com.example.fitbit_tracker.adapter.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitbit_tracker.R;

public class SessionSensorViewHolder extends RecyclerView.ViewHolder {

    private final TextView sensorTextView;
    private final ConstraintLayout readingContainer;

    public SessionSensorViewHolder(View itemView) {
        super(itemView);
        sensorTextView = itemView.findViewById(R.id.readingTypeTextView);
        readingContainer = itemView.findViewById(R.id.readingContainer);
    }

    public void bindChartLabel(String label) {
        this.sensorTextView.setText(label);
    }

    public void bindOnClickListener(View.OnClickListener onClickListener) {
        readingContainer.setOnClickListener(onClickListener);
    }

    public static SessionSensorViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_item, parent, false);
        return new SessionSensorViewHolder(view);
    }


}
