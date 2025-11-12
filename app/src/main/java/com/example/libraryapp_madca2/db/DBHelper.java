package com.example.libraryapp_madca2.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    Context context;
    public static final String DATABASE_NAME = "BookDB";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTableStatement = "CREATE TABLE User(Email TEXT PRIMARY KEY, Password TEXT, Name TEXT, Dob TEXT)";
        db.execSQL(createUserTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String insertStatement) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(insertStatement);
        } catch (SQLException e) {
            Toast.makeText(context,
                    "Error in inserting data into DB",
                    Toast.LENGTH_LONG
                    ).show();
        }
        db.close();
    }
}
