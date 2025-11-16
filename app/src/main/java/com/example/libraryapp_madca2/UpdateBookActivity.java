package com.example.libraryapp_madca2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.libraryapp_madca2.classes.Book;
import com.example.libraryapp_madca2.db.DBHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class UpdateBookActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView tvTitle, tvAuthor;
    Spinner spinnerStatus;
    EditText etReview;
    Book book;

    ArrayList<String> statuses = new ArrayList<>(Arrays.asList(
            "In progress", "Finished"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int bookId = Integer.parseInt(getIntent().getStringExtra("BOOK_ID"));

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statuses
        );
        statusAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerStatus = findViewById(R.id.spinner_updatestatus);
        spinnerStatus.setAdapter(statusAdapter);

        tvTitle = findViewById(R.id.label_updatetitle);
        tvAuthor = findViewById(R.id.label_updateauthor);
        etReview = findViewById(R.id.et_updatereview);

        dbHelper = new DBHelper(this);
        book = dbHelper.findBook(bookId);

        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        if (book.getStatus().equals("In progress")) {
            spinnerStatus.setSelection(0);
        }
        else {
            spinnerStatus.setSelection(1);
        }
        etReview.setText(book.getReview());
    }

    public void updateBook(View view) {
        String status = spinnerStatus.getSelectedItem().toString();
        String review = etReview.getText().toString();

        book.setStatus(status);
        book.setReview(review);

        dbHelper.updateBook(book.getId(), book.getStatus(), book.getReview());

        Intent intent = new Intent(UpdateBookActivity.this, LibraryActivity.class);
        startActivity(intent);
    }
}