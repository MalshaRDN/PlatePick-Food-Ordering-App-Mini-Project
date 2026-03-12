package com.example.platepick;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database and Table details
    public static final String DATABASE_NAME = "PlatePick.db";
    public static final String TABLE_NAME = "users";
    public static final String COL_2 = "USERNAME";
    public static final String COL_3 = "EMAIL";
    public static final String COL_4 = "PASSWORD";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the users table
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT, " + COL_3 + " TEXT, " + COL_4 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to insert a new user (Registration)
    public boolean insertData(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, email);
        contentValues.put(COL_4, password);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1; // If result is -1, data was not inserted
    }

    // Method to check if email and password match (Login)
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_3 + " = ? AND " + COL_4 + " = ?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Method to get the username based on email
    public String getUsername(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_2 + " FROM " + TABLE_NAME + " WHERE " + COL_3 + " = ?", new String[]{email});
        String username = "User"; // Default if not found
        if (cursor.moveToFirst()) {
            username = cursor.getString(0); // Return the name found in the first column
        }
        cursor.close();
        return username;
    }

    // Method to get full user profile details
    public String[] getUserProfile(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_2 + ", " + COL_3 + ", " + COL_4 + " FROM " + TABLE_NAME + " WHERE " + COL_3 + " = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            String username = cursor.getString(0);
            String userEmail = cursor.getString(1);
            String password = cursor.getString(2);
            cursor.close();
            return new String[]{username, userEmail, password};
        }

        cursor.close();
        return null;
    }

    // Method to update profile details
    public boolean updateUserProfile(String currentEmail, String username, String newEmail, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, newEmail);
        contentValues.put(COL_4, password);

        int updatedRows = db.update(TABLE_NAME, contentValues, COL_3 + " = ?", new String[]{currentEmail});
        return updatedRows > 0;
    }
}
