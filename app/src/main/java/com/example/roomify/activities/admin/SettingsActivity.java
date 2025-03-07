package com.example.roomify.activities.admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.roomify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class SettingsActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText emailInput;
    private Switch notificationsSwitch;
    private Button updateProfileButton;
    private Button changePasswordButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize UI components
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);
        notificationsSwitch = findViewById(R.id.notifications_switch);
        updateProfileButton = findViewById(R.id.update_profile_button);
        changePasswordButton = findViewById(R.id.change_password_button);

        // Load user profile data
        loadUserProfile();

        // Set up click listeners
        updateProfileButton.setOnClickListener(v -> updateProfile());
        changePasswordButton.setOnClickListener(v -> sendPasswordResetEmail());
    }

    private void loadUserProfile() {
        if (currentUser != null) {
            // Set the email field
            emailInput.setText(currentUser.getEmail());

            // Get additional user data from Firestore
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String firstName = document.getString("firstName");
                                String lastName = document.getString("lastName");
                                if (firstName != null && lastName != null) {
                                    nameInput.setText(firstName + " " + lastName);
                                }

                                // You could also load notification preferences here if stored
                            }
                        } else {
                            Toast.makeText(SettingsActivity.this, "Error loading profile: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateProfile() {
        if (currentUser == null) return;

        String fullName = nameInput.getText().toString().trim();
        String[] nameParts = fullName.split(" ", 2);
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        // Update Firestore document
        db.collection("users").document(currentUser.getUid())
                .update(
                        "firstName", firstName,
                        "lastName", lastName,
                        "notificationsEnabled", notificationsSwitch.isChecked()
                )
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SettingsActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SettingsActivity.this, "Error updating profile: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void sendPasswordResetEmail() {
        if (currentUser == null) return;

        String email = currentUser.getEmail();
        if (email != null && !email.isEmpty()) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this,
                                    "Password reset email sent to " + email,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SettingsActivity.this,
                                    "Failed to send password reset email: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}