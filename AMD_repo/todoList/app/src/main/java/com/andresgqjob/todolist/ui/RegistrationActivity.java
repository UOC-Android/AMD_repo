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

public class RegistrationActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private EditText regEmail, regPass;
    private Button regBtn;
    private TextView loginQtn;

    private FirebaseAuth mAuth;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar();

        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        regEmail = findViewById(R.id.input_email_reg);
        regPass  = findViewById(R.id.input_pass_reg);
        regBtn   = findViewById(R.id.btn_signup);
        loginQtn = findViewById(R.id.login_link);

        loginQtn.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        regBtn.setOnClickListener(v -> {
            String email = regEmail.getText().toString().trim();
            String pass = regPass.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                regEmail.setError("El email es obligatorio");
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                regPass.setError("El password es obligatorio");
                return;
            } else {
                loader.setMessage("Registro en progreso");
                loader.setCanceledOnTouchOutside(false);
                loader.show();
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String error = task.getException().toString();
                        Toast.makeText(RegistrationActivity.this, "El registro falló" + error, Toast.LENGTH_SHORT).show();
                    }
                    loader.dismiss();
                });
            }
        });
    }
}