package com.andresgqjob.grocery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andresgqjob.grocery.R;
import com.andresgqjob.grocery.model.AllCategoryModel;

import java.util.List;

public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.AllCategoryViewHolder> {

    Context context;
    List<AllCategoryModel> allCategoryList;

    public AllCategoryAdapter(Context context, List<AllCategoryModel> allCategoryList) {
        this.context = context;
        this.allCategoryList = allCategoryList;
    }

    public static class AllCategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;

        public AllCategoryViewHolder(@NonNull ViewGroup parent) {
            super(parent);

            categoryImage = itemView.findViewById(R.id.categoryImg);
        }
    }

    @NonNull
    @Override
    public AllCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_category_row_items, parent, false);
        return new AllCategoryViewHolder((ViewGroup) view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllCategoryViewHolder holder, int position) {
        holder.categoryImage.setImageResource(allCategoryList.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return allCategoryList.size();
    }
}

