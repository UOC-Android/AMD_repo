package com.andresgqjob.grocery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andresgqjob.grocery.databinding.ActivityMainUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class MainSellerActivity extends AppCompatActivity
{
    private ActivityMainUserBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Por favor, espere...");
        progressDialog.setCanceledOnTouchOutside(false);

        checkUser();

        binding.signoutBtn.setOnClickListener(view -> {
            // pasar al estado offline
            // salir de la sesión
            // ir a la actividad de inicio de sesión
            makeMeOffline();
        });

        binding.editProfileBtn.setOnClickListener(view -> {
            // ir a la actividad de edición de perfil
            startActivity(new Intent(MainSellerActivity.this, ProfileEditSellerActivity.class));
        });
    }

    private void makeMeOffline() {
        // despues de logearse, se actualiza el estado de la sesión en la base de datos a usuario en línea
        progressDialog.setMessage("Saliendo de la sesión...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        // actualizar el estado de la sesión en la base de datos
        DatabaseReference ref = FirebaseDatabase.getInstance("https://grocery-26758-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Users");
        ref.child(Objects.requireNonNull(firebaseAuth.getUid())).updateChildren(hashMap)
                .addOnSuccessListener(aVoid -> {
                    // actualización exitosa
                    firebaseAuth.signOut();
                    checkUser();
                })
                .addOnFailureListener(e -> {
                    // fallo en la actualización
                    progressDialog.dismiss();
                    Toast.makeText(MainSellerActivity.this,
                            "Error al iniciar sesión" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            // User is signed in
            startActivity(new Intent(MainSellerActivity.this, LoginActivity.class));
            finish();
        } else {
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        for (DataSnapshot ds : snap.getChildren()) {
                            String name = "" + ds.child("name").getValue();
                            String accountType = "" + ds.child("accountType").getValue();

                            binding.nameTv.setText(name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError e) {
                        Toast.makeText(MainSellerActivity.this,
                                "Error al cargar información: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}