package com.example.roomify.activities.admin;

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

                Toast.makeText(this, "Add new booking functionality to be implemented", Toast.LENGTH_SHORT).show();
            });
        }

        loadBookings();
    }

    private void loadBookings() {
    }

    @Override
    public void onBookingClick(Booking booking, int position) {
        Toast.makeText(this, "Booking for Room " + booking.getRoomNumber(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookingOptionsClick(Booking booking, int position, View view) {
        popup.show();
    }

    private void deleteBooking(Booking booking, int position) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
