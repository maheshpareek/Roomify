package com.example.roomify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomify.R;
import com.example.roomify.activities.user.UserDashboardActivity.ActivityItem;

import java.util.List;

public class RecentActivityAdapter extends RecyclerView.Adapter<RecentActivityAdapter.ActivityViewHolder> {

    private List<ActivityItem> activityItems;

    public RecentActivityAdapter(List<ActivityItem> activityItems) {
        this.activityItems = activityItems;
    }

    public void updateData(List<ActivityItem> newItems) {
        this.activityItems = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_activity, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        ActivityItem item = activityItems.get(position);
        holder.activityIcon.setImageResource(item.getIconResId());
        holder.activityTitle.setText(item.getTitle());
        holder.activityDescription.setText(item.getDescription());

        if (item.getTimestamp() != null && !item.getTimestamp().isEmpty()) {
            holder.activityTimestamp.setText(item.getTimestamp());
            holder.activityTimestamp.setVisibility(View.VISIBLE);
        } else {
            holder.activityTimestamp.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return activityItems.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        ImageView activityIcon;
        TextView activityTitle;
        TextView activityDescription;
        TextView activityTimestamp;

        ActivityViewHolder(View itemView) {
            super(itemView);
            activityIcon = itemView.findViewById(R.id.activity_icon);
            activityTitle = itemView.findViewById(R.id.activity_title);
            activityDescription = itemView.findViewById(R.id.activity_description);
            activityTimestamp = itemView.findViewById(R.id.activity_timestamp);
        }
    }
}