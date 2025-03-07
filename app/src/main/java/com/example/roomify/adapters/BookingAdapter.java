package com.example.roomify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

// Add these imports
import com.example.roomify.R;
import com.example.roomify.models.Booking;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookingList;
    private OnBookingClickListener listener;
    private SimpleDateFormat dateFormat;

    public interface OnBookingClickListener {
        void onBookingClick(Booking booking, int position);
        void onBookingOptionsClick(Booking booking, int position, View view);
    }

    public BookingAdapter(List<Booking> bookingList, OnBookingClickListener listener) {
        this.bookingList = bookingList;
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        // Set room number
        holder.roomNumberTextView.setText("Room " + booking.getRoomNumber());

        // Set guest name if available, otherwise use "Guest"
        String guestName = booking.getGuestName();
        holder.guestNameTextView.setText(guestName != null ? guestName : "Guest");

        // Format check-in and check-out dates
        String checkin = dateFormat.format(booking.getCheckInDate());
        String checkout = dateFormat.format(booking.getCheckOutDate());
        holder.dateRangeTextView.setText(checkin + " - " + checkout);

        // Format price with currency symbol
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        holder.priceTextView.setText(currencyFormat.format(booking.getTotalPrice()));

        // Set status with appropriate color and formatting
        holder.statusTextView.setText(formatStatus(booking.getStatus()));
        setStatusColor(holder.statusTextView, booking.getStatus());

        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookingClick(booking, position);
            }
        });

        holder.optionsButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookingOptionsClick(booking, position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void updateData(List<Booking> newBookingList) {
        this.bookingList = newBookingList;
        notifyDataSetChanged();
    }

    private String formatStatus(String status) {
        if (status == null) return "Unknown";

        switch (status) {
            case "pending":
                return "Pending";
            case "confirmed":
                return "Confirmed";
            case "checked_in":
                return "Checked In";
            case "checked_out":
                return "Checked Out";
            case "cancelled":
                return "Cancelled";
            default:
                return status.substring(0, 1).toUpperCase() + status.substring(1).replace("_", " ");
        }
    }

    private void setStatusColor(TextView textView, String status) {
        int colorResId;

        if (status == null) {
            colorResId = android.R.color.darker_gray;
        } else {
            switch (status) {
                case "confirmed":
                    colorResId = android.R.color.holo_green_dark;
                    break;
                case "pending":
                    colorResId = android.R.color.holo_orange_dark;
                    break;
                case "checked_in":
                    colorResId = android.R.color.holo_blue_dark;
                    break;
                case "checked_out":
                    colorResId = android.R.color.holo_purple;
                    break;
                case "cancelled":
                    colorResId = android.R.color.holo_red_dark;
                    break;
                default:
                    colorResId = android.R.color.darker_gray;
            }
        }

        textView.setTextColor(textView.getContext().getResources().getColor(colorResId));
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView roomNumberTextView;
        TextView guestNameTextView;
        TextView dateRangeTextView;
        TextView priceTextView;
        TextView statusTextView;
        ImageView optionsButton;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            roomNumberTextView = itemView.findViewById(R.id.booking_room_number);
            guestNameTextView = itemView.findViewById(R.id.booking_guest_name);
            dateRangeTextView = itemView.findViewById(R.id.booking_date_range);
            priceTextView = itemView.findViewById(R.id.booking_price);
            statusTextView = itemView.findViewById(R.id.booking_status);
            optionsButton = itemView.findViewById(R.id.booking_options_button);
        }
    }
}