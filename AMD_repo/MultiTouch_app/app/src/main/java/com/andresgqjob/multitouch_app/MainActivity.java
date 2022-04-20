package com.andresgqjob.multitouch_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    MultiTouchView myTouchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiTouchView touchView = findViewById(R.id.multi_touch_view);
        touchView.initView(this);
    }
}