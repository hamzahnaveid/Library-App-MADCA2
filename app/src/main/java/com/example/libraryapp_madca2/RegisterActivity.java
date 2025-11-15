package com.example.libraryapp_madca2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.libraryapp_madca2.classes.User;
import com.example.libraryapp_madca2.db.DBHelper;

public class RegisterActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    EditText etEmail, etPassword, etName, etDob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etEmail = findViewById(R.id.et_registeremail);
        etPassword = findViewById(R.id.et_registerpassword);
        etName = findViewById(R.id.et_name);
        etDob = findViewById(R.id.et_dob);

        dbHelper = new DBHelper(this);
    }

    public void register(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String name = etName.getText().toString();
        String dob = etDob.getText().toString();

        if (dbHelper.findUser(email) != null) {
            Toast.makeText(
                    this, "This email is already in use. Please try again",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || dob.isEmpty()) {
            Toast.makeText(this,
                    "Please fill in all of the required fields",
                    Toast.LENGTH_SHORT
            ).show();
            etEmail.setText("");
            etPassword.setText("");
            etName.setText("");
            etDob.setText("");
            return;
        }

        User user = new User(email, password, name, dob);
        dbHelper.insertUser(user);

        Intent intent = new Intent(RegisterActivity.this, LibraryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("EMAIL", email);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}