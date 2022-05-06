package com.andresgqjob.expenseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

public class TripListActivity extends AppCompatActivity {
    private Button btnAddNewTrip;
    private Button btnViewTrip;
    private Button btnEditTrip;
    private Button btnDeleteTrip;
    private Button btnViewUserProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        btnAddNewTrip = findViewById(R.id.btnAddTrip);
        btnViewTrip = findViewById(R.id.btnViewTrip);
        btnEditTrip = findViewById(R.id.btnEditTrip);
        btnDeleteTrip = findViewById(R.id.btnDeleteTrip);
        btnViewUserProfile = findViewById(R.id.btnUserProfile);


        btnAddNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripListActivity.this, AddTripActivity.class);
                startActivity(intent);
            }
        });

        btnViewUserProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent k = new Intent(TripListActivity.this, UserProfileActivity.class);
                startActivity(k);
            }
        });

        btnViewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripListActivity.this, ViewTripActivity.class);
                startActivity(intent);
            }
        });

        String path1 = "https://m.media-amazon.com/images/M/MV5BNzUxNjM4ODI1OV5BMl5BanBnXkFtZTgwNTEwNDE2OTE@._V1_SX150_CR0,0,150,150_.jpg";
        String path2 = "https://m.media-amazon.com/images/M/MV5BMTUyMDU1MTU2N15BMl5BanBnXkFtZTgwODkyNzQ3MDE@._V1_SX150_CR0,0,150,150_.jpg";
        String path3 = "https://m.media-amazon.com/images/M/MV5BMTk1MjM5NDg4MF5BMl5BanBnXkFtZTcwNDg1OTQ4Nw@@._V1_SX150_CR0,0,150,150_.jpg";
        String path4 = "https://m.media-amazon.com/images/M/MV5BMjExNjY5NDY0MV5BMl5BanBnXkFtZTgwNjQ1Mjg1MTI@._V1_SX150_CR0,0,150,150_.jpg";

        TripInfo[] myListData = new TripInfo[]{
                new TripInfo(path1, "(10/17/2021)", "Trip 1", 1),
                new TripInfo("", "(10/17/2021)", "Trip 2", 2),
                new TripInfo(path2, "(10/17/2021)", "Trip 3", 3),
                new TripInfo("", "(10/17/2021)", "Trip 4", 4),
                new TripInfo(path3, "(10/17/2021)", "Trip 5", 5),
                new TripInfo(path4, "(10/17/2021)", "Trip 6", 6),
                new TripInfo("", "(10/17/2021)", "Trip 7", 7),
                new TripInfo("", "(10/17/2021)", "Trip 8", 8),
                new TripInfo("", "(10/17/2021)", "Trip 9", 9),
                new TripInfo("", "(10/17/2021)", "Trip 10", 10),
                new TripInfo("", "(10/17/2021)", "Trip 11", 11),
                new TripInfo("", "(10/17/2021)", "Trip 12", 12)
        };

        RecyclerView recyclerView = findViewById(R.id.trip_list);
        TripListAdapter adapter = new TripListAdapter(myListData, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}