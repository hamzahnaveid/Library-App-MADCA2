package com.example.libraryapp_madca2;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.libraryapp_madca2.classes.User;
import com.example.libraryapp_madca2.db.DBHelper;

public class UserProfileActivity extends AppCompatActivity {

    User user;
    DBHelper dbHelper;

    TextView tvName, tvDob;
    EditText etName, etDob;
    Button btnSaveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String email = getIntent().getExtras().getString("EMAIL");
        dbHelper = new DBHelper(this);
        user = dbHelper.findUser(email);

        tvName = findViewById(R.id.tv_name);
        tvDob = findViewById(R.id.tv_dob);
        etName = findViewById(R.id.et_profilename);
        etDob = findViewById(R.id.et_profiledob);

        btnSaveChanges = findViewById(R.id.button_savechanges);

        tvName.setText(user.getName());
        tvDob.setText(user.getDob());
        etName.setText(user.getName());
        etDob.setText(user.getDob());
    }

    public void changeName(View view) {
        if (!btnSaveChanges.isEnabled()) {
            btnSaveChanges.setEnabled(true);
        }
        tvName.setVisibility(INVISIBLE);
        etName.setVisibility(VISIBLE);
    }

    public void changeDob(View view) {
        if (!btnSaveChanges.isEnabled()) {
            btnSaveChanges.setEnabled(true);
        }
        tvDob.setVisibility(INVISIBLE);
        etDob.setVisibility(VISIBLE);
    }

    public void saveChanges(View view) {
        String email = user.getEmail();
        String name = etName.getText().toString();
        String dob = etDob.getText().toString();

        if (name.isEmpty() || dob.isEmpty()) {
            Toast.makeText(
                    this, "Please fill in the required fields",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        dbHelper.updateUser(email, name, dob);
        Toast.makeText(
                this, "Details successfully updated",
                Toast.LENGTH_SHORT
        ).show();

        user = dbHelper.findUser(email);

        tvName.setText(user.getName());
        etName.setText(user.getName());

        tvDob.setText(user.getDob());
        etDob.setText(user.getDob());

        etDob.setVisibility(INVISIBLE);
        tvDob.setVisibility(VISIBLE);

        etName.setVisibility(INVISIBLE);
        tvName.setVisibility(VISIBLE);

        btnSaveChanges.setEnabled(false);
    }
}