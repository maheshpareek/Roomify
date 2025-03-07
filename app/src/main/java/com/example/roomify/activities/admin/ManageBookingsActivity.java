package com.example.roomify.activities.admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomify.R;
import com.example.roomify.adapters.BookingAdapter;
import com.example.roomify.models.Booking;  // Fixed import
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManageBookingsActivity extends AppCompatActivity implements BookingAdapter.OnBookingClickListener {

    private RecyclerView bookingsRecyclerView;
    private FirebaseFirestore db;
    private List<Booking> bookingList;
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bookings);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage Bookings");
        }

        bookingsRecyclerView = findViewById(R.id.bookings_recycler_view);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize booking list and adapter
        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(bookingList, this);
        bookingsRecyclerView.setAdapter(adapter);

        FloatingActionButton addBookingFab = findViewById(R.id.fab_add_booking);
        if (addBookingFab != null) {
            addBookingFab.setOnClickListener(v -> {
                // Show dialog to add new booking or navigate to add booking screen
                Toast.makeText(this, "Add new booking functionality to be implemented", Toast.LENGTH_SHORT).show();
            });
        }

        // Load bookings
        loadBookings();
    }

    private void loadBookings() {
        // For now, just add some mock data
        // In a real app, this would fetch from Firestore
        addMockData();

        // Uncomment and adapt this code when you're ready to fetch from Firestore
        /*
        bookingList.clear();

        db.collection("bookings")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Booking booking = document.toObject(Booking.class);
                        booking.setId(document.getId());
                        bookingList.add(booking);
                    }

                    adapter.notifyDataSetChanged();

                    // For now, just show a toast with the count
                    Toast.makeText(this, "Loaded " + bookingList.size() + " bookings", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error loading bookings: " + task.getException().getMessage(),
                                  Toast.LENGTH_SHORT).show();
                }
            });
        */
    }

    private void addMockData() {
        // Add some mock bookings for testing
        Date now = new Date();

        // Mock booking 1
        Booking booking1 = new Booking("user123", "room101", "101", new Date(now.getTime() + 86400000),
                new Date(now.getTime() + 86400000 * 5), 499.95);
        booking1.setId("booking1");
        booking1.setStatus("confirmed");
        booking1.setGuestName("John Doe");
        bookingList.add(booking1);

        // Mock booking 2
        Booking booking2 = new Booking("user456", "room102", "102", new Date(now.getTime() + 86400000 * 7),
                new Date(now.getTime() + 86400000 * 10), 299.85);
        booking2.setId("booking2");
        booking2.setStatus("pending");
        booking2.setGuestName("Jane Smith");
        bookingList.add(booking2);

        // Mock booking 3
        Booking booking3 = new Booking("user789", "room103", "103", new Date(now.getTime() - 86400000 * 3),
                new Date(now.getTime() + 86400000 * 2), 699.50);
        booking3.setId("booking3");
        booking3.setStatus("checked_in");
        booking3.setGuestName("Robert Johnson");
        bookingList.add(booking3);

        adapter.notifyDataSetChanged();

        Toast.makeText(this, "Loaded " + bookingList.size() + " bookings (mock data)", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookingClick(Booking booking, int position) {
        // Show booking details or open edit booking screen
        Toast.makeText(this, "Booking for Room " + booking.getRoomNumber(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookingOptionsClick(Booking booking, int position, View view) {
        // Show popup menu with options (edit, delete, etc.)
        PopupMenu popup = new PopupMenu(this, view);
        popup.inflate(R.menu.booking_options_menu);

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_edit_booking) {
                // Navigate to edit booking screen
                Toast.makeText(this, "Edit booking for Room " + booking.getRoomNumber(), Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.action_delete_booking) {
                // Show confirmation dialog and delete booking
                deleteBooking(booking, position);
                return true;
            } else if (id == R.id.action_change_status) {
                // Show dialog to change booking status
                changeBookingStatus(booking, position);
                return true;
            }

            return false;
        });

        popup.show();
    }

    private void deleteBooking(Booking booking, int position) {
        // In a real app, you would delete from Firestore
        // For now, just remove from the list
        bookingList.remove(position);
        adapter.notifyItemRemoved(position);
        Toast.makeText(this, "Booking deleted", Toast.LENGTH_SHORT).show();
    }

    private void changeBookingStatus(Booking booking, int position) {
        // Cycle through statuses: pending -> confirmed -> checked_in -> checked_out
        String currentStatus = booking.getStatus();
        String newStatus;

        switch (currentStatus) {
            case "pending":
                newStatus = "confirmed";
                break;
            case "confirmed":
                newStatus = "checked_in";
                break;
            case "checked_in":
                newStatus = "checked_out";
                break;
            default:
                newStatus = "pending";
                break;
        }

        // Update status
        booking.setStatus(newStatus);
        adapter.notifyItemChanged(position);

        Toast.makeText(this, "Status updated to: " + newStatus, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}