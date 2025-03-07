package com.example.roomify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

// Add these imports
import com.example.roomify.R;
import com.example.roomify.models.Room;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList;
    private OnRoomClickListener listener;

    public interface OnRoomClickListener {
        void onRoomClick(Room room, int position);
        void onRoomOptionsClick(Room room, int position, View view);
    }

    public RoomAdapter(List<Room> roomList, OnRoomClickListener listener) {
        this.roomList = roomList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);

        // Set room number
        holder.roomNumberTextView.setText("Room " + room.getRoomNumber());

        // Set room type
        holder.roomTypeTextView.setText(room.getType());

        // Format price with currency symbol
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        holder.priceTextView.setText(currencyFormat.format(room.getPrice()) + " / night");

        // Set availability status
        if (room.isAvailable()) {
            holder.availabilityTextView.setText("Available");
            holder.availabilityTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.availabilityTextView.setText("Booked");
            holder.availabilityTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
        }

        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRoomClick(room, position);
            }
        });

        holder.optionsButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRoomOptionsClick(room, position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public void updateData(List<Room> newRoomList) {
        this.roomList = newRoomList;
        notifyDataSetChanged();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomNumberTextView;
        TextView roomTypeTextView;
        TextView priceTextView;
        TextView availabilityTextView;
        ImageView optionsButton;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomNumberTextView = itemView.findViewById(R.id.room_number);
            roomTypeTextView = itemView.findViewById(R.id.room_type);
            priceTextView = itemView.findViewById(R.id.room_price);
            availabilityTextView = itemView.findViewById(R.id.availability_status);
            optionsButton = itemView.findViewById(R.id.room_options_button);
        }
    }
}