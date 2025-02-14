package com.example.roomify;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class UserDashboardActivity extends AppCompatActivity {

    private TextView userNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        // Find the user name TextView by its ID
        userNameTextView = findViewById(R.id.user_name);

        // Get current user info from Firebase
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        // Set the user name or a default message if no name is found
        if (userName != null) {
            userNameTextView.setText("Welcome, " + userName);
        } else {
            userNameTextView.setText("Welcome, Guest");
        }
    }
}
