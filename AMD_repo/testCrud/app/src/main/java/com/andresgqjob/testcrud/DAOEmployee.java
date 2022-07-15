package com.andresgqjob.testcrud;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAOEmployee
{
    private final DatabaseReference myDbRef;

    public DAOEmployee() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://testcrud-3a256-default-rtdb.europe-west1.firebasedatabase.app");
        myDbRef = db.getReference(Employee.class.getSimpleName());
    }

    public Task<Void> add(Employee emp) {
        return myDbRef.push().setValue(emp);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return myDbRef.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key) {
        return myDbRef.child(key).removeValue();
    }

    public Query get(String key) {
        if (key == null) {
            return myDbRef.orderByKey().limitToFirst(8);
        }
        return myDbRef.orderByKey().startAfter(key).limitToFirst(8);
    }

    public Query getAll() {
        return myDbRef;
    }
}