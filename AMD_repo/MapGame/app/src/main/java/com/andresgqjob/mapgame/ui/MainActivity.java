package com.andresgqjob.mapgame.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.andresgqjob.mapgame.R;

public class MainActivity extends AppCompatActivity
{
    Button singlePlayer;
    Button multiPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        singlePlayer = findViewById(R.id.single_player);
        multiPlayer = findViewById(R.id.multi_player);

        singlePlayer.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SinglePlayer.class)));
        multiPlayer.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MultiPlayer.class)));
    }
}