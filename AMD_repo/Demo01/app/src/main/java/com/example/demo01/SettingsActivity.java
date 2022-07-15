package com.example.demo01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Button b_pvsp, b_pvsai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        b_pvsp = (Button) findViewById(R.id.b_pvsp);
        b_pvsai = (Button) findViewById(R.id.b_pvsai);

        b_pvsp.setOnClickListener(this);
        b_pvsai.setOnClickListener(this);

//        b_pvsai.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), TicTacToe_AI.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.b_pvsp) {
                Intent intent = new Intent(getApplicationContext(), TicTacToe.class);
                startActivity(intent);
        }
        //Intent intent2 = new Intent(getApplicationContext(), TicTacToe_AI.class);
        //startActivity(intent2);
    }
}