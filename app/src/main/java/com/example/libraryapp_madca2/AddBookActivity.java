package com.example.libraryapp_madca2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

public class AddBookActivity extends AppCompatActivity {

    DBHelper dbHelper;

    Spinner spinnerStatus, spinnerCategory;

    EditText etTitle, etAuthor, etStartDate, etReview;

    ArrayList<String> categories = new ArrayList<>(Arrays.asList(
            "Fantasy", "Sci-fi", "Technology", "Lifestyle", "Romance"
            ));
    ArrayList<String> statuses = new ArrayList<>(Arrays.asList(
            "In progress", "Finished"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(this);

        //Populating Category spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerCategory.setAdapter(categoryAdapter);

        //Populating Status spinner
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statuses
        );
        statusAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerStatus = findViewById(R.id.spinner_status);
        spinnerStatus.setAdapter(statusAdapter);

        //References for EditText fields
        etTitle = findViewById(R.id.et_addtitle);
        etAuthor = findViewById(R.id.et_addauthor);
        etStartDate = findViewById(R.id.et_addstartdate);
        etReview = findViewById(R.id.et_addreview);
    }

    public void addBook(View view) {
        String title = etTitle.getText().toString();
        String author = etAuthor.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String startDate = etStartDate.getText().toString();
        String review = etReview.getText().toString();
        String status = spinnerStatus.getSelectedItem().toString();

        Book book = new Book(0, title, author, category, startDate, review, status);
        dbHelper.insertBook(book);
    }
}