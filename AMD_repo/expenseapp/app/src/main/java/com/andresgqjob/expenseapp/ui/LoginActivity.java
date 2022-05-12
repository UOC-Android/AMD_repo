package com.andresgqjob.expenseapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andresgqjob.expenseapp.R;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    //private TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login      = findViewById(R.id.btn_login);//boton de login
        TextView register = findViewById(R.id.btn_goto_register);//boton de registro
        email    = findViewById(R.id.input_username);//campo de email
        password = findViewById(R.id.input_pwd);//campo de password
//        forgotPassword = findViewById(R.id.forgotPassword);

        //boton de login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//si el usuario y contraseña son correctos
                String mail = LoginActivity.this.email.getText().toString();//obtiene el email
                String pass = LoginActivity.this.password.getText().toString();//obtiene la contraseña
                if (mail.isEmpty() || pass.isEmpty()) {//si el email o la contraseña estan vacios
                    Toast.makeText(LoginActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();//muestra un mensaje de error
                } else {//si el email y la contraseña no estan vacios
                    Intent intent = new Intent(LoginActivity.this, TripListActivity.class);//cambia de actividad
                    startActivity(intent);//inicia la actividad
                }
            }
        });

        //boton de registro
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//cambia de actividad
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);//cambia de actividad
                startActivity(intent);//inicia la actividad
            }
        });

//        forgotPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}