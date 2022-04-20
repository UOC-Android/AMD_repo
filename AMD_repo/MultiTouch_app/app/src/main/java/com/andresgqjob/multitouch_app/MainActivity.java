package com.andresgqjob.multitouch_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    MultiTouchView myTouchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myTouchView = new MultiTouchView(this);
        setContentView(myTouchView);
        //setContentView(R.layout.activity_main);
    }
}