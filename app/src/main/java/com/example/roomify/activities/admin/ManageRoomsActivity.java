package com.example.roomify.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomify.R;
import com.example.roomify.activities.user.LogInActivity;
import com.example.roomify.adapters.RoomAdapter;
import com.example.roomify.models.Room;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ManageRoomsActivity (manual approach for creating bookings).
 * Whenever we toggle a room's isAvailable from true→false,
 * we create a doc in "bookings". If toggling false→true,
 * we remove that doc.
 */
public class ManageRoomsActivity extends AppCompatActivity implements RoomAdapter.OnRoomClickListener {

    private RecyclerView roomsRecyclerView;
    private RoomAdapter roomAdapter;
    private List<Room> roomList;
    private FirebaseFirestore db;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_rooms);

        roomsRecyclerView = findViewById(R.id.rooms_recycler_view);
        roomsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAdd = findViewById(R.id.fab_add_room);
        fabAdd.setOnClickListener(v -> {
            // Launch AddRoomActivity
            startActivity(new Intent(ManageRoomsActivity.this, AddRoomActivity.class));
        });

        roomList = new ArrayList<>();
        // Pass 'this' as the listener to handle room clicks/options
        roomAdapter = new RoomAdapter(roomList, this);
        roomsRecyclerView.setAdapter(roomAdapter);

        db = FirebaseFirestore.getInstance();

        // Real-time listener for "rooms" collection
        loadRooms();
    }

    private void loadRooms() {
        CollectionReference roomsRef = db.collection("rooms");
        roomsRef.addSnapshotListener((QuerySnapshot querySnapshot, FirebaseFirestoreException e) -> {
            if (e != null) {
                Toast.makeText(ManageRoomsActivity.this, "Error loading rooms: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ManageRoomsActivity", "Listen failed.", e);
                return;
            }
            if (querySnapshot != null) {
                roomList.clear();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Room room = document.toObject(Room.class);
                    room.setId(document.getId());
                    roomList.add(room);
                }
                roomAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRoomClick(Room room, int position) {
        // If you want to show room details on click
        Toast.makeText(this, "Clicked on Room: " + room.getRoomNumber(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoomOptionsClick(Room room, int position, View view) {
        // Show popup menu with toggle availability, delete, etc.
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.room_options_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_edit_room) {
                Toast.makeText(this, "Edit Room (not implemented)", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.action_toggle_availability) {
                toggleAvailability(room, position);
                return true;
            } else if (id == R.id.action_delete_room) {
                deleteRoom(room, position);
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void toggleAvailability(Room room, int position) {
        boolean newAvailability = !room.isAvailable();

        // 1) Update the "rooms" doc isAvailable
        db.collection("rooms").document(room.getId())
                .update("isAvailable", newAvailability)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ManageRoomsActivity.this,
                            "Room availability set to " + newAvailability, Toast.LENGTH_SHORT).show();

                    // 2) If now booked => create doc in "bookings", else remove doc
                    if (!newAvailability) {
                        // was true => now false => create booking doc
                        createBookingDoc(room);
                    } else {
                        // was false => now true => remove booking doc
                        removeBookingDoc(room.getId());
                    }

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ManageRoomsActivity.this, "Failed to toggle availability: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void createBookingDoc(Room room) {
        // We'll store minimal fields, add more if needed
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("roomId", room.getId());
        bookingData.put("roomNumber", room.getRoomNumber());
        bookingData.put("status", "booked"); // or "confirmed"
        bookingData.put("updatedAt", FieldValue.serverTimestamp());

        db.collection("bookings").document(room.getId())
                .set(bookingData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ManageRoomsActivity.this, "Booking doc created", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ManageRoomsActivity.this, "Failed to create booking doc: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void removeBookingDoc(String roomId) {
        db.collection("bookings").document(roomId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ManageRoomsActivity.this, "Booking doc removed", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ManageRoomsActivity.this, "Failed to remove booking doc: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteRoom(Room room, int position) {
        // If you want a delete room feature
        db.collection("rooms").document(room.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ManageRoomsActivity.this, "Room deleted", Toast.LENGTH_SHORT).show();
                    // snapshot listener will auto-update
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ManageRoomsActivity.this, "Error deleting room: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
