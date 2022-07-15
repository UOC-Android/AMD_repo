package com.andresgqjob.todolist.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOTask {
    DatabaseReference myRef;
    public DAOTask(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        myRef = db.getReference(Task.class.getSimpleName());
        myRef.keepSynced(true);
    }
    public Task<Void> add(Task t){
        return myRef.push().setValue(t);
    }
}
