package com.example.roomify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomify.R;
import com.example.roomify.models.ActivityItem;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private List<ActivityItem> activityItems;

    public ActivityAdapter(List<ActivityItem> activityItems) {
        this.activityItems = activityItems;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity, parent, false);
        return new ActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        ActivityItem item = activityItems.get(position);

        holder.titleText.setText(item.getTitle());
        holder.subtitleText.setText(item.getSubtitle());

        // Set icon based on activity type
        switch (item.getType()) {
            case ActivityItem.TYPE_BOOKING:
                holder.iconView.setImageResource(R.drawable.ic_booking);
                break;
            case ActivityItem.TYPE_PAYMENT:
                holder.iconView.setImageResource(R.drawable.ic_payment);
                break;
            case ActivityItem.TYPE_USER:
                // Use a default user icon if ic_users drawable is missing
                holder.iconView.setImageResource(R.drawable.ic_profile_placeholder);
                break;
            case ActivityItem.TYPE_ROOM:
                // Use a default room icon if ic_room drawable is missing
                holder.iconView.setImageResource(R.drawable.ic_book_room);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return activityItems.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        ImageView iconView;
        TextView titleText;
        TextView subtitleText;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.activity_icon);
            titleText = itemView.findViewById(R.id.activity_title);
            subtitleText = itemView.findViewById(R.id.activity_subtitle);
        }
    }
}