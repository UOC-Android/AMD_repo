package com.andresgqjob.grocery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andresgqjob.grocery.R;
import com.andresgqjob.grocery.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;

        public CategoryViewHolder(@NonNull ViewGroup parent) {
            super(parent);

            categoryImage = itemView.findViewById(R.id.categoryImg);
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_row_items, parent, false);
        return new CategoryViewHolder((ViewGroup) view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        holder.categoryImage.setImageResource(categoryList.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}

