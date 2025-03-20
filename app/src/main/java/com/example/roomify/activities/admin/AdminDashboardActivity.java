package com.example.roomify.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomify.activities.user.LogInActivity;
import com.example.roomify.R;
import com.example.roomify.adapters.ActivityAdapter;
import com.example.roomify.models.ActivityItem;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView totalBookingsText, availableRoomsText, totalRevenueText, newUsersText;
    private MaterialCardView manageRoomsCard, manageBookingsCard, manageUsersCard, managePaymentsCard;
    private RecyclerView recentActivityList;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize UI components
        initializeViews();
        setupToolbar();
        setupNavigationDrawer();
        setupActionCards();
        setupRecentActivityList();
        setupFloatingActionButton();

        // Set mock data (replace with real data from Firebase/Backend)
        setMockData();
    }

    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        // Dashboard stats
        totalBookingsText = findViewById(R.id.total_bookings);
        availableRoomsText = findViewById(R.id.available_rooms);
        totalRevenueText = findViewById(R.id.total_revenue);
        newUsersText = findViewById(R.id.new_users);

        // Action cards
        manageRoomsCard = findViewById(R.id.card_manage_rooms);
        manageBookingsCard = findViewById(R.id.card_manage_bookings);
        manageUsersCard = findViewById(R.id.card_manage_users);
        managePaymentsCard = findViewById(R.id.card_manage_payments);

        // Recent activity
        recentActivityList = findViewById(R.id.recent_activity_list);

        // Floating action button for adding new room
        fabAdd = findViewById(R.id.fab_add);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        // Set up the toggle for the drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupActionCards() {
        manageRoomsCard.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ManageRoomsActivity.class);
            startActivity(intent);
        });

        manageBookingsCard.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ManageBookingsActivity.class);
            startActivity(intent);
        });

        manageUsersCard.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ManageUsersActivity.class);
            startActivity(intent);
        });

        managePaymentsCard.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ManagePaymentsActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecentActivityList() {
        // Set up RecyclerView with LinearLayoutManager
        recentActivityList.setLayoutManager(new LinearLayoutManager(this));

        // Create and set adapter with mock data
        List<ActivityItem> activityItems = new ArrayList<>();
        activityItems.add(new ActivityItem("Room 102 booked", "John Doe - 2 hours ago", ActivityItem.TYPE_BOOKING));
        activityItems.add(new ActivityItem("Payment received", "$230.00 - 4 hours ago", ActivityItem.TYPE_PAYMENT));
        activityItems.add(new ActivityItem("New user registered", "Jane Smith - 6 hours ago", ActivityItem.TYPE_USER));

        ActivityAdapter adapter = new ActivityAdapter(activityItems);
        recentActivityList.setAdapter(adapter);
    }

    private void setupFloatingActionButton() {
        // When the FAB is clicked, launch AddRoomActivity
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AddRoomActivity.class);
            startActivity(intent);
        });
    }

    private void setMockData() {
        totalBookingsText.setText("150");
        availableRoomsText.setText("42");
        totalRevenueText.setText("$12,450");
        newUsersText.setText("24");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            // Already on dashboard, do nothing
        } else if (id == R.id.nav_rooms) {
            startActivity(new Intent(this, ManageRoomsActivity.class));
        } else if (id == R.id.nav_bookings) {
            startActivity(new Intent(this, ManageBookingsActivity.class));
        } else if (id == R.id.nav_users) {
            startActivity(new Intent(this, ManageUsersActivity.class));
        } else if (id == R.id.nav_payments) {
            startActivity(new Intent(this, ManagePaymentsActivity.class));
        } else if (id == R.id.nav_reports) {
            startActivity(new Intent(this, ReportsActivity.class));
        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(this, LogInActivity.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
