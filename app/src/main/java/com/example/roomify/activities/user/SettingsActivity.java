package com.example.roomify.activities.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roomify.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {

    private TextInputEditText nameInput, emailInput;
    private Button updateProfileButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); // your settings.xml layout

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);
        updateProfileButton = findViewById(R.id.update_profile_button);

        // Load user data from Firestore and populate the fields
        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String firstName = documentSnapshot.getString("firstName");
                            String lastName = documentSnapshot.getString("lastName");
                            String email = documentSnapshot.getString("email");

                            // Concatenate first and last name to show in the full name field
                            String fullName = "";
                            if (firstName != null) {
                                fullName += firstName;
                            }
                            if (lastName != null && !lastName.isEmpty()) {
                                fullName += " " + lastName;
                            }
                            nameInput.setText(fullName.trim());
                            emailInput.setText(email);
                        } else {
                            Toast.makeText(SettingsActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SettingsActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                    });
        }

        // Update button click listener – update Firestore with new name
        updateProfileButton.setOnClickListener(v -> {
            String fullName = nameInput.getText().toString().trim();
            if (TextUtils.isEmpty(fullName)) {
                Toast.makeText(SettingsActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            // Split full name into first and last names (if possible)
            String firstName = fullName;
            String lastName = "";
            if (fullName.contains(" ")) {
                int index = fullName.indexOf(" ");
                firstName = fullName.substring(0, index).trim();
                lastName = fullName.substring(index + 1).trim();
            }
            // Update Firestore document with new names
            db.collection("users").document(currentUser.getUid())
                    .update("firstName", firstName, "lastName", lastName)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(SettingsActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SettingsActivity.this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
