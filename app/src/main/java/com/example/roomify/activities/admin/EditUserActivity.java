package com.example.roomify.activities.admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roomify.R;
import com.example.roomify.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditUserActivity extends AppCompatActivity {

    private EditText firstNameInput, lastNameInput, emailInput;
    private Spinner userTypeSpinner;
    private Button updateUserButton;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        db = FirebaseFirestore.getInstance();
        userId = getIntent().getStringExtra("userId");

        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        emailInput = findViewById(R.id.emailInput);
        userTypeSpinner = findViewById(R.id.userTypeSpinner);
        updateUserButton = findViewById(R.id.updateUserButton);

        // Spinner with admin, guest
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(spinnerAdapter);

        // Load user details
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            firstNameInput.setText(user.getFirstName());
                            lastNameInput.setText(user.getLastName());
                            emailInput.setText(user.getEmail());
                            String userType = user.getUserType();
                            if (userType != null) {
                                int pos = spinnerAdapter.getPosition(userType.toLowerCase());
                                if (pos >= 0) {
                                    userTypeSpinner.setSelection(pos);
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditUserActivity.this, "Error loading user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        updateUserButton.setOnClickListener(v -> {
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String userType = userTypeSpinner.getSelectedItem().toString().toLowerCase();

            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email)) {
                Toast.makeText(EditUserActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("users").document(userId)
                    .update("firstName", firstName,
                            "lastName", lastName,
                            "email", email,
                            "userType", userType)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditUserActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(EditUserActivity.this, "Error updating user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
