package com.example.libraryapp_madca2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.libraryapp_madca2.classes.Book;
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

        String createBookTableStatement = "CREATE TABLE Book(BookID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " Title TEXT, Author TEXT, Category TEXT, StartDate TEXT, Review TEXT, Status TEXT)";
        db.execSQL(createBookTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Handling User table
    public void insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        String createUserStatement = "INSERT INTO User VALUES('" +
                user.getEmail() + "', '" + user.getPassword() +
                "', '" + user.getName() + "', '" + user.getDob() + "')";
        try {
            db.execSQL(createUserStatement);
        } catch (SQLException e) {
            Toast.makeText(context,
                    "Error in inserting user into DB",
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

    //Handling Book table
    public void insertBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("Title", book.getTitle());
        cv.put("Author", book.getAuthor());
        cv.put("Category", book.getCategory());
        cv.put("StartDate", book.getStartDate());
        cv.put("Review", book.getReview());
        cv.put("Status", book.getStatus());

        long result = db.insert("Book", null, cv);
        if (result == -1) {
            Toast.makeText(context,
                    "Error in inserting book into DB",
                    Toast.LENGTH_LONG
            ).show();
        }
        else {
            Toast.makeText(context,
                    "Book successfully added to library",
                    Toast.LENGTH_SHORT
            ).show();
        }
        db.close();
    }

    public Cursor getAllBooks() {
        SQLiteDatabase db = this.getWritableDatabase();
        String getAllBooksStatement = "SELECT * FROM Book";

        Cursor cursor = null;
        cursor = db.rawQuery(getAllBooksStatement, null);
        return cursor;
    }

    public boolean bookExists(String title, String author) {
        SQLiteDatabase db = getWritableDatabase();

        String findUserStatement = "SELECT * FROM Book WHERE Title = ? AND Author = ?";

        Cursor cursor = db.rawQuery(findUserStatement, new String[] {title, author});
        return cursor.moveToFirst();
    }

    public Book findBook(int id) {
        String title, author, category, startDate, review, status;
        SQLiteDatabase db = getReadableDatabase();

        String findBookStatement = "SELECT * FROM Book WHERE BookID = " + id;

        Cursor cursor = db.rawQuery(findBookStatement, null);
        if (cursor.moveToFirst()) {
            title = cursor.getString(1);
            author = cursor.getString(2);
            category = cursor.getString(3);
            startDate = cursor.getString(4);
            review = cursor.getString(5);
            status = cursor.getString(6);
        }
        else {
            return null;
        }

        cursor.close();
        db.close();
        return new Book(id, title, author, category, startDate, review, status);
    }

    public void updateBook(int id, String status, String review) {
        SQLiteDatabase db = getReadableDatabase();

        String updateBookStatement = "UPDATE Book SET Status = '" +
                status +"', Review = '" + review + "' WHERE BookID = " +
                id;

        try {
            db.execSQL(updateBookStatement);
        } catch (SQLException e) {
            Toast.makeText(context,
                    "Error in updating book data",
                    Toast.LENGTH_LONG
            ).show();
        }
        db.close();
    }

}
