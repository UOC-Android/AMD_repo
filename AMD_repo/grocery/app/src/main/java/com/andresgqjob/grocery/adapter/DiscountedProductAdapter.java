package com.andresgqjob.grocery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andresgqjob.grocery.MainActivity;
import com.andresgqjob.grocery.R;
import com.andresgqjob.grocery.model.DiscountedProducts;

import java.util.List;

public class DiscountedProductAdapter extends RecyclerView.Adapter<DiscountedProductAdapter.DiscountedProductViewHolder> {

    Context context;
    List<DiscountedProducts> discountedProductsList;

    public DiscountedProductAdapter(MainActivity mainActivity, List<DiscountedProducts> dataList) {
        context = mainActivity;
        discountedProductsList = dataList;
    }

    public static class DiscountedProductViewHolder extends RecyclerView.ViewHolder {
        ImageView discountedProductImage;
        public DiscountedProductViewHolder(@NonNull ViewGroup parent) {
            super(parent);

            discountedProductImage = itemView.findViewById(R.id.categoryImg);
        }
    }

    @NonNull
    @Override
    public DiscountedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discounted_row_items, parent, false);
        return new DiscountedProductViewHolder((ViewGroup) view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountedProductAdapter.DiscountedProductViewHolder holder, int position) {
        holder.discountedProductImage.setImageResource(discountedProductsList.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return discountedProductsList.size();
    }
}


