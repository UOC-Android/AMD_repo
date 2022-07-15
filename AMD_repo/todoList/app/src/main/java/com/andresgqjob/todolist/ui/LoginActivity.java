package com.andresgqjob.todolist.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andresgqjob.todolist.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private EditText loginEmail, loginPass;
    private Button loginBtn;
    private TextView regQtn;

    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar();
        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        loginEmail = findViewById(R.id.input_email_login);
        loginPass  = findViewById(R.id.input_pass_login);
        loginBtn   = findViewById(R.id.btn_login);
        regQtn     = findViewById(R.id.signup_link);

        regQtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        loginBtn.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String pass = loginPass.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                loginEmail.setError("El email es obligatorio");
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                loginPass.setError("El password es obligatorio");
                return;
            } else {
                loader.setMessage("Login en progreso");
                loader.setCanceledOnTouchOutside(false);
                loader.show();
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String error = task.getException().toString();
                        Toast.makeText(LoginActivity.this, "El login falló" + error, Toast.LENGTH_SHORT).show();
                    }
                    loader.dismiss();
                });
            }

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}