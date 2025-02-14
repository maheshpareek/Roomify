package com.example.roomify;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Database
        dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Force database creation

        // Check if database exists
        if (doesDatabaseExist(this)) {
            Log.d("DatabaseCheck", "✅ Roomify database exists!");
        } else {
            Log.d("DatabaseCheck", "❌ Roomify database NOT found!");
        }

        // Request storage permission (only required for older Android versions)
        requestStoragePermission();

        // Find Sign Up and Log In buttons
        Button signUpButton = findViewById(R.id.signUpButton);
        Button logInButton = findViewById(R.id.logInButton);

        // Set up click listeners using lambda expressions
        signUpButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SignUpActivity.class)));
        logInButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LogInActivity.class)));
    }

    // Method to check if the database exists
    private boolean doesDatabaseExist(Context context) {
        File dbFile = context.getDatabasePath("roomify.db");
        return dbFile.exists();
    }

    // Method to request storage permissions
    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 1);
            }
        }
    }
}
