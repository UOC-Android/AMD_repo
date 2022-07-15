package com.andresgqjob.mapgame.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.andresgqjob.mapgame.R;

public class MultiPlayer extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);

        this.getActionBar().setTitle("MultiPlayer");
    }
}