package com.example.libraryapp_madca2.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.libraryapp_madca2.classes.User;

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

    public void insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        String createUserStatement = "INSERT INTO User VALUES('" +
                user.getEmail() + "', '" + user.getPassword() +
                "', '" + user.getName() + "', '" + user.getDob() + "')";
        try {
            db.execSQL(createUserStatement);
        } catch (SQLException e) {
            Toast.makeText(context,
                    "Error in inserting data into DB",
                    Toast.LENGTH_LONG
                    ).show();
        }
        db.close();
    }
    public User findUser(String email) {
        String password, name, dob;
        SQLiteDatabase db = getReadableDatabase();

        String findUserStatement = "SELECT * FROM User WHERE Email = '" + email + "'";

        Cursor cursor = db.rawQuery(findUserStatement, null);
        if (cursor.moveToFirst()) {
            password = cursor.getString(1);
            name = cursor.getString(2);
            dob = cursor.getString(3);
        }
        else {
            return null;
        }

        cursor.close();
        db.close();
        return new User(email, password, name, dob);
    }

    public void updateUser(String email, String name, String dob) {
        SQLiteDatabase db = getReadableDatabase();

        String updateUserStatement = "UPDATE User SET Name = '" +
                name +"', Dob = '" + dob + "' WHERE Email = '" +
                email + "'";

        try {
            db.execSQL(updateUserStatement);
        } catch (SQLException e) {
            Toast.makeText(context,
                    "Error in updating user data",
                    Toast.LENGTH_LONG
            ).show();
        }
        db.close();
    }
}
