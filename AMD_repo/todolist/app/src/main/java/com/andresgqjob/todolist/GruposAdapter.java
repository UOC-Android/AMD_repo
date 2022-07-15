package com.andresgqjob.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andresgqjob.todolist.databinding.ItemBinding;

import java.util.List;

public class GruposAdapter extends RecyclerView.Adapter<GruposAdapter.ViewHolder>
{
    private final List<Grupo> grupos;
    private final Context context;

    public GruposAdapter(List<Grupo> grupos, Context context) {
        this.grupos = grupos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBinding binding = ItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Grupo grupo = grupos.get(position);
        holder.binding.idGroup.setText(String.valueOf(grupo.getId()));
        holder.binding.groupName.setText(grupo.getGroupName());
    }

    @Override
    public int getItemCount() {
        return grupos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        protected ItemBinding binding;

        public ViewHolder(@NonNull ItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
