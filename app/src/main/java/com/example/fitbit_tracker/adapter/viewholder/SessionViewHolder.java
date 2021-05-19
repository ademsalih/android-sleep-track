package com.example.fitbit_tracker.adapter.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitbit_tracker.R;

public class SessionViewHolder extends RecyclerView.ViewHolder {

    private final TextView sessionStartEndTextView;
    private final TextView timeDeltaTextView;
    private final TextView sessionDurationTextView;
    private final TextView deviceModelTextView;
    private final TextView readingCountTextView;
    private final ConstraintLayout sessionContainer;

    public SessionViewHolder(View itemView) {
        super(itemView);
        sessionStartEndTextView = itemView.findViewById(R.id.sessionStartEndTextView);
        timeDeltaTextView = itemView.findViewById(R.id.timeDeltaTextView);
        sessionDurationTextView = itemView.findViewById(R.id.sessionDurationTextView);
        deviceModelTextView = itemView.findViewById(R.id.deviceModelTextView);
        readingCountTextView = itemView.findViewById(R.id.readingCountTextView);
        sessionContainer = (ConstraintLayout) itemView;
    }

    public void bindDeviceModel(String deviceModel) {
        deviceModelTextView.setText(deviceModel);
    }

    public void bindStartEndTime(String time) {
        sessionStartEndTextView.setText(time);
    }

    public void bindTimeDelta(String timeDelta) {
        timeDeltaTextView.setText(timeDelta);
    }

    public void bindReadingsCount(String readingsCount) {
        readingCountTextView.setText(readingsCount);
    }

    public void bindDuration(String duration) {
        sessionDurationTextView.setText(duration);
    }

    public void bindOnClickListener(View.OnClickListener onClickListener) {
        sessionContainer.setOnClickListener(onClickListener);
    }

    public static SessionViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item, parent, false);
        return new SessionViewHolder(view);
    }

}
