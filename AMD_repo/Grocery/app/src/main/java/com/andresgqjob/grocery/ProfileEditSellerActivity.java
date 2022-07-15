package com.andresgqjob.grocery;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.andresgqjob.grocery.databinding.ActivityProfileEditSellerBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ProfileEditSellerActivity extends AppCompatActivity implements LocationListener
{
    private ActivityProfileEditSellerBinding binding;

    private double lat = 0.0;
    private double lon = 0.0;

    // permission constants
    private static final int GPS_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int CAMERA_REQUEST_CODE = 300;

    // image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    // permission arrays
    private String[] locationPermissions;
    private String[] storagePermissions;
    private String[] cameraPermissions;

    // image picked uri
    private Uri image_uri;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditSellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // init permission arrays
        locationPermissions = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION};
        storagePermissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Cargando, por favor, espere...");
        progressDialog.setCanceledOnTouchOutside(false);

        checkUser();

        // init UI views
        binding.backBtn.setOnClickListener(view -> onBackPressed());

        binding.gpsBtn.setOnClickListener(view -> {
            // detectar la posicion actual
            if (checkLocationPermission()) {
                // si tiene permisos, se inicia el GPS
                detectLocation();
            } else {
                // si no tiene permisos, se solicita
                requestLocationPermission();
            }
        });

        binding.profileIv.setOnClickListener(view -> {
            // seleccionar imagen de perfil desde la galeria
            showImagePickDialog();
        });

        binding.updateBtn.setOnClickListener(view -> {
            // update user
            inputData();
        });
    }

    String name;
    String shopName;
    String phone;
    String dlvryFee;
    String country;
    String cp;
    String city;
    String address;
    boolean shopOpen;

    private void inputData() {
        // input data
        name = binding.nameEt.getText().toString().trim();
        shopName = binding.shopNameEt.getText().toString().trim();
        phone = binding.phoneEt.getText().toString().trim();
        dlvryFee = binding.deliveryFeeEt.getText().toString().trim();
        country = binding.countryEt.getText().toString().trim();
        cp = binding.postalCodeEt.getText().toString().trim();
        city = binding.cityEt.getText().toString().trim();
        address = binding.completeAddressEt.getText().toString().trim();
        shopOpen = binding.shopOpenSwitch.isChecked();

        updateProfile();
    }

    private void updateProfile() {
        progressDialog.setMessage("Actualizando perfil, por favor, espere...");
        progressDialog.show();

        if (image_uri == null) {
            // si no se ha seleccionado imagen, se actualiza el perfil sin imagen
            updateProfileWithoutImage();
        } else {
            // si se ha seleccionado imagen, se actualiza el perfil con imagen
            updateProfileWithImage();
        }
    }

    private void updateProfileWithImage() {
        // guardar info con imagen

        // nombre y ruta de la imagen
        String filePathAndName = "profile_img/" + "" + firebaseAuth.getUid();
        // cargar imagen a firebase storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(image_uri)
                .addOnSuccessListener(taskSnapshot -> {
                    // obtener url de la imagen cargada
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    Uri downloadUri = uriTask.getResult();

                    if (uriTask.isSuccessful()) {
                        // configurar datos a guardar
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("name", "" + name);
                        hashMap.put("shopName", "" + shopName);
                        hashMap.put("phone", "" + phone);
                        hashMap.put("dlvryFee", "" + dlvryFee);
                        hashMap.put("country", "" + country);
                        hashMap.put("cp", "" + cp);
                        hashMap.put("city", "" + city);
                        hashMap.put("address", "" + address);
                        hashMap.put("lat", "" + lat);
                        hashMap.put("lon", "" + lon);
                        hashMap.put("shopOpen", "true");
                        hashMap.put("profileImage", "" + downloadUri);

                        // guardar los datos en la base de datos de firebase
                        DatabaseReference ref =
                                FirebaseDatabase.getInstance("https://grocery-26758-default-rtdb.europe-west1.firebasedatabase.app/")
                                        .getReference("Users");
                        ref.child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(hashMap)
                                .addOnSuccessListener(aVoid -> {
                                    // Datos guardados correctamente
                                    progressDialog.dismiss();
                                    Toast.makeText(ProfileEditSellerActivity.this,
                                            "Cuenta actualizada correctamente...",
                                            Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Error al cargar la imagen
                    progressDialog.dismiss();
                    Toast.makeText(ProfileEditSellerActivity.this,
                            "Error al cargar la imagen, por favor, intenta de nuevo..." + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void updateProfileWithoutImage() {
        if (image_uri == null) {
            // guardar info sin imagen

            // configurar datos a guardar
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name", "" + name);
            hashMap.put("shopName", "" + shopName);
            hashMap.put("phone", "" + phone);
            hashMap.put("dlvryFee", "" + dlvryFee);
            hashMap.put("country", "" + country);
            hashMap.put("cp", "" + cp);
            hashMap.put("city", "" + city);
            hashMap.put("address", "" + address);
            hashMap.put("lat", "" + lat);
            hashMap.put("lon", "" + lon);
            hashMap.put("shopOpen", "true");

            // guardar datos en la base de datos de Firebase
            DatabaseReference ref =
                    FirebaseDatabase.getInstance("https://grocery-26758-default-rtdb.europe-west1.firebasedatabase.app/")
                            .getReference("Users");
            ref.child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(hashMap)
                    .addOnSuccessListener(aVoid -> {
                        // Datos actualizados correctamente
                        progressDialog.dismiss();
                        Toast.makeText(ProfileEditSellerActivity.this,
                                "Cuenta actualizada correctamente...",
                                Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Error al actualizar los datos
                        progressDialog.dismiss();
                        Toast.makeText(ProfileEditSellerActivity.this,
                                "Error al guardar los datos, por favor, intenta de nuevo..." + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            // User is signed in
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        } else {
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://grocery-26758-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        for (DataSnapshot ds : snap.getChildren()) {
                            String uid = "" + ds.child("uid").getValue();
                            String name = "" + ds.child("name").getValue();
                            String shopName = "" + ds.child("shopName").getValue();
                            String email = "" + ds.child("email").getValue();
                            String phone = "" + ds.child("phone").getValue();
                            String dlvryFee = "" + ds.child("dlvryFee").getValue();
                            String country = "" + ds.child("country").getValue();
                            String cp = "" + ds.child("cp").getValue();
                            String city = "" + ds.child("city").getValue();
                            String address = "" + ds.child("address").getValue();
                            String timestamp = "" + ds.child("timestamp").getValue();
                            lat = Double.parseDouble("" + ds.child("lat").getValue());
                            lon = Double.parseDouble("" + ds.child("lon").getValue());
                            String accountType = "" + ds.child("accountType").getValue();
                            String online = "" + ds.child("online").getValue();
                            String shopOpen = "" + ds.child("shopOpen").getValue();
                            String profileImg = "" + ds.child("profileImg").getValue();

                            binding.nameEt.setText(name);
                            binding.shopNameEt.setText(shopName);
                            binding.phoneEt.setText(phone);
                            binding.deliveryFeeEt.setText(dlvryFee);
                            binding.countryEt.setText(country);
                            binding.postalCodeEt.setText(cp);
                            binding.cityEt.setText(city);
                            binding.completeAddressEt.setText(address);
                            binding.shopOpenSwitch.setChecked(shopOpen.equals("true"));

                            try {
                                Picasso.get().load(profileImg).placeholder(R.drawable.ic_store_gray)
                                        .into(binding.profileIv);
                            } catch (Exception e) {
                                binding.profileIv.setImageResource(R.drawable.ic_person_gray);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProfileEditSellerActivity.this,
                                "Error al cargar información",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showImagePickDialog() {
        // seleccionar imagen de perfil desde la galeria
        String[] options = {"Camera", "Gallery"};
        // crear un nuevo dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen desde:")
                .setItems(options, (dialogInterface, which) -> {
                    // seleccionar opcion
                    if (which == 0) {
                        // seleccionar imagen desde la camara
                        if (checkCameraPermission()) {
                            pickFromCamera();
                        } else {
                            requestCameraPermission();
                        }
                    } else {
                        // seleccionar imagen desde la galeria
                        if (checkStoragePermission()) {
                            pickFromGallery();
                        } else {
                            requestStoragePermission();
                        }
                    }
                })
                .show();
    }

    private void pickFromCamera() {
        // seleccionar imagen de perfil desde la camara
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        // insertar imagen en la galeria
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // abrir camara
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        // abrir galeria
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void findAddress() {
        // buscar direccion, pais, ciudad, etc.
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);

            String address = addresses.get(0).getAddressLine(0); // direccion completa
            String city = addresses.get(0).getLocality(); // ciudad
            String country = addresses.get(0).getCountryName(); // pais
            String postalCode = addresses.get(0).getPostalCode(); // codigo postal

            // configurar direcciones
            binding.cityEt.setText(city);
            binding.countryEt.setText(country);
            binding.postalCodeEt.setText(postalCode);
            binding.completeAddressEt.setText(address);
        } catch (Exception e) {
            Toast.makeText(this, "Error al detectar la ubicación" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void detectLocation() {
        Toast.makeText(this, "Detectando ubicación, por favor, espere...", Toast.LENGTH_SHORT).show();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                (PackageManager.PERMISSION_GRANTED);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, locationPermissions, GPS_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        // verificar si tiene permisos de camara
        boolean result0 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return result0 && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // ubicacion detectada
        lat = location.getLatitude();
        lon = location.getLongitude();

        findAddress();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        // gps/ubicacion deshabilitada
        Toast.makeText(this, "GPS deshabilitado, por favor actívelo", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case GPS_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted) {
                        // habilitar el boton de ubicacion, permission granted
                        detectLocation();
                    } else {
                        // mostrar mensaje de error, permission denied
                        Toast.makeText(this, "El permiso de ubicación es necesario...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        // habilitar el boton de camara, permission granted
                        pickFromCamera();
                    } else {
                        // mostrar mensaje de error, permission denied
                        Toast.makeText(this, "El permiso de camara es necesario...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        // habilitar el boton de galeria, permission granted
                        pickFromGallery();
                    } else {
                        // mostrar mensaje de error, permission denied
                        Toast.makeText(this, "El permiso de almacenamiento es necesario...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // imagen seleccionada de la galeria
                image_uri = data.getData();
                binding.profileIv.setImageURI(image_uri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // imagen seleccionada de la camara
                binding.profileIv.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}