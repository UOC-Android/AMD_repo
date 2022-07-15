package com.andresgqjob.testcrud;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;

public class RVActivity extends AppCompatActivity
{
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
//    RVAdapter adapter;
    DAOEmployee dao;
    FirebaseRecyclerAdapter adapter;
    boolean isLoading = false;
    String key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rvactivity);

        swipeRefreshLayout = findViewById(R.id.swip);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
//        adapter = new RVAdapter(this);
//        clerView.setAdapter(adapter);
        dao = new DAOEmployee();
        FirebaseRecyclerOptions<Employee> opt =
                new FirebaseRecyclerOptions.Builder<Employee>().setQuery(dao.getAll(), new SnapshotParser<Employee>()
                {
                    @NonNull
                    @Override
                    public Employee parseSnapshot(@NonNull DataSnapshot snapshot) {
                        Employee emp = snapshot.getValue(Employee.class);
                        assert emp != null;
                        emp.setKey(snapshot.getKey());
                        return emp;
                    }
                }).build();

        adapter = new FirebaseRecyclerAdapter(opt)
        {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Object model) {
                EmployeeVH vh = (EmployeeVH) holder;
                Employee emp = (Employee) model;

                vh.txtName.setText(emp.getName());
                vh.txtPos.setText(emp.getPos());
                vh.txtOpt.setOnClickListener(v ->
                {
                    PopupMenu popupMenu = new PopupMenu(RVActivity.this, vh.txtOpt);
                    popupMenu.inflate(R.menu.option_menu);
                    popupMenu.setOnMenuItemClickListener(item ->
                    {
                        switch (item.getItemId()) {
                            case R.id.menu_edit:
                                Intent intent = new Intent(RVActivity.this, MainActivity.class);
                                intent.putExtra("EDIT", emp);
                                startActivity(intent);
                                break;
                            case R.id.menu_rmv:
                                DAOEmployee dao = new DAOEmployee();
                                dao.remove(emp.getKey()).addOnSuccessListener(suc ->
                                {
                                    Toast.makeText(RVActivity.this, "Registro borrado", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(er ->
                                {
                                    Toast.makeText(RVActivity.this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                                break;
                        }
                        return false;
                    });
                    popupMenu.show();
                });
            }

            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(RVActivity.this).inflate(R.layout.layout_item, parent, false);
                return new EmployeeVH(view);
            }

            @Override
            public void onDataChanged() {
                Toast.makeText(RVActivity.this, "Datos modificados", Toast.LENGTH_SHORT).show();
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

//        loadData();

//    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
//    {
//        @Override
//        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//            assert linearLayoutManager != null;
//
//            int totalItem = linearLayoutManager.getItemCount();
//            int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//
//            if (totalItem < lastVisible + 3) {
//                if (!isLoading) {
//                    isLoading = true;
//                    loadData();
//                }
//            }
//        }
//    });

//    private void loadData() {
//        swipeRefreshLayout.setRefreshing(true);
//
//        dao.get(key).addListenerForSingleValueEvent(new ValueEventListener()
//        {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ArrayList<Employee> emps = new ArrayList<>();
//
//                for (DataSnapshot data : snapshot.getChildren()) {
//                    Employee emp = data.getValue(Employee.class);
//                    assert emp != null;
//                    emp.setKey(data.getKey());
//                    emps.add(emp);
//                    key = data.getKey();
//                }
//
//                adapter.setItems(emps);
//                adapter.notifyDataSetChanged();
//                isLoading = false;
//                swipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//    }
}