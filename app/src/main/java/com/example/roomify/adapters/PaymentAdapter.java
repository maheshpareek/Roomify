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

// Add missing imports
import com.example.roomify.R;
import com.example.roomify.models.Payment;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    private List<Payment> paymentList;
    private OnPaymentClickListener listener;
    private SimpleDateFormat dateFormat;

    public interface OnPaymentClickListener {
        void onPaymentClick(Payment payment, int position);
        void onPaymentOptionsClick(Payment payment, int position, View view);
    }

    public PaymentAdapter(List<Payment> paymentList, OnPaymentClickListener listener) {
        this.paymentList = paymentList;
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment, parent, false);
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        Payment payment = paymentList.get(position);

        // Format amount with currency symbol
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        holder.amountTextView.setText(currencyFormat.format(payment.getAmount()));

        // Format payment date
        String formattedDate = dateFormat.format(payment.getPaymentDate());
        holder.dateTextView.setText(formattedDate);

        // Set payment method
        holder.methodTextView.setText(formatPaymentMethod(payment.getPaymentMethod()));

        // Set payment status with appropriate color
        holder.statusTextView.setText(formatStatus(payment.getStatus()));
        setStatusColor(holder.statusTextView, payment.getStatus());

        // Set booking reference if available
        if (payment.getBookingId() != null) {
            holder.bookingRefTextView.setText("Booking: " + shortenId(payment.getBookingId()));
            holder.bookingRefTextView.setVisibility(View.VISIBLE);
        } else {
            holder.bookingRefTextView.setVisibility(View.GONE);
        }

        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPaymentClick(payment, position);
            }
        });

        holder.optionsButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPaymentOptionsClick(payment, position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public void updateData(List<Payment> newPaymentList) {
        this.paymentList = newPaymentList;
        notifyDataSetChanged();
    }

    private String formatPaymentMethod(String method) {
        if (method == null) return "Unknown";

        switch (method) {
            case "credit_card":
                return "Credit Card";
            case "debit_card":
                return "Debit Card";
            case "cash":
                return "Cash";
            case "bank_transfer":
                return "Bank Transfer";
            default:
                return method.substring(0, 1).toUpperCase() + method.substring(1).replace("_", " ");
        }
    }

    private String formatStatus(String status) {
        if (status == null) return "Unknown";

        switch (status) {
            case "pending":
                return "Pending";
            case "completed":
                return "Completed";
            case "failed":
                return "Failed";
            case "refunded":
                return "Refunded";
            default:
                return status.substring(0, 1).toUpperCase() + status.substring(1);
        }
    }

    private void setStatusColor(TextView textView, String status) {
        int colorResId;

        if (status == null) {
            colorResId = android.R.color.darker_gray;
        } else {
            switch (status) {
                case "completed":
                    colorResId = android.R.color.holo_green_dark;
                    break;
                case "pending":
                    colorResId = android.R.color.holo_orange_dark;
                    break;
                case "failed":
                    colorResId = android.R.color.holo_red_dark;
                    break;
                case "refunded":
                    colorResId = android.R.color.holo_blue_dark;
                    break;
                default:
                    colorResId = android.R.color.darker_gray;
            }
        }

        textView.setTextColor(textView.getContext().getResources().getColor(colorResId));
    }

    private String shortenId(String id) {
        if (id == null || id.length() <= 8) return id;
        return id.substring(0, 8) + "...";
    }

    static class PaymentViewHolder extends RecyclerView.ViewHolder {
        TextView amountTextView;
        TextView dateTextView;
        TextView methodTextView;
        TextView statusTextView;
        TextView bookingRefTextView;
        ImageView optionsButton;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.payment_amount);
            dateTextView = itemView.findViewById(R.id.payment_date);
            methodTextView = itemView.findViewById(R.id.payment_method);
            statusTextView = itemView.findViewById(R.id.payment_status);
            bookingRefTextView = itemView.findViewById(R.id.booking_reference);
            optionsButton = itemView.findViewById(R.id.payment_options_button);
        }
    }
}