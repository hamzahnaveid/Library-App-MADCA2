package com.example.libraryapp_madca2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.example.libraryapp_madca2.db.DBHelper;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    ConstraintLayout mainLibrary;
    RecyclerView rv;
    RVAdapter rvAdapter;
    DBHelper dbHelper;
    ArrayList<String> bookId, bookTitle, bookAuthor, bookCategory, bookStartDate, bookStatus;

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

        bookId = new ArrayList<>();
        bookTitle = new ArrayList<>();
        bookAuthor = new ArrayList<>();
        bookCategory = new ArrayList<>();
        bookStartDate = new ArrayList<>();
        bookStatus = new ArrayList<>();

        displayBooks();

        rv = findViewById(R.id.recycler_view);
        mainLibrary = findViewById(R.id.main_library);
        rvAdapter = new RVAdapter(this,
                bookId,
                bookTitle,
                bookAuthor,
                bookCategory,
                bookStartDate,
                bookStatus);
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int bookIdSwiped = Integer.parseInt(bookId.remove(viewHolder.getBindingAdapterPosition()));
            rvAdapter.notifyItemRemoved(viewHolder.getBindingAdapterPosition());
            dbHelper.removeBook(bookIdSwiped);
        }
    };

    public void displayBooks() {
        Cursor cursor = dbHelper.getAllBooks();
        if (cursor.getCount() == 0) {
            Toast.makeText(
                    this,
                    "No books in your library.",
                    Toast.LENGTH_SHORT
            ).show();
        }
        else {
            while (cursor.moveToNext()) {
                bookId.add(cursor.getString(0));
                bookTitle.add(cursor.getString(1));
                bookAuthor.add(cursor.getString(2));
                bookCategory.add(cursor.getString(3));
                bookStartDate.add(cursor.getString(4));
                bookStatus.add(cursor.getString(6));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile_button) {
            Intent intent = new Intent(LibraryActivity.this, UserProfileActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void toAddBookScreen(View view) {
        Intent intent = new Intent(LibraryActivity.this, AddBookActivity.class);
        startActivity(intent);
    }
}