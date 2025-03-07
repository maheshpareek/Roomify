package com.example.roomify.activities.admin;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomify.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ManagePaymentsActivity extends AppCompatActivity {

    private RecyclerView paymentsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_payments);

        // Initialize UI components
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Manage Payments");

        paymentsRecyclerView = findViewById(R.id.payments_recycler_view);
        paymentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton addPaymentFab = findViewById(R.id.fab_add_payment);
        addPaymentFab.setOnClickListener(v -> {
            Toast.makeText(this, "Record new payment functionality to be implemented", Toast.LENGTH_SHORT).show();
        });

        // Load mock payment data
        loadMockPaymentData();
    }

    private void loadMockPaymentData() {
        // For now, just show a toast with mock data
        Toast.makeText(this, "Loaded payment data (mock)", Toast.LENGTH_SHORT).show();

        // In a real implementation, you would:
        // 1. Fetch payment data from Firebase or your backend
        // 2. Create a Payment model class
        // 3. Create a PaymentAdapter class
        // 4. Populate and set the adapter on the RecyclerView
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}