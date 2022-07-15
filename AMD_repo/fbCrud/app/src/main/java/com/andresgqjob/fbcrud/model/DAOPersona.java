package com.andresgqjob.fbcrud.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOPersona {
    DatabaseReference myRef;
    public DAOPersona(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        myRef = db.getReference(Persona.class.getSimpleName());
        myRef.keepSynced(true);
    }
    public Task<Void> add(Persona p){
        return myRef.push().setValue(p);
    }
}
