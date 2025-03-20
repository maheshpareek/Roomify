package com.example.roomify.activities.user;

<<<<<<< HEAD
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

=======
import android.os.Bundle;
import android.widget.TextView;
>>>>>>> upstream/main
import androidx.appcompat.app.AppCompatActivity;

import com.example.roomify.R;
import com.google.firebase.auth.FirebaseAuth;
<<<<<<< HEAD
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
=======
>>>>>>> upstream/main

public class UserDashboardActivity extends AppCompatActivity {

    private TextView userNameTextView;
<<<<<<< HEAD
    private Button btnSettings, btnLogout;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
=======
>>>>>>> upstream/main

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

<<<<<<< HEAD
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
=======
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
>>>>>>> upstream/main
    }
}
