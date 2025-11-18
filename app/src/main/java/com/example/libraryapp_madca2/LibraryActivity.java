package com.example.libraryapp_madca2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp_madca2.adapters.RVAdapter;
import com.example.libraryapp_madca2.classes.Book;
import com.example.libraryapp_madca2.db.DBHelper;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    ConstraintLayout mainLibrary;
    RecyclerView rv;
    RVAdapter rvAdapter;
    DBHelper dbHelper;
    ArrayList<Book> books;
    ArrayList<Book> favouriteBooks;
    SearchView searchView;
    ImageView imgFavouriteIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_library);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_library), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(this);

        books = new ArrayList<>();
        favouriteBooks = new ArrayList<>();

        displayBooks();
        getFavouriteBooks();

        rv = findViewById(R.id.recycler_view);
        mainLibrary = findViewById(R.id.main_library);
        rvAdapter = new RVAdapter(this,
                books);
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        ItemTouchHelper deleteItemTouchHelper = new ItemTouchHelper(deleteSimpleCallback);
        deleteItemTouchHelper.attachToRecyclerView(rv);
        ItemTouchHelper favouriteItemTouchHelper = new ItemTouchHelper(favouriteSimpleCallback);
        favouriteItemTouchHelper.attachToRecyclerView(rv);
    }

    ItemTouchHelper.SimpleCallback deleteSimpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Book book = books.remove(viewHolder.getBindingAdapterPosition());
            int bookId = book.getId();
            rvAdapter.notifyItemRemoved(viewHolder.getBindingAdapterPosition());
            dbHelper.removeBook(bookId);
        }
    };

    ItemTouchHelper.SimpleCallback favouriteSimpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            imgFavouriteIcon = viewHolder.itemView.findViewById(R.id.rv_favourite_icon);

            Book book = books.get(viewHolder.getBindingAdapterPosition());
            if (book.getFavourite() == 0) {
                book.setFavourite(1);
                favouriteBooks.add(book);
                dbHelper.updateBookFavourite(book.getId(), 1);
            }
            else {
                book.setFavourite(0);
                favouriteBooks.remove(book);
                dbHelper.updateBookFavourite(book.getId(), 0);
            }
            rvAdapter.notifyItemChanged(viewHolder.getBindingAdapterPosition());
        }
    };

    public void displayBooks() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userEmail = sp.getString("EMAIL", "");

        Book book;
        int bookId;
        String bookTitle;
        String bookAuthor;
        String bookCategory;
        String bookStartDate;
        String bookReview;
        String bookStatus;
        int bookFavourited;

        Cursor cursor = dbHelper.getAllBooks(userEmail);

        if (cursor.getCount() == 0) {
            Toast.makeText(
                    this,
                    "No books in your library.",
                    Toast.LENGTH_SHORT
            ).show();
        }
        else {
            while (cursor.moveToNext()) {
                bookId = cursor.getInt(0);
                bookTitle = cursor.getString(1);
                bookAuthor = cursor.getString(2);
                bookCategory = cursor.getString(3);
                bookStartDate = cursor.getString(4);
                bookReview = cursor.getString(5);
                bookStatus = cursor.getString(6);
                bookFavourited = cursor.getInt(8);
                book = new Book(bookId, bookTitle, bookAuthor, bookCategory, bookStartDate, bookReview, bookStatus, bookFavourited);
                books.add(book);
            }
        }
    }

    public void getFavouriteBooks() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userEmail = sp.getString("EMAIL", "");

        Book book;
        int bookId;
        String bookTitle;
        String bookAuthor;
        String bookCategory;
        String bookStartDate;
        String bookReview;
        String bookStatus;
        int bookFavourited;

        Cursor cursor = dbHelper.getFavouriteBooks(userEmail);

        while (cursor.moveToNext()) {
            bookId = cursor.getInt(0);
            bookTitle = cursor.getString(1);
            bookAuthor = cursor.getString(2);
            bookCategory = cursor.getString(3);
            bookStartDate = cursor.getString(4);
            bookReview = cursor.getString(5);
            bookStatus = cursor.getString(6);
            bookFavourited = cursor.getInt(8);
            book = new Book(bookId, bookTitle, bookAuthor, bookCategory, bookStartDate, bookReview, bookStatus, bookFavourited);
            favouriteBooks.add(book);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        setupSearchView(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile_button) {
            Intent intent = new Intent(LibraryActivity.this, UserProfileActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.favourite_button) {
            new AlertDialog.Builder(LibraryActivity.this)
                    .setTitle("Favourite Books")
                    .setMessage(getFavouriteBooksToString())
                    .setPositiveButton("OK", null)
                    .show();
        }
        return true;
    }

    public String getFavouriteBooksToString() {
        String result = "";
        for (Book book : favouriteBooks) {
            result += book.toString();
        }
        return result;
    }

    public void setupSearchView(Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.search_view).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                rvAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                rvAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    public void toAddBookScreen(View view) {
        Intent intent = new Intent(LibraryActivity.this, AddBookActivity.class);
        startActivity(intent);
    }
}