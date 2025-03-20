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

    // 1) Define the interface with the EXACT signature we will use:
    public interface OnUserClickListener {
        void onUserOptionsClick(User user, int position, View anchorView);
    }

    private List<User> userList;
    private OnUserClickListener listener;

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

        // Example data binding (assuming you have these IDs in item_user.xml)
        holder.nameTextView.setText(user.getFirstName() + " " + user.getLastName());
        holder.emailTextView.setText(user.getEmail());

        // 2) When the options button is clicked, we call onUserOptionsClick
        holder.optionsButton.setOnClickListener(v -> {
            if (listener != null) {
                // EXACT param order: (User, int, View)
                listener.onUserOptionsClick(user, position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Standard ViewHolder
    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView emailTextView;
        ImageView optionsButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.user_name);
            emailTextView = itemView.findViewById(R.id.user_email);
            optionsButton = itemView.findViewById(R.id.user_options_button);
        }
    }
}
