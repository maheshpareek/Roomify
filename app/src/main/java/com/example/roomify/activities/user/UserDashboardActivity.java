package com.example.roomify.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roomify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDashboardActivity extends AppCompatActivity {

    private TextView userNameTextView;
    private Button btnSettings, btnLogout;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        userNameTextView = findViewById(R.id.user_name);
        btnSettings = findViewById(R.id.btn_settings);
        btnLogout = findViewById(R.id.btn_logout);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            // Fetch user details from Firestore to display their name
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String firstName = documentSnapshot.getString("firstName");
                            String lastName = documentSnapshot.getString("lastName");
                            String fullName = "";
                            if (firstName != null) {
                                fullName += firstName;
                            }
                            if (lastName != null && !lastName.isEmpty()) {
                                fullName += " " + lastName;
                            }
                            if (!fullName.isEmpty()) {
                                userNameTextView.setText("Welcome, " + fullName);
                            } else {
                                userNameTextView.setText("Welcome, Guest");
                            }
                        } else {
                            userNameTextView.setText("Welcome, Guest");
                        }
                    })
                    .addOnFailureListener(e -> userNameTextView.setText("Welcome, Guest"));
        } else {
            userNameTextView.setText("Welcome, Guest");
        }

        // Set click listener for the Settings button
        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(UserDashboardActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Set click listener for the Logout button
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Sign out the user
            // Clear the activity stack and start the login activity
            Intent loginIntent = new Intent(UserDashboardActivity.this, LogInActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
        });
    }
}
