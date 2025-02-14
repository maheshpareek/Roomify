package com.example.roomify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView totalBookings, upcomingCheckins, availableRooms, bookedRooms, totalRevenue;
    private Button manageRoomsButton, manageBookingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize the views
        totalBookings = findViewById(R.id.total_bookings);
        upcomingCheckins = findViewById(R.id.upcoming_checkins);
        availableRooms = findViewById(R.id.available_rooms);
        bookedRooms = findViewById(R.id.booked_rooms);
        totalRevenue = findViewById(R.id.total_revenue);
        manageRoomsButton = findViewById(R.id.btn_manage_rooms);
        manageBookingsButton = findViewById(R.id.btn_manage_bookings);

        // Set mock data (replace with real data from Firebase/Backend)
        totalBookings.setText("150");
        upcomingCheckins.setText("5");
        availableRooms.setText("12");
        bookedRooms.setText("30");
        totalRevenue.setText("$10,000");

        // Setup manage rooms button click listener
        manageRoomsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ManageRoomsActivity.class);
            startActivity(intent);
        });

        // Setup manage bookings button click listener
        manageBookingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ManageBookingsActivity.class);
            startActivity(intent);
        });
    }
}
