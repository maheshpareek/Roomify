package com.example.roomify.activities.admin;

<<<<<<< HEAD
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
=======
import android.os.Bundle;
>>>>>>> upstream/main
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

<<<<<<< HEAD
import androidx.annotation.NonNull;
=======
>>>>>>> upstream/main
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomify.R;
import com.example.roomify.adapters.BookingAdapter;
<<<<<<< HEAD
import com.example.roomify.models.Booking;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
=======
import com.example.roomify.models.Booking;  // Fixed import
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
>>>>>>> upstream/main
import java.util.List;

public class ManageBookingsActivity extends AppCompatActivity implements BookingAdapter.OnBookingClickListener {

    private RecyclerView bookingsRecyclerView;
    private FirebaseFirestore db;
    private List<Booking> bookingList;
    private BookingAdapter adapter;
<<<<<<< HEAD
    private FloatingActionButton fabAddBooking;
=======
>>>>>>> upstream/main

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bookings);

<<<<<<< HEAD
        // Set up Toolbar
=======
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
>>>>>>> upstream/main
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage Bookings");
        }

<<<<<<< HEAD
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView
=======
>>>>>>> upstream/main
        bookingsRecyclerView = findViewById(R.id.bookings_recycler_view);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize booking list and adapter
        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(bookingList, this);
        bookingsRecyclerView.setAdapter(adapter);

<<<<<<< HEAD
        // Floating Action Button for adding booking (if needed)
        fabAddBooking = findViewById(R.id.fab_add_booking);
        if (fabAddBooking != null) {
            fabAddBooking.setOnClickListener(v -> {
=======
        FloatingActionButton addBookingFab = findViewById(R.id.fab_add_booking);
        if (addBookingFab != null) {
            addBookingFab.setOnClickListener(v -> {
                // Show dialog to add new booking or navigate to add booking screen
>>>>>>> upstream/main
                Toast.makeText(this, "Add new booking functionality to be implemented", Toast.LENGTH_SHORT).show();
            });
        }

<<<<<<< HEAD
        // Real-time load of "bookings" from Firestore
=======
        // Load bookings
>>>>>>> upstream/main
        loadBookings();
    }

    private void loadBookings() {
<<<<<<< HEAD
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
=======
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
>>>>>>> upstream/main
    }

    @Override
    public void onBookingClick(Booking booking, int position) {
<<<<<<< HEAD
=======
        // Show booking details or open edit booking screen
>>>>>>> upstream/main
        Toast.makeText(this, "Booking for Room " + booking.getRoomNumber(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookingOptionsClick(Booking booking, int position, View view) {
<<<<<<< HEAD
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
=======
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

>>>>>>> upstream/main
        popup.show();
    }

    private void deleteBooking(Booking booking, int position) {
<<<<<<< HEAD
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
=======
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
>>>>>>> upstream/main
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> upstream/main
