package com.example.roomify.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomify.R;
import com.example.roomify.adapters.UserAdapter;
import com.example.roomify.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays two lists of users: Admins vs Guests.
 * Each user has an options button that shows a popup with Edit/Delete.
 */
public class ManageUsersActivity extends AppCompatActivity {

    private RecyclerView adminUsersRecyclerView, guestUsersRecyclerView;
    private FirebaseFirestore db;
    private List<User> adminUserList, guestUserList;
    private UserAdapter adminAdapter, guestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage Users");
        }

        db = FirebaseFirestore.getInstance();

        // Prepare lists
        adminUserList = new ArrayList<>();
        guestUserList = new ArrayList<>();

        // Set up RecyclerViews
        adminUsersRecyclerView = findViewById(R.id.admin_users_recycler_view);
        guestUsersRecyclerView = findViewById(R.id.guest_users_recycler_view);
        adminUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        guestUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapters
        adminAdapter = new UserAdapter(adminUserList, (user, position, view) -> {
            showUserOptionsPopup(user, position, view);
        });
        guestAdapter = new UserAdapter(guestUserList, (user, position, view) -> {
            showUserOptionsPopup(user, position, view);
        });

        adminUsersRecyclerView.setAdapter(adminAdapter);
        guestUsersRecyclerView.setAdapter(guestAdapter);

        FloatingActionButton addUserFab = findViewById(R.id.fab_add_user);
        addUserFab.setOnClickListener(v -> {
            Toast.makeText(this, "Add new user not implemented", Toast.LENGTH_SHORT).show();
        });

        // Real-time load
        loadUsers();
    }

    private void loadUsers() {
        db.collection("users")
                .addSnapshotListener((QuerySnapshot querySnapshot, com.google.firebase.firestore.FirebaseFirestoreException error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading users: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    adminUserList.clear();
                    guestUserList.clear();
                    if (querySnapshot != null) {
                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            User user = doc.toObject(User.class);
                            user.setId(doc.getId());
                            if (user.getUserType() != null && user.getUserType().equalsIgnoreCase("admin")) {
                                adminUserList.add(user);
                            } else {
                                guestUserList.add(user);
                            }
                        }
                    }
                    adminAdapter.notifyDataSetChanged();
                    guestAdapter.notifyDataSetChanged();
                });
    }

    // EXACT signature: (User user, int position, View anchorView)
    private void showUserOptionsPopup(User user, int position, View anchorView) {
        android.widget.PopupMenu popup = new android.widget.PopupMenu(this, anchorView);
        popup.inflate(R.menu.user_options_menu);
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_edit_user) {
                // Launch your EditUserActivity or handle user editing
                Intent intent = new Intent(this, EditUserActivity.class);
                intent.putExtra("userId", user.getId());
                startActivity(intent);
                return true;
            } else if (id == R.id.action_delete_user) {
                deleteUser(user);
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void deleteUser(User user) {
        db.collection("users").document(user.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error deleting user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
