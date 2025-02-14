package com.example.roomify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.api.ApiException;

public class SignUpActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseHelper dbHelper;
    private EditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Database Helper
        dbHelper = DatabaseHelper.getInstance(this);

        // Google Sign-In Setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize UI Components
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        signUpButton = findViewById(R.id.signUpButton);
        LinearLayout googleSignUpButton = findViewById(R.id.googleSignUpButton);

        // Handle Regular Sign-Up
        signUpButton.setOnClickListener(v -> validateAndSignup());

        // Handle Google Sign-Up
        googleSignUpButton.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 101);
        });
    }

    // Method to Validate Inputs and Perform Signup
    private void validateAndSignup() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "⚠️ Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUpActivity.this, "⚠️ Please check password and confirm password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.checkEmailExists(email)) {
            Toast.makeText(SignUpActivity.this, "⚠️ Email is already registered!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.addUser(name, email, password)) {
            Toast.makeText(SignUpActivity.this, "✅ Signup Successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            finish();
        } else {
            Toast.makeText(SignUpActivity.this, "❌ Signup Failed! Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to Handle Google Sign-Up
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    String email = account.getEmail();
                    String name = account.getDisplayName();

                    if (!dbHelper.checkEmailExists(email)) {
                        boolean success = dbHelper.addUser(name, email, "GoogleAuth");
                        if (success) {
                            Toast.makeText(SignUpActivity.this, "✅ Google Signup Successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "❌ Google Signup Failed! Try Again.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "⚠️ Google Account Already Exists!", Toast.LENGTH_SHORT).show();
                    }

                    // Redirect to Login Page after successful signup
                    startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                    finish();
                }
            } catch (ApiException e) {
                Log.e("Google Sign-Up", "Error: " + e.getStatusCode());
                Toast.makeText(SignUpActivity.this, "❌ Google Sign-Up Failed! Error Code: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}


