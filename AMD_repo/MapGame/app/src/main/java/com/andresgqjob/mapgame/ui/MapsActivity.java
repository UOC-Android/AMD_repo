package com.andresgqjob.mapgame.ui;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.andresgqjob.mapgame.R;
import com.andresgqjob.mapgame.databinding.ActivityMapsBinding;
import com.andresgqjob.mapgame.model.Quiz;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap mMap;

    private Button btnValidate;
    private Button btnSetPos;
    private Button btnNext;

    private TextView txtQuest;
    private TextView txtTotalDist;

    double distance = 0;
    float totalDist = 0.0f;
    int currentQuiz = -1;

    LatLng selectedPos;
    LatLng targetPos;

    Marker selectedMrk = null;
    Marker targetMrk = null;

    Polyline polyline = null;

    ArrayList<Quiz> quizArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //----------
        // Only for testing purposes:
        quizArrayList.add(new Quiz("Where is Barcelona?", 2.1699, 41.3879));
        quizArrayList.add(new Quiz("Where is Paris?", 2.3515, 48.8566));
        //----------

        com.andresgqjob.mapgame.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        // Add a marker in Sydney and move the camera
        btnSetPos = findViewById(R.id.btn_setPos);
        btnSetPos.setOnClickListener(v -> {
            selectedPos = mMap.getCameraPosition().target;
            if (selectedMrk != null) {
                selectedMrk.remove();
            }
            selectedMrk = mMap.addMarker(new MarkerOptions().position(selectedPos).title("Your position"));
            btnValidate.setEnabled(true);
        });

        btnValidate = findViewById(R.id.btn_validate);
        btnValidate.setOnClickListener(v -> {
            double lon = quizArrayList.get(currentQuiz).getLon();
            double lat = quizArrayList.get(currentQuiz).getLat();
            targetPos = new LatLng(lat, lon);
            targetMrk = mMap.addMarker(new MarkerOptions()
                    .position(targetPos)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_flag))
                    .title("TARGET"));

            distance = SphericalUtil.computeDistanceBetween(targetPos, selectedPos) / 1000;

            txtQuest.setText(String.format("Distance between target and selected: %.2f Km.", distance));
            totalDist += distance;
            txtTotalDist.setText(String.format("Total distance: %.2f Km.", totalDist));

            polyline = mMap.addPolyline(new PolylineOptions()
                    .add(selectedPos, targetPos)
                    .clickable(true)
                    .width(5)
                    .color(Color.RED));

            btnSetPos.setVisibility(View.INVISIBLE);
            btnValidate.setVisibility(View.INVISIBLE);
            showAllMarkers();
            btnNext.setVisibility(View.VISIBLE);
        });

        btnValidate.setEnabled(false);
        txtQuest = findViewById(R.id.txt_quest);
        txtTotalDist = findViewById(R.id.txt_dist);
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(v -> prepareNextQuestion());
        prepareNextQuestion();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void showAllMarkers() {
        double lon = quizArrayList.get(currentQuiz).getLon();
        double lat = quizArrayList.get(currentQuiz).getLat();
        targetPos = new LatLng(lat, lon);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(selectedPos);
        builder.include(targetPos);

        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
        mMap.animateCamera(cu, new GoogleMap.CancelableCallback()
        {
            public void onCancel() {

            }

            public void onFinish() {
                CameraUpdate zoomOut = CameraUpdateFactory.zoomBy((float) -0.5);
                mMap.animateCamera(zoomOut);
            }
        });
    }

    public void prepareNextQuestion() {
        if (currentQuiz != -1) {
            if (targetMrk != null) {
                targetMrk.remove();
                targetMrk = null;
            }

            if (selectedMrk != null) {
                selectedMrk.remove();
                selectedMrk = null;
            }

            if (polyline != null) {
                polyline.remove();
                polyline = null;
            }
        }

        if (currentQuiz == quizArrayList.size() - 1) {
            // End of the game
            new AlertDialog.Builder(MapsActivity.this)
                    .setTitle("Round finished")
                    .setMessage(String.format("Total distance: %.2f Km.", totalDist))
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        } else {
            txtTotalDist.setText(String.format("Total distance: %.2f Km.", totalDist));
            btnNext.setVisibility(View.INVISIBLE);
            currentQuiz++;
            btnValidate.setVisibility(View.VISIBLE);
            btnValidate.setEnabled(true);
            btnSetPos.setVisibility(View.VISIBLE);
            btnSetPos.setEnabled(true);
            txtQuest.setText(quizArrayList.get(currentQuiz).getDescription());
        }
    }
}