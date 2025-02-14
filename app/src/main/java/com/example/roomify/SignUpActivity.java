package com.example.roomify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private GoogleSignInClient mGoogleSignInClient;

    private EditText emailInput, passwordInput, confirmPasswordInput, firstNameInput, lastNameInput;
    private Button signUpButton;
    private LinearLayout googleSignUpButton;

    private ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<androidx.activity.result.ActivityResult>() {
                        @Override
                        public void onActivityResult(androidx.activity.result.ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK) {
                                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                                try {
                                    GoogleSignInAccount account = task.getResult(ApiException.class);
                                    firebaseAuthWithGoogle(account);
                                } catch (ApiException e) {
                                    Toast.makeText(SignUpActivity.this, "Google Sign-in failed: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize UI elements
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        firstNameInput = findViewById(R.id.firstNameInput);  // New field for first name
        lastNameInput = findViewById(R.id.lastNameInput);    // New field for last name
        signUpButton = findViewById(R.id.signUpButton);
        googleSignUpButton = findViewById(R.id.googleSignUpButton);

        signUpButton.setOnClickListener(v -> registerUser());
        googleSignUpButton.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });
    }

    private void registerUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(SignUpActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignUpActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            sendEmailVerification(user);
                            // Use the user-inputted first and last name
                            createUserInFirestore(user.getUid(), firstName, lastName, email);
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void createUserInFirestore(String userId, String firstName, String lastName, String email) {
        // Create user object with first name, last name, and email
        User user = new User(firstName, lastName, email, "guest");

        // Save the user object to Firestore
        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignUpActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                    loginUser(email, passwordInput.getText().toString());
                })
                .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, "Firestore Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(SignUpActivity.this, UserDashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(SignUpActivity.this, "Verification email sent! Please check your inbox.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        if (account != null) {
            String firstName = account.getGivenName();
            String lastName = account.getFamilyName();
            String email = account.getEmail();

            // Use the Google user's first name and last name
            User user = new User(firstName, lastName, email, "guest");

            // Save Google user data to Firestore
            db.collection("users").document(account.getId())
                    .set(user)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(SignUpActivity.this, "Google Sign-in successful!", Toast.LENGTH_SHORT).show();
                        loginUser(account.getEmail(), passwordInput.getText().toString());
                    })
                    .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, "Firestore Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}
