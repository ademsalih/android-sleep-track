package com.example.fitbit_tracker.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

    public void bind(String text) {
        deviceModelTextView.setText(text);
    }

    public static SessionViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item_2, parent, false);
        return new SessionViewHolder(view);
    }
}
