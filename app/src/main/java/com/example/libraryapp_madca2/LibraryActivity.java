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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp_madca2.adapters.RVAdapter;
import com.example.libraryapp_madca2.classes.User;
import com.example.libraryapp_madca2.db.DBHelper;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    RecyclerView rv;
    RVAdapter rvAdapter;
    DBHelper dbHelper;
    ArrayList<String> bookId, bookTitle, bookAuthor, bookCategory, bookStartDate, bookStatus;
//    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_library);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        String email = getIntent().getExtras().getString("EMAIL");
        dbHelper = new DBHelper(this);
//        user = dbHelper.findUser(email);

        bookId = new ArrayList<>();
        bookTitle = new ArrayList<>();
        bookAuthor = new ArrayList<>();
        bookCategory = new ArrayList<>();
        bookStartDate = new ArrayList<>();
        bookStatus = new ArrayList<>();

        displayBooks();

        rv = findViewById(R.id.recycler_view);
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
    }

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
//            Bundle bundle = new Bundle();
//            bundle.putString("EMAIL", user.getEmail());
//            intent.putExtras(bundle);
            startActivity(intent);
        }
        return true;
    }

    public void toAddBookScreen(View view) {
        Intent intent = new Intent(LibraryActivity.this, AddBookActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("EMAIL", user.getEmail());
//        intent.putExtras(bundle);
        startActivity(intent);
    }
}