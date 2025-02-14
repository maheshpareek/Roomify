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

public class LogInActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseHelper dbHelper;
    private EditText emailInput, passwordInput;
    private Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Database Helper
        dbHelper = DatabaseHelper.getInstance(this);

        // Google Sign-In Setup
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize UI Components
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        logInButton = findViewById(R.id.login_button);

        // Regular Log-In
        logInButton.setOnClickListener(v -> validateAndLogin());

        // Google Login Button Click
        LinearLayout googleSignUpButton = findViewById(R.id.googleSignUpButton);
        googleSignUpButton.setOnClickListener(v -> signInWithGoogle());
    }

    private void validateAndLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LogInActivity.this, "⚠️ Please enter email and password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.checkUser(email, password)) {
            Toast.makeText(LogInActivity.this, "✅ Login Successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LogInActivity.this, UserHomeActivity.class));
            finish();
        } else {
            Toast.makeText(LogInActivity.this, "⚠️ Email or Password is incorrect!", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 102);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    String email = account.getEmail();

                    // Check if the email exists in the database
                    if (dbHelper.checkEmailExists(email)) {
                        Toast.makeText(LogInActivity.this, "✅ Login Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LogInActivity.this, UserHomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LogInActivity.this, "⚠️ Please sign up first!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
                    }
                }
            } catch (ApiException e) {
                Toast.makeText(LogInActivity.this, "❌ Google Sign-In Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
