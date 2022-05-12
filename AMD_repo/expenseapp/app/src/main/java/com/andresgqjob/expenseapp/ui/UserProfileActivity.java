package com.andresgqjob.expenseapp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.andresgqjob.expenseapp.R;

public class UserProfileActivity extends AppCompatActivity {
    Button btnLogout;
    Button btnSave;
    TextView txtUserName;
    ImageView imgAvatar;
    EditText inputName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        btnLogout   = findViewById(R.id.btn_logout);
        btnSave     = findViewById(R.id.btn_save);
        txtUserName = findViewById(R.id.txt_username);
        imgAvatar   = findViewById(R.id.img_user);
        inputName   = findViewById(R.id.input_name);

        //Add actions to the buttons:
        btnLogout.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            //TODO: Save the user's name and avatar
        });
    }
}