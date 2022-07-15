package com.andresgqjob.fbcrud;

import static com.andresgqjob.fbcrud.model.FirebaseRef.PERSONA_REF;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andresgqjob.fbcrud.model.Persona;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final java.util.UUID UUID = null;
    EditText nameP;
    EditText emailP;
    EditText passP;
    EditText surnameP;
    FirebaseDatabase db;
    ListView listVPerson;
    DatabaseReference myRef;
    Persona personaSelected;
//    DAOPersona dao = new DAOPersona();
    ArrayAdapter<Persona> arrayAdapterPerson;
    private final List<Persona> listPerson = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameP = findViewById(R.id.txt_nombrePersona);
        surnameP = findViewById(R.id.txt_apePersona);
        emailP = findViewById(R.id.txt_emailPersona);
        passP = findViewById(R.id.txt_passPersona);
        listVPerson = findViewById(R.id.lv_datosPersonales);

        initFirebase();
        listData();
        listVPerson.setOnItemClickListener((adapterView, view, i, l) -> {
            personaSelected = (Persona) adapterView.getItemAtPosition(i);
            nameP.setText(personaSelected.getName());
            surnameP.setText(personaSelected.getSurname());
            emailP.setText(personaSelected.getEmail());
            passP.setText(personaSelected.getPassword());
        });
    }

    private void initFirebase() {
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();
        myRef.keepSynced(true);
    }

    private void listData() {
        myRef.child(PERSONA_REF).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPerson.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    Persona p = objSnapshot.getValue(Persona.class);
                    listPerson.add(p);
                }
                arrayAdapterPerson = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, listPerson);
                listVPerson.setAdapter(arrayAdapterPerson);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String name = nameP.getText().toString();
        String surname = surnameP.getText().toString();
        String email = emailP.getText().toString();
        String pass = passP.getText().toString();

        switch (item.getItemId()) {
            case R.id.icon_add:
                if (name.equals("") || surname.equals("") || email.equals("") || pass.equals("")) {
                    validation();
                } else {
//                    Persona persona = new Persona(personaSelected.getUid(), name, surname, email, pass);
                    Persona persona = new Persona();
                    persona.setUid(UUID.randomUUID().toString());
                    persona.setName(name);
                    persona.setSurname(surname);
                    persona.setEmail(email);
                    persona.setPassword(pass);
//                    dao.add(persona).addOnSuccessListener(suc -> {
//                        Toast.makeText(MainActivity.this, "Persona agregada", Toast.LENGTH_SHORT).show();
//                        listData();
//                    }).addOnFailureListener(er -> Toast.makeText(MainActivity.this, ""+er.getMessage()+"Error al agregar persona", Toast.LENGTH_SHORT).show());
                    myRef.child("persona").child(persona.getUid()).setValue(persona);
                    Toast.makeText(this, "Agregado", Toast.LENGTH_SHORT).show();
                    fieldReset();
                }
                break;
            case R.id.icon_save:
                Persona p = new Persona();
                p.setUid(personaSelected.getUid());
                p.setName(name.trim());
                p.setSurname(surname.trim());
                p.setEmail(email.trim());
                p.setPassword(pass.trim());
                myRef.child(PERSONA_REF).child(p.getUid()).setValue(p);
                Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show();
                fieldReset();
                break;
            case R.id.icon_delete:
                Persona pers = new Persona();
                pers.setUid(personaSelected.getUid());
                myRef.child(PERSONA_REF).child(pers.getUid()).removeValue();
                Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show();
                fieldReset();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void fieldReset() {
        nameP.setText("");
        surnameP.setText("");
        emailP.setText("");
        passP.setText("");
    }

    private void validation() {
        String name = nameP.getText().toString();
        String surname = surnameP.getText().toString();
        String email = emailP.getText().toString();
        String pass = passP.getText().toString();
        String msg = "No puede dejar campos vacios";

        if (name.equals("")) {
            nameP.setError(msg);
        } else if (surname.equals("")) {
            surnameP.setError(msg);
        } else if (email.equals("")) {
            emailP.setError(msg);
        } else if (pass.equals("")) {
            passP.setError(msg);
        }
    }
}