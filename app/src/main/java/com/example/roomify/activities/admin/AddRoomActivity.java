package com.example.roomify.activities.admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.roomify.R;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddRoomActivity extends AppCompatActivity {

    private EditText roomNumberInput;
    private EditText roomPriceInput;
    private Spinner roomTypeSpinner;
    private EditText roomDescriptionInput;
    private Button addRoomButton;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New Room");

        roomNumberInput = findViewById(R.id.room_number_input);
        roomPriceInput = findViewById(R.id.room_price_input);
        roomTypeSpinner = findViewById(R.id.room_type_spinner);
        roomDescriptionInput = findViewById(R.id.room_description_input);
        addRoomButton = findViewById(R.id.add_room_button);

        // Set up click listener for Add Room button
        addRoomButton.setOnClickListener(v -> validateAndAddRoom());
    }

    private void validateAndAddRoom() {
        String roomNumber = roomNumberInput.getText().toString().trim();
        String roomPriceStr = roomPriceInput.getText().toString().trim();
        String roomType = roomTypeSpinner.getSelectedItem().toString();
        String description = roomDescriptionInput.getText().toString().trim();

        // Validate input fields
        if (roomNumber.isEmpty() || roomPriceStr.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double roomPrice;
        try {
            roomPrice = Double.parseDouble(roomPriceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new room map
        Map<String, Object> room = new HashMap<>();
        room.put("roomNumber", roomNumber);
        room.put("price", roomPrice);
        room.put("type", roomType);
        room.put("description", description);
        room.put("isAvailable", true);

        // Add the room to Firestore
        db.collection("rooms")
                .add(room)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddRoomActivity.this, "Room added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity and go back
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddRoomActivity.this, "Error adding room: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
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