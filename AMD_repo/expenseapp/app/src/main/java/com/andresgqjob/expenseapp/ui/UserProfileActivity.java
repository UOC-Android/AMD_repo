package com.andresgqjob.expenseapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.andresgqjob.expenseapp.R;
import com.google.firebase.auth.FirebaseAuth;

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

        btnLogout = findViewById(R.id.btn_logout);
        btnSave = findViewById(R.id.btn_save);
        txtUserName = findViewById(R.id.txt_username);
        imgAvatar = findViewById(R.id.img_user);
        inputName = findViewById(R.id.input_name);

        //Add actions to the buttons:
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(UserProfileActivity.this, LoginActivity.class);
                // set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }
}