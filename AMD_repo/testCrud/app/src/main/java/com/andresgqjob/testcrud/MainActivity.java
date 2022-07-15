package com.andresgqjob.testcrud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editName = findViewById(R.id.edit_name);
        final EditText editPos  = findViewById(R.id.edit_pos);

        Button btnSave = findViewById(R.id.btn_submit);
        Button btnOpen = findViewById(R.id.btn_open);

        btnOpen.setOnClickListener(v ->
        {
            Intent intent = new Intent(MainActivity.this, RVActivity.class);
            startActivity(intent);
        });

        DAOEmployee dao = new DAOEmployee();
        Employee empEdit = (Employee) getIntent().getSerializableExtra("EDIT");

        if (empEdit != null) {
            btnSave.setText("UPDATE");
            editName.setText(empEdit.getName());
            editPos.setText(empEdit.getPos());
            btnOpen.setVisibility(View.GONE);
        } else {
            btnSave.setText("SUBMIT");
            btnOpen.setVisibility(View.VISIBLE);
        }

        btnSave.setOnClickListener(v ->
        {
            Employee emp = new Employee(editName.getText().toString(), editPos.getText().toString());

            if (empEdit == null) {
                dao.add(emp).addOnSuccessListener(suc ->
                {
                    Toast.makeText(this, "Registro agregado", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("name", editName.getText().toString());
                hashMap.put("pos", editPos.getText().toString());
                dao.update(empEdit.getKey(), hashMap).addOnSuccessListener(suc ->
                {
                    Toast.makeText(this, "Registro actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(er ->
                {
                    Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}