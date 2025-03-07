package com.example.roomify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomify.R;
import com.example.roomify.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(User user, int position);
        void onUserOptionsClick(User user, int position, View view);
    }

    public UserAdapter(List<User> userList, OnUserClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        // Set user name
        holder.nameTextView.setText(user.getFirstName() + " " + user.getLastName());

        // Set user email
        holder.emailTextView.setText(user.getEmail());

        // Set user type with first letter capitalized
        String userType = user.getUserType();
        if (userType != null && !userType.isEmpty()) {
            userType = userType.substring(0, 1).toUpperCase() + userType.substring(1).toLowerCase();
        }
        holder.userTypeTextView.setText(userType);

        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(user, position);
            }
        });

        holder.optionsButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserOptionsClick(user, position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateData(List<User> newUserList) {
        this.userList = newUserList;
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView emailTextView;
        TextView userTypeTextView;
        ImageView optionsButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.user_name);
            emailTextView = itemView.findViewById(R.id.user_email);
            userTypeTextView = itemView.findViewById(R.id.user_type);
            optionsButton = itemView.findViewById(R.id.user_options_button);
        }
    }
}