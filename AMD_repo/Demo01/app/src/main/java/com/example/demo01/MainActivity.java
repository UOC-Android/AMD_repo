package com.example.demo01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private EditText user;
    private EditText pass;
    private TextView errUser;
    private TextView errPass;
    private Button checkCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkCredentials = findViewById(R.id.checkCredentials);
        user = findViewById(R.id.user);
        pass = findViewById(R.id.pass);

        errUser = findViewById(R.id.errUser);
        errPass = findViewById(R.id.errUser);

        errUser.setVisibility(View.INVISIBLE);
        errPass.setVisibility(View.INVISIBLE);

        user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkUser();
                }
            }
        });

        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkPass();
                }
            }
        });

        checkCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPass() && checkUser()) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    intent.putExtra("user", user.getText().toString());
                    intent.putExtra("pass", pass.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    private boolean checkPass() {
        String s_pwd = pass.getText().toString();
        char[] chars = s_pwd.toCharArray();
        boolean containsDigit = false;
        for (char c : chars) {
            if (Character.isDigit(c)) {
                containsDigit = true;
            }
        }
        //Check pwd:
        boolean allIsCorrect = false;
        if (s_pwd.length() <= 5) {
            errPass.setVisibility(View.VISIBLE);
            errPass.setText("El password debe estar formado por más de 5 caracteres");
        }
        else if (!containsDigit) {
            errPass.setVisibility(View.VISIBLE);
            errPass.setText("El password debe contener al menos un número");
        }
        else {
            errPass.setVisibility(View.INVISIBLE);
            allIsCorrect = true;
        }

        return allIsCorrect;
    }

    private boolean checkUser() {
        boolean allIsCorrect = false;
        //Check user email:
        String s_user = user.getText().toString();
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s_user);
        if (!m.matches()) {
            errUser.setVisibility(View.VISIBLE);
            errUser.setText("Debe ser un email con formato válido!");
        }
        else {
            errUser.setVisibility(View.INVISIBLE);
            allIsCorrect = true;
        }

        return allIsCorrect;
    }
}