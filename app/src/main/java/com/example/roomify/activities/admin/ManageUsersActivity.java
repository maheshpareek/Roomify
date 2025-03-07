package com.example.roomify.activities.admin;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomify.R;
import com.example.roomify.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {

    private RecyclerView usersRecyclerView;
    private FirebaseFirestore db;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Manage Users");

        usersRecyclerView = findViewById(R.id.users_recycler_view);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton addUserFab = findViewById(R.id.fab_add_user);
        addUserFab.setOnClickListener(v -> {
            // Show dialog to add new user or navigate to add user screen
            Toast.makeText(this, "Add new user functionality to be implemented", Toast.LENGTH_SHORT).show();
        });

        // Load users
        loadUsers();
    }

    private void loadUsers() {
        userList = new ArrayList<>();

        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            userList.add(user);
                        }

                        // Create and set adapter (You'll need to create this adapter)
                        // UserAdapter adapter = new UserAdapter(userList);
                        // usersRecyclerView.setAdapter(adapter);

                        // For now, just show a toast with the count
                        Toast.makeText(this, "Loaded " + userList.size() + " users", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error loading users: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}