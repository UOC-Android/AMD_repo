package com.andresgqjob.expenseapp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andresgqjob.expenseapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    TextView inputUserName;
    TextView inputPwd;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btn_register);
        inputPwd = findViewById(R.id.input_pwd);
        inputUserName = findViewById(R.id.input_username);

        //Add actions to the buttons:
        btnRegister.setOnClickListener(v -> {
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(inputUserName.getText().toString(), inputPwd.getText().toString())
                    .addOnCompleteListener(RegisterActivity.this, task -> {
                        if (!task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Successful Registration", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Error with Register: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    });
            finish();
        });
    }
}