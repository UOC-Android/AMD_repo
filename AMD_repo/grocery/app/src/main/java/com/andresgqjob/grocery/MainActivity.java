package com.andresgqjob.grocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.andresgqjob.grocery.adapter.CategoryAdapter;
import com.andresgqjob.grocery.adapter.DiscountedProductAdapter;
import com.andresgqjob.grocery.adapter.RecentlyViewedAdapter;
import com.andresgqjob.grocery.model.Category;
import com.andresgqjob.grocery.model.DiscountedProducts;
import com.andresgqjob.grocery.model.RecentlyViewed;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    RecyclerView discountRecyclerView, categoryRecyclerView, recentlyViewedRecycler;
    DiscountedProductAdapter discountedProductAdapter;
    List<DiscountedProducts> discountedProductsList;

    CategoryAdapter categoryAdapter;
    List<Category> categoryList;

    RecentlyViewedAdapter recentlyViewedAdapter;
    List<RecentlyViewed> recentlyViewedList;

    ImageView allCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        discountRecyclerView = findViewById(R.id.discountedRecycler);
        categoryRecyclerView = findViewById(R.id.categoryRecycler);
        allCategory = findViewById(R.id.allCategoryImg);
        recentlyViewedRecycler = findViewById(R.id.recently_item);

        allCategory.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, com.andresgqjob.grocery.AllCategory.class));
        });

        //adding data to model
        discountedProductsList = new ArrayList<>();
        discountedProductsList.add(new DiscountedProducts(1, R.drawable.discountberry));
        discountedProductsList.add(new DiscountedProducts(2, R.drawable.discountbrocoli));
        discountedProductsList.add(new DiscountedProducts(3, R.drawable.discountmeat));
        discountedProductsList.add(new DiscountedProducts(4, R.drawable.discountberry));
        discountedProductsList.add(new DiscountedProducts(5, R.drawable.discountbrocoli));
        discountedProductsList.add(new DiscountedProducts(6, R.drawable.discountmeat));

        categoryList = new ArrayList<>();
        categoryList.add(new Category(1, R.drawable.ic_home_fish));
        categoryList.add(new Category(2, R.drawable.ic_home_meats));
        categoryList.add(new Category(3, R.drawable.ic_home_veggies));
        categoryList.add(new Category(4, R.drawable.ic_home_fruits));
        categoryList.add(new Category(5, R.drawable.ic_home_fish));
        categoryList.add(new Category(6, R.drawable.ic_home_meats));
        categoryList.add(new Category(7, R.drawable.ic_home_veggies));
        categoryList.add(new Category(8, R.drawable.ic_home_fruits));

        recentlyViewedList = new ArrayList<>();
        recentlyViewedList.add(new RecentlyViewed("Watermelon", "Watermelon has high water content and also provides some fiber.", "3 €", "1", "KG", R.drawable.card4, R.drawable.b4));
        recentlyViewedList.add(new RecentlyViewed("Papaya", "Papayas are spherical or pear-shaped fruits that can be as long as 20 inches.", "1.25 €", "1", "KG", R.drawable.card3, R.drawable.b3));
        recentlyViewedList.add(new RecentlyViewed("Strawberry", "The strawberry is a highly nutritious fruit, loaded with vitamin C.", "2.50 €", "1", "KG", R.drawable.card2, R.drawable.b1));
        recentlyViewedList.add(new RecentlyViewed("Kiwi", "Full of nutrients like vitamin C, vitamin K, vitamin E, folate, and potassium.", "2.25 €", "1", "PC", R.drawable.card1, R.drawable.b2));

        setDiscountedRecycler(discountedProductsList);
        setCategoryRecycler(categoryList);
        setRecentlyViewedRecycler(recentlyViewedList);
    }

    private void setDiscountedRecycler(List<DiscountedProducts> dataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        discountRecyclerView.setLayoutManager(layoutManager);
        discountedProductAdapter = new DiscountedProductAdapter(this, dataList);
        discountRecyclerView.setAdapter(discountedProductAdapter);
    }

    private void setCategoryRecycler(List<Category> categoryDataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(this, categoryDataList);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void setRecentlyViewedRecycler(List<RecentlyViewed> recentlyViewedDataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        recentlyViewedRecycler.setLayoutManager(layoutManager);
        recentlyViewedAdapter = new RecentlyViewedAdapter(this, recentlyViewedDataList);
        recentlyViewedRecycler.setAdapter(recentlyViewedAdapter);
    }
}