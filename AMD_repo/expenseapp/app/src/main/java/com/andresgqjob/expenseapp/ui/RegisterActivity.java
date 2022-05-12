package com.andresgqjob.expenseapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andresgqjob.expenseapp.R;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    TextView inputUserName;
    TextView inputPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//construye la actividad
        setContentView(R.layout.activity_register);//carga el layout

        btnRegister = findViewById(R.id.btn_register);//obtiene el boton
        inputPwd = findViewById(R.id.input_pwd);//obtiene el campo de contraseña
        inputUserName = findViewById(R.id.input_username);//obtiene el campo de usuario

        btnRegister.setOnClickListener(new View.OnClickListener() {
            //al presionar el boton
            public void onClick(View v) {
                finish();//cierra la actividad
            }
        });
    }
}