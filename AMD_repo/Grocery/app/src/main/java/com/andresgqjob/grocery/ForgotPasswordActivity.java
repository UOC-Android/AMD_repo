package com.andresgqjob.grocery;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andresgqjob.grocery.databinding.ActivityForgotPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity
{
    private ActivityForgotPasswordBinding binding;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor, espere...");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn.setOnClickListener(view -> onBackPressed());

        binding.recoverBtn.setOnClickListener(view -> recoverPass());
    }

    private void recoverPass() {
        String mail = binding.emailEt.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            Toast.makeText(this, "Por favor, ingrese un correo válido...", Toast.LENGTH_SHORT).show();
        }

        progressDialog.setMessage("Enviando instrucciones para recuperar contraseña...");
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(mail)
                .addOnSuccessListener(aVoid -> {
                    // instrucciones enviadas
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, "Se ha enviado un correo para recuperar contraseña...", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // error al enviar las instrucciones
                    progressDialog.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}