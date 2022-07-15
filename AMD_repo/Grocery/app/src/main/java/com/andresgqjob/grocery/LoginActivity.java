package com.andresgqjob.grocery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.andresgqjob.grocery.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity
{
    private ActivityLoginBinding binding;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor, espere...");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.noAccountTv.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterUserActivity.class));
        });

        binding.forgotPassTv.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        binding.loginBtn.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        String mail = binding.emailEt.getText().toString().trim();
        String pass = binding.passwordEt.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            Toast.makeText(this, "Por favor, introduce un correo válido", Toast.LENGTH_SHORT).show();
        }
        if (pass.length() < 6) {
            Toast.makeText(this, "Por favor, introduce una contraseña válida", Toast.LENGTH_SHORT).show();
        }

        progressDialog.setMessage("Entrando en la sesión...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(mail, pass)
                .addOnSuccessListener(authResult -> {
                    // login correcto
                    makeMeOnline();
                })
                .addOnFailureListener(e -> {
                    // login incorrecto
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,
                            "Error al iniciar sesión" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void makeMeOnline() {
        // despues de logearse, se actualiza el estado de la sesión en la base de datos a usuario en línea
        progressDialog.setMessage("Comprobando usuario...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "true");

        // actualizar el estado de la sesión en la base de datos
        DatabaseReference dbRef =
                FirebaseDatabase.getInstance("https://grocery-26758-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users");
        dbRef.child(Objects.requireNonNull(firebaseAuth.getUid())).updateChildren(hashMap)
                .addOnSuccessListener(aVoid -> {
                    // actualización exitosa
                    checkUserType();
                })
                .addOnFailureListener(e -> {
                    // fallo en la actualización
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,
                            "Error al iniciar sesión" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void checkUserType() {
        // si el usuario es tipo vendedor, se redirige a la actividad principal de vendedor
        // si el usuario es tipo cliente, se redirige a la actividad principal de cliente

        DatabaseReference ref =
                FirebaseDatabase.getInstance("https://grocery-26758-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        for (DataSnapshot ds : snap.getChildren()) {
                            String accountType = ds.child("accountType").getValue(String.class);
                            if (accountType.equals("Vendedor")) {
                                startActivity(new Intent(LoginActivity.this, MainSellerActivity.class));
                                finish();
                            } else if (accountType.equals("Usuario")) {
                                startActivity(new Intent(LoginActivity.this, MainUserActivity.class));
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,
                                "Error al iniciar sesión" + databaseError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}