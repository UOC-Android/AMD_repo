package com.andresgqjob.expenseapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andresgqjob.expenseapp.R;
import com.andresgqjob.expenseapp.model.TripInfo;
import com.andresgqjob.expenseapp.ui.adapter.TripListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class TripListActivity extends AppCompatActivity {
    FloatingActionButton btnAddNewTrip;
    ImageButton btnViewProfile;

    @Override
    //Method to create the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        btnAddNewTrip  = findViewById(R.id.btn_add_new_trip);//botón para agregar un nuevo viaje
        btnViewProfile = findViewById(R.id.btn_view_profile);//botón para ver el perfil

        //Add actions to the buttons:
        //botón para agregar un nuevo viaje
        btnAddNewTrip.setOnClickListener(v -> {
            Intent k = new Intent(TripListActivity.this, TripEditActivity.class);
            startActivity(k);
        });

        //botón para ver el perfil
        btnViewProfile.setOnClickListener(v -> {
            Intent k = new Intent(TripListActivity.this, UserProfileActivity.class);
            startActivity(k);
        });

        //Crear el recyclerview
        String path1 = "https://m.media-amazon.com/images/M/MV5BNzUxNjM4ODI1OV5BMl5BanBnXkFtZTgwNTEwNDE2OTE@._V1_SX150_CR0,0,150,150_.jpg";//imagen del viaje 1
        String path2 = "https://m.media-amazon.com/images/M/MV5BMTUyMDU1MTU2N15BMl5BanBnXkFtZTgwODkyNzQ3MDE@._V1_SX150_CR0,0,150,150_.jpg";
        String path3 = "https://m.media-amazon.com/images/M/MV5BMTk1MjM5NDg4MF5BMl5BanBnXkFtZTcwNDg1OTQ4Nw@@._V1_SX150_CR0,0,150,150_.jpg";
        String path4 = "https://m.media-amazon.com/images/M/MV5BMjExNjY5NDY0MV5BMl5BanBnXkFtZTgwNjQ1Mjg1MTI@._V1_SX150_CR0,0,150,150_.jpg";
        String path5 = "https://imagenes.elpais.com/resizer/ignf5hRqPoNrcNeilF3aB9CKy-M=/1960x0/cloudfront-eu-central-1.images.arcpublishing.com/prisa/HE3SMC3L7Z7XENXLHLLKE3CDEA.jpg";

        TripInfo[] myListData = new TripInfo[] {//lista de viajes
                new TripInfo(path5, "(10/17/2021)", "Trip 1", 1),
                new TripInfo("", "(10/17/2021)", "Trip 2",2),
                new TripInfo(path2, "(10/17/2021)", "Trip 3",3),
                new TripInfo("", "(10/17/2021)", "Trip 4",4),
                new TripInfo(path3, "(10/17/2021)", "Trip 5",5),
                new TripInfo(path4, "(10/17/2021)", "Trip 6",6),
                new TripInfo("", "(10/17/2021)", "Trip 7",7),
                new TripInfo("", "(10/17/2021)", "Trip 8",8),
                new TripInfo("", "(10/17/2021)", "Trip 9",9),
                new TripInfo("", "(10/17/2021)", "Trip 10",10),
                new TripInfo("", "(10/17/2021)", "Trip 11",11),
                new TripInfo("", "(10/17/2021)", "Trip 12",12)
        };

        //Crear el adaptador
        RecyclerView recyclerView = findViewById(R.id.trip_list);//lista de viajes
        TripListAdapter adapter = new TripListAdapter(myListData, this);//crear el adaptador
        recyclerView.setHasFixedSize(true);//para que no se modifique el tamaño de la lista
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//para que se muestre en forma de lista
        recyclerView.setAdapter(adapter);//para que se muestre el adaptador
    }
}