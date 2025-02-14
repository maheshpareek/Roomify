package com.example.roomify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "roomify.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper instance; // SINGLETON INSTANCE

    // User Table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USER_NAME = "name";
    private static final String COL_USER_EMAIL = "email";
    private static final String COL_USER_PASSWORD = "password";

    // Admin Table
    private static final String TABLE_ADMINS = "admins";
    private static final String COL_ADMIN_ID = "id";
    private static final String COL_ADMIN_NAME = "name";
    private static final String COL_ADMIN_EMAIL = "email";
    private static final String COL_ADMIN_PASSWORD = "password";
    private static final String COL_ADMIN_ROLE = "role";

    // Singleton Pattern to Prevent Multiple Instances
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "Creating Roomify Database...");

        String createUserTable = "CREATE TABLE " + TABLE_USERS + " ("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USER_NAME + " TEXT NOT NULL, "
                + COL_USER_EMAIL + " TEXT UNIQUE NOT NULL, "
                + COL_USER_PASSWORD + " TEXT NOT NULL);";
        db.execSQL(createUserTable);

        String createAdminTable = "CREATE TABLE " + TABLE_ADMINS + " ("
                + COL_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_ADMIN_NAME + " TEXT NOT NULL, "
                + COL_ADMIN_EMAIL + " TEXT UNIQUE NOT NULL, "
                + COL_ADMIN_PASSWORD + " TEXT NOT NULL, "
                + COL_ADMIN_ROLE + " TEXT NOT NULL);";
        db.execSQL(createAdminTable);

        Log.d("DatabaseHelper", "✅ Database Created Successfully!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "Upgrading Database...");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMINS);
        onCreate(db);
    }

    // Method to check if the database already exists
    public boolean doesDatabaseExist(Context context) {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    // Method to add User (with password hashing)
    public boolean addUser(String name, String email, String password) {
        if (checkEmailExists(email)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_EMAIL, email);

        // Assign "GoogleAuth" as password placeholder for Google users
        values.put(COL_USER_PASSWORD, password.equals("GoogleAuth") ? "GoogleAuth" : hashPassword(password));

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }



    // Method to add Admin (with password hashing)
    public boolean addAdmin(String name, String email, String password, String role) {
        if (checkEmailExists(email)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ADMIN_NAME, name);
        values.put(COL_ADMIN_EMAIL, email);
        values.put(COL_ADMIN_PASSWORD, hashPassword(password));
        values.put(COL_ADMIN_ROLE, role);

        long result = db.insert(TABLE_ADMINS, null, values);
        return result != -1;
    }

    // Method to check if the email already exists in either table
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean exists = false;

        Cursor cursorUser = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USER_EMAIL + "=?", new String[]{email});
        if (cursorUser.getCount() > 0) exists = true;
        cursorUser.close();

        Cursor cursorAdmin = db.rawQuery("SELECT * FROM " + TABLE_ADMINS + " WHERE " + COL_ADMIN_EMAIL + "=?", new String[]{email});
        if (cursorAdmin.getCount() > 0) exists = true;
        cursorAdmin.close();

        return exists;
    }

    // Method to check User login (compares hashed password)
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE "
                + COL_USER_EMAIL + "=? AND " + COL_USER_PASSWORD + "=?", new String[]{email, hashPassword(password)});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Method to check Admin login (compares hashed password)
    public boolean checkAdmin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ADMINS + " WHERE "
                + COL_ADMIN_EMAIL + "=? AND " + COL_ADMIN_PASSWORD + "=?", new String[]{email, hashPassword(password)});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Retrieve Admin Role for further use in the app
    public String getAdminRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_ADMIN_ROLE + " FROM " + TABLE_ADMINS + " WHERE " + COL_ADMIN_EMAIL + "=?", new String[]{email});

        String role = null;
        if (cursor.moveToFirst()) {
            role = cursor.getString(0);
        }
        cursor.close();
        return role;
    }

    // Password Hashing using SHA-256 for security
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("DatabaseHelper", "Hashing Error: " + e.getMessage());
            return password; // Fallback (Not Recommended)
        }
    }
}
