package com.example.fitbit_tracker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitbit_tracker.model.Session;
import com.example.fitbit_tracker.views.SessionDetailsActivity;
import com.example.fitbit_tracker.views.SessionsActivity;

import java.util.List;

public class SessionRecyclerViewAdapter extends RecyclerView.Adapter<SessionRecyclerViewAdapter.SessionsViewHolder> {
    private final List<Session> sessions;
    protected Context context;

    public SessionRecyclerViewAdapter(Context context, List<Session> sessions) {
        this.context = context;
        this.sessions = sessions;
    }

    @NonNull
    @Override
    public SessionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item, parent, false);
        return new SessionsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionsViewHolder holder, final int position) {
        final Session session = sessions.get(position);
        holder.description.setText(session.getUuid());
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    class SessionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView description;

        SessionsViewHolder(View view) {
            super(view);
            description = view.findViewById(R.id.sessionUUIDTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, SessionDetailsActivity.class);
            context.startActivity(intent);
        }
    }
}
