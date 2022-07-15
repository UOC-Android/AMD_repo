package com.andresgqjob.todolist.ui;

import static com.andresgqjob.todolist.model.FirebaseRef.TASK_REF;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andresgqjob.todolist.R;
import com.andresgqjob.todolist.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity
{
    DatabaseReference ref;
    DatabaseReference myRef;
    FirebaseDatabase db;
    private ProgressDialog loader;
    String selectedDate;
    TextView txt_tripDate;

//    private final String key = "";
//    private String task;
//    private String desc;
//
//    ListView listVTask;
//
//    ArrayAdapter<Task> arrayAdapterTask;
//    private final List<Task> listTask = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar();

        RecyclerView recyclerView = findViewById(R.id.home_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loader = new ProgressDialog(this);

        initFirebase();
//        listData();

//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser mUser = mAuth.getCurrentUser();
//        assert mUser != null;
//        String onlineUserID = mUser.getUid();
//        ref = FirebaseDatabase.getInstance().getReference().child("tasks").child(onlineUserID);

        FloatingActionButton fab = findViewById(R.id.home_fab);
        fab.setOnClickListener(v -> addTask());
    }

    private void initFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        String onlineUserID = mUser.getUid();
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference().child("tasks").child(onlineUserID);
        myRef.keepSynced(true);
    }

//    private void listData() {
//        myRef.child(TASK_REF).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                listTask.clear();
//                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
//                    Task t = objSnapshot.getValue(Task.class);
//                    listTask.add(t);
//                }
//                arrayAdapterTask = new ArrayAdapter<>(HomeActivity.this, android.R.layout.simple_list_item_1, listTask);
//                listVTask.setAdapter(arrayAdapterTask);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(HomeActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        loader.setTitle("Loading...");
//        loader.setMessage("Please wait while we load your tasks.");
//        loader.setCanceledOnTouchOutside(false);
//        loader.show();
//        ref.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    loader.dismiss();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                loader.dismiss();
//            }
//        });
//    }

    private void addTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.input_file, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);
        dialog.show();

        final EditText task = myView.findViewById(R.id.field_task);
        final EditText desc = myView.findViewById(R.id.field_desc);
        Button save = myView.findViewById(R.id.save_btn);
        Button cancel = myView.findViewById(R.id.cancel_btn);

        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        save.setOnClickListener(v -> {
            String mTask = task.getText().toString().trim();
            String mDesc = desc.getText().toString().trim();
            String id = ref.push().getKey();
//            txt_tripDate = findViewById(R.id.txt_tripDate);
//            txt_tripDate.setText("Fecha:  ----");
            final String[] date = {DateFormat.getDateInstance().format(new Date())};
            DatePickerDialog mDlgDatePicker = new DatePickerDialog(HomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    date[0] = year + "-" + (monthOfYear + 1 < 10 ? "0" : "") + (monthOfYear + 1) + "-"
                            + (dayOfMonth < 10 ? "0" : "") + dayOfMonth;
                    selectedDate = date[0];
                    txt_tripDate.setText(String.format("Fecha:  %s", date[0]));
                }
            }, 2014, 1, 1);
            mDlgDatePicker.show();

            if (TextUtils.isEmpty(mTask)) {
                task.setError("Tarea obligatoria");
            }
            if (TextUtils.isEmpty(mDesc)) {
                task.setError("Descripcion obligatoria");
            } else {
                loader.setMessage("Añadiendo fecha");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                Task model = new Task(mTask, mDesc, id, date[0]);
                assert id != null;
                ref.child(id).setValue(model).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(HomeActivity.this, "La tarea se inserto correctamente", Toast.LENGTH_LONG).show();
                        loader.dismiss();
                    } else {
                        String err = task1.getException().toString();
                        Toast.makeText(HomeActivity.this, "Fallo: " + err, Toast.LENGTH_LONG).show();
                        loader.dismiss();
                    }
                });
            }
            dialog.dismiss();
        });
        dialog.show();
    }
}