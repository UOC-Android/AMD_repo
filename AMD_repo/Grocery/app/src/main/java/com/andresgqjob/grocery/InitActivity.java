package com.andresgqjob.grocery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InitActivity extends AppCompatActivity
{
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hacer la pantalla completa
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();

        // comenzar la login activity despues de 1secs
        new Handler().postDelayed(() -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // usuario no logeado al comenzar la login activity
                startActivity(new Intent(InitActivity.this, LoginActivity.class));
                finish();
            } else {
                // usuario logeado y comprueba el tipo de cuenta que tiene
                checkUserType();
            }
        }, 1000);
    }

    private void checkUserType() {
        // si el usuario es tipo vendedor, se redirige a la actividad principal de vendedor
        // si el usuario es tipo cliente, se redirige a la actividad principal de cliente

        DatabaseReference dbRef = FirebaseDatabase.getInstance("https://grocery-26758-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users");
        dbRef.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String accountType = "" + ds.child("accountType").getValue();
                            if (accountType.equals("Vendedor")) {
                                // redirigir a la actividad principal de vendedor
                                startActivity(new Intent(InitActivity.this, MainSellerActivity.class));
                                finish();
                            } else if (accountType.equals("Usuario")) {
                                // redirigir a la actividad principal de cliente
                                startActivity(new Intent(InitActivity.this, MainUserActivity.class));
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(InitActivity.this,
                                "Error al iniciar sesión" + databaseError.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}