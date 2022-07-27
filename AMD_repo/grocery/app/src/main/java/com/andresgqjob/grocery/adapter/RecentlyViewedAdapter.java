package com.andresgqjob.grocery.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.andresgqjob.grocery.ProductDetails;
import com.andresgqjob.grocery.R;
import com.andresgqjob.grocery.model.RecentlyViewed;

import java.util.List;

public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.RecentlyViewedViewHolder> {

    Context context;
    List<RecentlyViewed> recentlyViewedList;

    public RecentlyViewedAdapter(Context context, List<RecentlyViewed> recentlyViewedList) {
        this.context = context;
        this.recentlyViewedList = recentlyViewedList;
    }

    public static class RecentlyViewedViewHolder extends RecyclerView.ViewHolder {

        TextView name, description, price, qty, ud;
        ConstraintLayout bg;

        public RecentlyViewedViewHolder(@NonNull ViewGroup parent) {
            super(parent);

            name = itemView.findViewById(R.id.product_name);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);
            ud = itemView.findViewById(R.id.unit);
            bg = itemView.findViewById(R.id.recently_layout);
        }
    }

    @NonNull
    @Override
    public RecentlyViewedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recently_viewed_items, parent, false);
        return new RecentlyViewedViewHolder((ViewGroup) view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyViewedViewHolder holder, final int position) {
        holder.name.setText(recentlyViewedList.get(position).getName());
        holder.description.setText(recentlyViewedList.get(position).getDescription());
        holder.price.setText(recentlyViewedList.get(position).getPrice());
        holder.qty.setText(recentlyViewedList.get(position).getQuantity());
        holder.ud.setText(recentlyViewedList.get(position).getUnit());
        holder.bg.setBackgroundResource(recentlyViewedList.get(position).getImageUrl());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ProductDetails.class);
            intent.putExtra("name", recentlyViewedList.get(position).getName());
            intent.putExtra("description", recentlyViewedList.get(position).getDescription());
            intent.putExtra("price", recentlyViewedList.get(position).getPrice());
            intent.putExtra("qty", recentlyViewedList.get(position).getQuantity());
            intent.putExtra("ud", recentlyViewedList.get(position).getUnit());
            intent.putExtra("image", recentlyViewedList.get(position).getBigImgUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recentlyViewedList.size();
    }
}
