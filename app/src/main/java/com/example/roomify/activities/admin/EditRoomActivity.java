package com.example.roomify.activities.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.roomify.R;
import com.example.roomify.models.Room;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditRoomActivity extends AppCompatActivity {

    private EditText roomNumberInput, priceInput, typeInput, descriptionInput;
    private Button saveButton;
    private FirebaseFirestore db;
    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_room);

        db = FirebaseFirestore.getInstance();

        // Retrieve roomId from intent
        roomId = getIntent().getStringExtra("roomId");

        roomNumberInput = findViewById(R.id.roomNumberInput);
        priceInput = findViewById(R.id.priceInput);
        typeInput = findViewById(R.id.typeInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        saveButton = findViewById(R.id.saveButton);

        loadRoomDetails();

        saveButton.setOnClickListener(v -> updateRoomDetails());
    }

    private void loadRoomDetails() {
        db.collection("rooms").document(roomId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Room room = documentSnapshot.toObject(Room.class);
                        if (room != null) {
                            // Populate fields
                            roomNumberInput.setText(room.getRoomNumber());
                            priceInput.setText(String.valueOf(room.getPrice()));
                            typeInput.setText(room.getType());
                            descriptionInput.setText(room.getDescription());
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load room data", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateRoomDetails() {
        String newNumber = roomNumberInput.getText().toString().trim();
        double newPrice = Double.parseDouble(priceInput.getText().toString().trim());
        String newType = typeInput.getText().toString().trim();
        String newDesc = descriptionInput.getText().toString().trim();

        db.collection("rooms").document(roomId)
                .update(
                        "roomNumber", newNumber,
                        "price", newPrice,
                        "type", newType,
                        "description", newDesc
                )
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Room updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error updating room: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
