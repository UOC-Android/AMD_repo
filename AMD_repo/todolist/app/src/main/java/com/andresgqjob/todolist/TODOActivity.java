package com.andresgqjob.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andresgqjob.todolist.databinding.ActivityTodoactivityBinding;
import com.andresgqjob.todolist.databinding.ItemBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TODOActivity extends AppCompatActivity
{
    private ActivityTodoactivityBinding binding;

    RecyclerView rv;
    LinearLayoutManager linearLayoutManager;
//    List<Grupo> grupos;
//    GruposAdapter adapter;
    DatabaseReference dbRef;
    FirebaseRecyclerOptions<Grupo> options;
    FirebaseRecyclerAdapter<Grupo, GruposAdapter.ViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTodoactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbRef = FirebaseDatabase.getInstance("https://todolistapp-7a67b-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference();

        rv = binding.recyclerView;
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        // adapter = new GruposAdapter(grupos, this);

        options = new FirebaseRecyclerOptions.Builder<Grupo>()
                .setQuery(dbRef.child("grupos"), Grupo.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Grupo, GruposAdapter.ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GruposAdapter.ViewHolder holder, int position, @NonNull Grupo model) {
                holder.binding.idGroup.setText(String.valueOf(model.getId()));
                holder.binding.groupName.setText(model.getGroupName());
                holder.binding.trash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.getRef(holder.getAbsoluteAdapterPosition()).removeValue();
                    }
                });
            }

            @NonNull
            @Override
            public GruposAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new GruposAdapter.ViewHolder(ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            }
        };
        adapter.startListening();
        rv.setAdapter(adapter);

        binding.addBtn.setOnClickListener(view -> {
            Grupo grupo = new Grupo(Integer.parseInt(binding.addIdGroup.getText().toString()),
                    binding.addGroupName.getText().toString());
            dbRef.child("grupos").push().setValue(grupo);
        });
    }

//    public void createData() {
//        grupos = new ArrayList<>();
//        grupos.add(new Grupo(1, "Grupo A"));
//        grupos.add(new Grupo(2, "Grupo B"));
//        grupos.add(new Grupo(3, "Grupo C"));
//        grupos.add(new Grupo(4, "Grupo D"));
//    }
}