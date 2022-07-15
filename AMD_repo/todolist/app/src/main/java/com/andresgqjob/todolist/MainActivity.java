package com.andresgqjob.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.andresgqjob.todolist.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.todoBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TODOActivity.class));
        });
    }
}