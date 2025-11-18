package com.example.libraryapp_madca2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
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
                " Title TEXT, Author TEXT, Category TEXT, StartDate TEXT, Review TEXT, Status TEXT, UserEmail TEXT, Favourite INT, FOREIGN KEY(UserEmail) REFERENCES User(EMAIL))";
        db.execSQL(createBookTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Handling User table
    public void insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("Email", user.getEmail());
        cv.put("Password", user.getPassword());
        cv.put("Name", user.getName());
        cv.put("Dob", user.getDob());

        long result = db.insert("User", null, cv);
        if (result == -1) {
            Toast.makeText(context,
                    "Error in inserting user into DB",
                    Toast.LENGTH_LONG
            ).show();
        }
        else {
            Toast.makeText(context,
                    "User successfully registered",
                    Toast.LENGTH_SHORT
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

        SQLiteStatement updateUserStatement = db.compileStatement("UPDATE User SET Name = ?, Dob = ? WHERE Email = ?");
        updateUserStatement.bindString(1, name);
        updateUserStatement.bindString(2, dob);
        updateUserStatement.bindString(3, email);
        try {
            updateUserStatement.execute();
        } catch (SQLException e) {
            Toast.makeText(context,
                    "Error in updating user data",
                    Toast.LENGTH_LONG
            ).show();
        }
        updateUserStatement.close();
        db.close();
    }

    //Handling Book table
    public void insertBook(Book book, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("Title", book.getTitle());
        cv.put("Author", book.getAuthor());
        cv.put("Category", book.getCategory());
        cv.put("StartDate", book.getStartDate());
        cv.put("Review", book.getReview());
        cv.put("Status", book.getStatus());
        cv.put("UserEmail", userEmail);

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

    public Cursor getAllBooks(String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        String getAllBooksStatement = "SELECT * FROM Book WHERE UserEmail = ?";

        Cursor cursor;
        cursor = db.rawQuery(getAllBooksStatement, new String[] {userEmail});
        return cursor;
    }

    public Cursor getFavouriteBooks(String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        String getAllBooksStatement = "SELECT * FROM Book WHERE UserEmail = ? AND Favourite = 1";

        Cursor cursor;
        cursor = db.rawQuery(getAllBooksStatement, new String[] {userEmail});
        return cursor;
    }

    public boolean bookExists(String title, String author, String userEmail) {
        SQLiteDatabase db = getWritableDatabase();

        String findUserStatement = "SELECT * FROM Book WHERE Title = ? AND Author = ? AND UserEmail = ?";

        Cursor cursor = db.rawQuery(findUserStatement, new String[] {title, author, userEmail});
        return cursor.moveToFirst();
    }

    public Book findBook(int id) {
        String title, author, category, startDate, review, status;
        int favourited;
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
            favourited = cursor.getInt(8);
        }
        else {
            return null;
        }

        cursor.close();
        db.close();
        return new Book(id, title, author, category, startDate, review, status, favourited);
    }

    public void updateBook(int id, String status, String review) {
        SQLiteDatabase db = getWritableDatabase();

        SQLiteStatement updateBookStatement = db.compileStatement("UPDATE Book SET Status = ?, Review = ? WHERE BookID = ?");
        updateBookStatement.bindString(1, status);
        updateBookStatement.bindString(2, review);
        updateBookStatement.bindLong(3, id);
        try {
            updateBookStatement.execute();
        } catch (SQLException e) {
            Toast.makeText(context,
                    "Error in updating book data",
                    Toast.LENGTH_LONG
            ).show();
        }
        updateBookStatement.close();
        db.close();
    }

    public void updateBookFavourite(int id, int favourited) {
        SQLiteDatabase db = getWritableDatabase();

        SQLiteStatement updateBookStatement = db.compileStatement("UPDATE Book SET Favourite = ? WHERE BookID = ?");
        updateBookStatement.bindLong(1, favourited);
        updateBookStatement.bindLong(2, id);
        try {
            updateBookStatement.execute();
        } catch (SQLException e) {
            Toast.makeText(context,
                    "Error in favouriting book",
                    Toast.LENGTH_LONG
            ).show();
            updateBookStatement.close();
            db.close();
            return;
        }
        if (favourited == 1) {
            Toast.makeText(context,
                    "Book added to Favourites",
                    Toast.LENGTH_LONG
            ).show();
        }
        else {
            Toast.makeText(context,
                    "Book removed from Favourites",
                    Toast.LENGTH_LONG
            ).show();
        }
        updateBookStatement.close();
        db.close();
    }

    public void removeBook(int id) {
        SQLiteDatabase db = getWritableDatabase();

        SQLiteStatement deleteBookStatement = db.compileStatement("DELETE FROM Book WHERE BookID = ?");
        deleteBookStatement.bindLong(1, id);
        try {
            deleteBookStatement.execute();
        } catch (SQLException e) {
            Toast.makeText(context,
                    "Error in updating book data",
                    Toast.LENGTH_LONG
            ).show();
            deleteBookStatement.close();
            db.close();
            return;
        }
        Toast.makeText(context,
                "Successfully removed book from library",
                Toast.LENGTH_LONG
        ).show();
        deleteBookStatement.close();
        db.close();
    }
}
