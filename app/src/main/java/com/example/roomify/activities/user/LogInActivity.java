package com.example.roomify.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roomify.R;
import com.example.roomify.activities.admin.AdminDashboardActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private GoogleSignInClient mGoogleSignInClient;

    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<androidx.activity.result.ActivityResult>() {
                        @Override
                        public void onActivityResult(androidx.activity.result.ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK) {
                                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                                try {
                                    GoogleSignInAccount account = task.getResult(ApiException.class);
                                    if (account != null) {
                                        checkUserType(account.getId());
                                    }
                                } catch (ApiException e) {
                                    Toast.makeText(LogInActivity.this, "Google Sign-In failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button logInButton = findViewById(R.id.login_button);
        LinearLayout googleSignInButton = findViewById(R.id.googleSignUpButton);
        // Bind the Forgot Password TextView (make sure it's defined in your login.xml)
        TextView forgotPasswordText = findViewById(R.id.forgotPasswordText);

        // Set click listener to launch ForgotPasswordActivity when user clicks "Forgot Password?"
        forgotPasswordText.setOnClickListener(v -> {
            startActivity(new Intent(LogInActivity.this, ForgotPasswordActivity.class));
        });

        // Log in with Email/Password
        logInButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LogInActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkUserType(user.getUid());
                            }
                        } else {
                            Toast.makeText(LogInActivity.this, "Login failed! Invalid credentials.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Google Sign-In button click listener
        googleSignInButton.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });

        // Google Sign-In setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void checkUserType(String uid) {
        db.collection("users").document(uid)
                .get(com.google.firebase.firestore.Source.SERVER) // Force fresh data
                .addOnSuccessListener(document -> {
                    String userType = document.getString("userType");
                    Log.d("FirestoreDebug", "Fetched userType: " + userType + " for UID: " + uid);

                    Intent intent;
                    if ("admin".equalsIgnoreCase(userType)) {
                        Log.d("FirestoreDebug", "Redirecting to AdminDashboardActivity");
                        Toast.makeText(this, "Redirecting to Admin Dashboard...", Toast.LENGTH_LONG).show();
                        intent = new Intent(this, AdminDashboardActivity.class);
                    } else {
                        Log.d("FirestoreDebug", "Redirecting to UserDashboardActivity");
                        Toast.makeText(this, "Redirecting to User Dashboard...", Toast.LENGTH_LONG).show();
                        intent = new Intent(this, UserDashboardActivity.class);
                    }
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LogInActivity.this, "Failed to get user data", Toast.LENGTH_SHORT).show();
                });
    }
}
