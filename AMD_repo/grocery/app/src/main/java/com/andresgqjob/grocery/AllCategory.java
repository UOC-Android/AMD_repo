package com.andresgqjob.grocery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.andresgqjob.grocery.adapter.AllCategoryAdapter;
import com.andresgqjob.grocery.model.AllCategoryModel;

import java.util.ArrayList;
import java.util.List;

public class AllCategory extends AppCompatActivity
{
    RecyclerView allCategoryRecycler;
    AllCategoryAdapter allCategoryAdapter;
    List<AllCategoryModel> categoryModelList;

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);

        allCategoryRecycler = findViewById(R.id.allCategory);
        back = findViewById(R.id.back);

        back.setOnClickListener(v -> {
            startActivity(new Intent(AllCategory.this, MainActivity.class));
            finish();
        });

        categoryModelList = new ArrayList<>();
        categoryModelList.add(new AllCategoryModel(1, R.drawable.ic_fruits));
        categoryModelList.add(new AllCategoryModel(2, R.drawable.ic_veggies));
        categoryModelList.add(new AllCategoryModel(3, R.drawable.ic_meat));
        categoryModelList.add(new AllCategoryModel(4, R.drawable.ic_fish));
        categoryModelList.add(new AllCategoryModel(5, R.drawable.ic_spices));
        categoryModelList.add(new AllCategoryModel(6, R.drawable.ic_egg));
        categoryModelList.add(new AllCategoryModel(7, R.drawable.ic_drink));
        categoryModelList.add(new AllCategoryModel(8, R.drawable.ic_cookies));
        categoryModelList.add(new AllCategoryModel(9, R.drawable.ic_juce));

        allCategoryRecycler(categoryModelList);
    }

    private void allCategoryRecycler(List<AllCategoryModel> categoryModelList) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        allCategoryRecycler.setLayoutManager(layoutManager);
        allCategoryRecycler.addItemDecoration(new GridSpacingItemDecoration(4, dpToPx(16), true));
        allCategoryRecycler.setItemAnimator(new DefaultItemAnimator());
        allCategoryAdapter = new AllCategoryAdapter(this, categoryModelList);
        allCategoryRecycler.setAdapter(allCategoryAdapter);
    }

    // now we need some item decoration class for manage spacing

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}