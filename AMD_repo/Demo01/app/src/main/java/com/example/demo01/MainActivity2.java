package com.example.demo01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        String user = intent.getStringExtra("user");
        String pass = intent.getStringExtra("pass");
        Log.i("DEMO-MainActivity2", user + "/" + pass);

        TextView info = findViewById(R.id.info);
        info.setText(user + "/" + pass);

        /*Button finish = findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                data.putExtra("ResultData", 10.5);
                setResult(RESULT_OK, intent);
                finish();
            }
        }*/
    }
}