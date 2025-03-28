package com.example.roomify.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomify.R;
import com.example.roomify.adapters.RecentActivityAdapter;
import com.example.roomify.models.ActivityItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView userNameTextView;
    private TextView totalBookingsTextView;
    private TextView rewardPointsTextView;
    private TextView activeBookingsTextView;
    private TextView memberStatusTextView;
    private RecyclerView recentActivityRecyclerView;
    private RecentActivityAdapter recentActivityAdapter;
    private CardView cardSearchRooms, cardBookRoom, cardReportProblem, cardFeedback;
    private CardView cardAboutUs, cardContactUs;
    private FloatingActionButton fabSupport;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        initializeUI();

        // Set up the toolbar and drawer
        setupToolbarAndDrawer();

        // Load user data
        loadUserData();

        // Set up click listeners
        setupClickListeners();

        // Load recent activity
        loadRecentActivity();
    }

    private void initializeUI() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        userNameTextView = findViewById(R.id.user_name);
        totalBookingsTextView = findViewById(R.id.total_bookings);
        rewardPointsTextView = findViewById(R.id.reward_points);
        activeBookingsTextView = findViewById(R.id.active_bookings);
        memberStatusTextView = findViewById(R.id.member_status);
        recentActivityRecyclerView = findViewById(R.id.recent_activity_list);

        // Cards
        cardSearchRooms = findViewById(R.id.card_search_rooms);
        cardBookRoom = findViewById(R.id.card_book_room);
        cardReportProblem = findViewById(R.id.card_report_problem);
        cardFeedback = findViewById(R.id.card_feedback);
        cardAboutUs = findViewById(R.id.card_about_us);
        cardContactUs = findViewById(R.id.card_contact_us);

        // FAB
        fabSupport = findViewById(R.id.fab_support);

        // Set up RecyclerView
        recentActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentActivityAdapter = new RecentActivityAdapter(new ArrayList<>());
        recentActivityRecyclerView.setAdapter(recentActivityAdapter);
    }

    private void setupToolbarAndDrawer() {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Update navigation header with user info
        if (currentUser != null) {
            // Comment out or remove these lines if you don't have these views in your nav_header layout
            /*
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = headerView.findViewById(R.id.nav_header_username);
            TextView navEmail = headerView.findViewById(R.id.nav_header_email);

            if (navUsername != null && navEmail != null) {
                navEmail.setText(currentUser.getEmail());
            }
            */
        }
    }

    private void loadUserData() {
        if (currentUser != null) {
            // Load user name for toolbar
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String firstName = documentSnapshot.getString("firstName");
                            String lastName = documentSnapshot.getString("lastName");
                            String fullName = "";
                            if (firstName != null) {
                                fullName += firstName;
                            }
                            if (lastName != null && !lastName.isEmpty()) {
                                fullName += " " + lastName;
                            }
                            if (!fullName.isEmpty()) {
                                userNameTextView.setText(fullName);

                                // Comment out or remove these lines if you don't have these views in your nav_header layout
                                /*
                                View headerView = navigationView.getHeaderView(0);
                                if (headerView != null) {
                                    TextView navUsername = headerView.findViewById(R.id.nav_header_username);
                                    if (navUsername != null) {
                                        navUsername.setText(fullName);
                                    }
                                }
                                */
                            } else {
                                userNameTextView.setText("Guest");
                            }

                            // Load other user data
                            Integer rewardPoints = documentSnapshot.getLong("rewardPoints") != null ?
                                    documentSnapshot.getLong("rewardPoints").intValue() : 0;
                            String memberStatus = documentSnapshot.getString("memberStatus");

                            rewardPointsTextView.setText(String.valueOf(rewardPoints));
                            if (memberStatus != null && !memberStatus.isEmpty()) {
                                memberStatusTextView.setText(memberStatus);
                            } else {
                                memberStatusTextView.setText("Standard");
                            }
                        } else {
                            userNameTextView.setText("Guest");
                        }
                    })
                    .addOnFailureListener(e -> userNameTextView.setText("Guest"));

            // Load booking statistics
            db.collection("bookings")
                    .whereEqualTo("userId", currentUser.getUid())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        int totalBookings = queryDocumentSnapshots.size();
                        int activeBookings = 0;

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String status = document.getString("status");
                            if (status != null && (status.equals("active") || status.equals("confirmed"))) {
                                activeBookings++;
                            }
                        }

                        totalBookingsTextView.setText(String.valueOf(totalBookings));
                        activeBookingsTextView.setText(String.valueOf(activeBookings));
                    })
                    .addOnFailureListener(e -> {
                        totalBookingsTextView.setText("0");
                        activeBookingsTextView.setText("0");
                    });
        } else {
            userNameTextView.setText("Guest");
            totalBookingsTextView.setText("0");
            rewardPointsTextView.setText("0");
            activeBookingsTextView.setText("0");
            memberStatusTextView.setText("Standard");
        }
    }

    private void setupClickListeners() {
        // Quick action cards
        cardSearchRooms.setOnClickListener(v -> {
            Toast.makeText(this, "Search Rooms clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create and navigate to SearchRoomsActivity
            // startActivity(new Intent(UserDashboardActivity.this, SearchRoomsActivity.class));
        });

        cardBookRoom.setOnClickListener(v -> {
            Toast.makeText(this, "Book Room clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create and navigate to BookRoomActivity
            // startActivity(new Intent(UserDashboardActivity.this, BookRoomActivity.class));
        });

        cardReportProblem.setOnClickListener(v -> {
            Toast.makeText(this, "Report Problem clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create and navigate to ReportProblemActivity
            // startActivity(new Intent(UserDashboardActivity.this, ReportProblemActivity.class));
        });

        cardFeedback.setOnClickListener(v -> {
            Toast.makeText(this, "Submit Feedback clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create and navigate to FeedbackActivity
            // startActivity(new Intent(UserDashboardActivity.this, FeedbackActivity.class));
        });

        // Info cards
        cardAboutUs.setOnClickListener(v -> {
            Toast.makeText(this, "About Us clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create and navigate to AboutUsActivity
            // startActivity(new Intent(UserDashboardActivity.this, AboutUsActivity.class));
        });

        cardContactUs.setOnClickListener(v -> {
            Toast.makeText(this, "Contact Us clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create and navigate to ContactUsActivity
            // startActivity(new Intent(UserDashboardActivity.this, ContactUsActivity.class));
        });

        // FAB for support chat
        fabSupport.setOnClickListener(v -> {
            Toast.makeText(this, "Support Chat clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create and navigate to SupportChatActivity
            // startActivity(new Intent(UserDashboardActivity.this, SupportChatActivity.class));
        });
    }

    private void loadRecentActivity() {
        if (currentUser != null) {
            List<ActivityItem> activityItems = new ArrayList<>();

            // Get recent bookings
            db.collection("bookings")
                    .whereEqualTo("userId", currentUser.getUid())
                    .orderBy("timestamp")
                    .limit(5)
                    .get()
                    .addOnSuccessListener(bookingSnapshots -> {
                        for (QueryDocumentSnapshot document : bookingSnapshots) {
                            String title = "Room Booking";
                            String description = "You booked room " + document.getString("roomNumber");
                            String timestamp = document.getDate("timestamp") != null ?
                                    document.getDate("timestamp").toString() : "";

                            activityItems.add(new ActivityItem(title, description, timestamp, R.drawable.ic_bookings));
                        }

                        // Get recent payments
                        db.collection("payments")
                                .whereEqualTo("userId", currentUser.getUid())
                                .orderBy("timestamp")
                                .limit(3)
                                .get()
                                .addOnSuccessListener(paymentSnapshots -> {
                                    for (QueryDocumentSnapshot document : paymentSnapshots) {
                                        String title = "Payment";
                                        String amount = document.getDouble("amount") != null ?
                                                "$" + document.getDouble("amount").toString() : "";
                                        String description = "You made a payment of " + amount;
                                        String timestamp = document.getDate("timestamp") != null ?
                                                document.getDate("timestamp").toString() : "";

                                        activityItems.add(new ActivityItem(title, description, timestamp, R.drawable.ic_payment));
                                    }

                                    // Update adapter with all items
                                    recentActivityAdapter.updateData(activityItems);
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Show a placeholder or message if there's an error
                        activityItems.add(new ActivityItem("No Recent Activity",
                                "Your recent activities will appear here", "", R.drawable.ic_info));
                        recentActivityAdapter.updateData(activityItems);
                    });
        } else {
            // Show a sign-in prompt for guest users
            List<ActivityItem> activityItems = new ArrayList<>();
            activityItems.add(new ActivityItem("Sign In Required",
                    "Please sign in to see your recent activities", "", R.drawable.ic_info));
            recentActivityAdapter.updateData(activityItems);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Already on home screen, just close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_profile) {
            Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create and navigate to ProfileActivity
            // startActivity(new Intent(UserDashboardActivity.this, ProfileActivity.class));
        } else if (id == R.id.nav_bookings) {
            Toast.makeText(this, "My Bookings clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create and navigate to MyBookingsActivity
            // startActivity(new Intent(UserDashboardActivity.this, MyBookingsActivity.class));
        } else if (id == R.id.nav_search) {
            Toast.makeText(this, "Search Rooms clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create and navigate to SearchRoomsActivity
            // startActivity(new Intent(UserDashboardActivity.this, SearchRoomsActivity.class));
        } else if (id == R.id.nav_report_problem) {
            Toast.makeText(this, "Report Problem clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create and navigate to ReportProblemActivity
            // startActivity(new Intent(UserDashboardActivity.this, ReportProblemActivity.class));
        } else if (id == R.id.nav_feedback) {
            Toast.makeText(this, "Submit Feedback clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create and navigate to FeedbackActivity
            // startActivity(new Intent(UserDashboardActivity.this, FeedbackActivity.class));
        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "About Us clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create and navigate to AboutUsActivity
            // startActivity(new Intent(UserDashboardActivity.this, AboutUsActivity.class));
        } else if (id == R.id.nav_contact) {
            Toast.makeText(this, "Contact Us clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create and navigate to ContactUsActivity
            // startActivity(new Intent(UserDashboardActivity.this, ContactUsActivity.class));
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            // TODO: Create SettingsActivity if it doesn't exist
            // startActivity(new Intent(UserDashboardActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_logout) {
            // Sign out the user
            FirebaseAuth.getInstance().signOut();
            // Clear the activity stack and start the login activity
            Intent loginIntent = new Intent(UserDashboardActivity.this, LogInActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        // Close drawer first if it's open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Helper class for RecentActivityAdapter if needed
    public static class ActivityItem {
        private final String title;
        private final String description;
        private final String timestamp;
        private final int iconResId;

        public ActivityItem(String title, String description, String timestamp, int iconResId) {
            this.title = title;
            this.description = description;
            this.timestamp = timestamp;
            this.iconResId = iconResId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public int getIconResId() {
            return iconResId;
        }
    }
}