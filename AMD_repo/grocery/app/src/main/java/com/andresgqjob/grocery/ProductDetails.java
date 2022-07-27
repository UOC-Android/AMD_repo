package com.andresgqjob.grocery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetails extends AppCompatActivity
{
    ImageView img, back;
    TextView productName, productDesc, productPrice;
    String name, desc, price;
    int image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        desc = intent.getStringExtra("desc");
        price = intent.getStringExtra("price");
        image = intent.getIntExtra("image", R.drawable.b1);

        img = findViewById(R.id.big_image);
        back = findViewById(R.id.back2);
        productName = findViewById(R.id.productName);
        productDesc = findViewById(R.id.prodDesc);
        productPrice = findViewById(R.id.prodPrice);

        productName.setText(name);
        productDesc.setText(desc);
        productPrice.setText(price);
        img.setImageResource(image);

        back.setOnClickListener(v -> {
            Intent i = new Intent(ProductDetails.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }
}