package com.example.roomify;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminSignupActivity extends AppCompatActivity {
    EditText adminName, adminEmail, adminPassword;
    Spinner adminRoleSpinner;
    Button adminSignUpButton;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        dbHelper = new DatabaseHelper(this);

        adminName = findViewById(R.id.adminName);
        adminEmail = findViewById(R.id.adminEmail);
        adminPassword = findViewById(R.id.adminPassword);
        adminRoleSpinner = findViewById(R.id.adminRoleSpinner);
        adminSignUpButton = findViewById(R.id.adminSignUpButton);

        adminSignUpButton.setOnClickListener(view -> {
            String name = adminName.getText().toString();
            String email = adminEmail.getText().toString();
            String password = adminPassword.getText().toString();
            String role = adminRoleSpinner.getSelectedItem().toString();

            boolean inserted = dbHelper.addAdmin(name, email, password, role);
            if (inserted) {
                Toast.makeText(AdminSignupActivity.this, "Admin Registered Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AdminSignupActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
