package com.example.libraryapp_madca2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.libraryapp_madca2.classes.User;
import com.example.libraryapp_madca2.db.DBHelper;

public class RegisterActivity extends AppCompatActivity {

    private DBHelper dbhelper;

    EditText etEmail, etPassword, etName, etDob;
    User user;

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

        dbhelper = new DBHelper(this);
    }

    public void register(View view) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String name = etName.getText().toString();
        String dob = etDob.getText().toString();

        String createUserStatement = "INSERT INTO User VALUES('" +
                email + "', '" + password +
                "', '" + name + "', '" + dob + "')";
        dbhelper.insert(createUserStatement);

        User user = new User(email, password, name, dob);

        Intent intent = new Intent(RegisterActivity.this, LibraryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", user);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}