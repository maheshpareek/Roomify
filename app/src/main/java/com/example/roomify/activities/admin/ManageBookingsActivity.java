package com.example.roomify.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomify.R;
import com.example.roomify.adapters.BookingAdapter;
import com.example.roomify.models.Booking;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageBookingsActivity extends AppCompatActivity implements BookingAdapter.OnBookingClickListener {

    private RecyclerView bookingsRecyclerView;
    private FirebaseFirestore db;
    private List<Booking> bookingList;
    private BookingAdapter adapter;
    private FloatingActionButton fabAddBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bookings);

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage Bookings");
        }

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        bookingsRecyclerView = findViewById(R.id.bookings_recycler_view);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize booking list and adapter
        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(bookingList, this);
        bookingsRecyclerView.setAdapter(adapter);

        // Floating Action Button for adding booking (if needed)
        fabAddBooking = findViewById(R.id.fab_add_booking);
        if (fabAddBooking != null) {
            fabAddBooking.setOnClickListener(v -> {
                Toast.makeText(this, "Add new booking functionality to be implemented", Toast.LENGTH_SHORT).show();
            });
        }

        // Real-time load of "bookings" from Firestore
        loadBookings();
    }

    private void loadBookings() {
        CollectionReference bookingsRef = db.collection("bookings");
        bookingsRef.addSnapshotListener((QuerySnapshot querySnapshot, FirebaseFirestoreException e) -> {
            if (e != null) {
                Toast.makeText(ManageBookingsActivity.this,
                        "Error loading bookings: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("ManageBookingsActivity", "Listen failed.", e);
                return;
            }
            if (querySnapshot != null) {
                bookingList.clear();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Booking booking = document.toObject(Booking.class);
                    booking.setId(document.getId());
                    bookingList.add(booking);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBookingClick(Booking booking, int position) {
        Toast.makeText(this, "Booking for Room " + booking.getRoomNumber(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookingOptionsClick(Booking booking, int position, View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.inflate(R.menu.booking_options_menu);
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_edit_booking) {
                Toast.makeText(this, "Edit booking for Room " + booking.getRoomNumber(), Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.action_delete_booking) {
                deleteBooking(booking, position);
                return true;
            } else if (id == R.id.action_change_status) {
                changeBookingStatus(booking, position);
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void deleteBooking(Booking booking, int position) {
        db.collection("bookings").document(booking.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Booking deleted", Toast.LENGTH_SHORT).show();
                    // The snapshot listener will update the list automatically.
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error deleting booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Updates the booking status.
     * For booked rooms, we allow only two status changes:
     * "booked" -> "checked_in" and "checked_in" -> "checked_out".
     * When changed to "checked_out", we also update the room's isAvailable to true
     * and schedule deletion of the booking doc after a delay.
     */
    private void changeBookingStatus(Booking booking, int position) {
        String currentStatus = booking.getStatus();
        String newStatus;
        if ("booked".equals(currentStatus)) {
            newStatus = "checked_in";
        } else if ("checked_in".equals(currentStatus)) {
            newStatus = "checked_out";
        } else {
            Toast.makeText(this, "Cannot change status from: " + currentStatus, Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("bookings").document(booking.getId())
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ManageBookingsActivity.this, "Status updated to: " + newStatus, Toast.LENGTH_SHORT).show();
                    // If the new status is "checked_out", update the room and schedule deletion.
                    if ("checked_out".equals(newStatus)) {
                        // Update corresponding room in "rooms" to set isAvailable to true.
                        db.collection("rooms").document(booking.getRoomId())
                                .update("isAvailable", true)
                                .addOnSuccessListener(aVoid2 -> {
                                    Toast.makeText(ManageBookingsActivity.this, "Room marked as available", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ManageBookingsActivity.this, "Error updating room: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                        // Delay deletion of the booking doc (e.g., 5 seconds)
                        new Handler().postDelayed(() -> {
                            db.collection("bookings").document(booking.getId())
                                    .delete()
                                    .addOnSuccessListener(aVoid3 -> {
                                        Toast.makeText(ManageBookingsActivity.this, "Booking removed", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(ManageBookingsActivity.this, "Error deleting booking: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }, 5000);
                    }
                    // Snapshot listener will refresh the list automatically.
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ManageBookingsActivity.this, "Error updating status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
