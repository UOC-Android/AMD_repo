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
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.andresgqjob.grocery.databinding.ActivityRegisterUserBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RegisterUserActivity extends AppCompatActivity implements LocationListener
{
    private ActivityRegisterUserBinding binding;

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
        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // init permission arrays
        locationPermissions = new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION };
        storagePermissions = new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE };
        cameraPermissions = new String[] { Manifest.permission.CAMERA };

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Cargando, por favor, espere...");
        progressDialog.setCanceledOnTouchOutside(false);

        // Iniciar las vistas UI
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

        binding.registerBtn.setOnClickListener(view -> {
            // registrar usuario
            inputData();
        });

        binding.registerSellerTv.setOnClickListener(view -> {
            // abrir actividad de registro de vendedor
            startActivity(new Intent(RegisterUserActivity.this, RegisterSellerActivity.class));
        });
    }

    private String name;
    private String phone;
    private String country;
    private String cp;
    private String city;
    private String address;
    private String email;
    private String pass;

    private void inputData() {
        // input data
        name = binding.nameEt.getText().toString().trim();
        phone = binding.phoneEt.getText().toString().trim();
        country = binding.countryEt.getText().toString().trim();
        cp = binding.postalCodeEt.getText().toString().trim();
        city = binding.cityEt.getText().toString().trim();
        address = binding.completeAddressEt.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();
        pass = binding.passwordEt.getText().toString().trim();
        String cPass = binding.cpasswordEt.getText().toString().trim();
        // validate data
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Por favor, introduce tu nombre...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Por favor, introduce tu numero de telefono...", Toast.LENGTH_SHORT).show();
        }
        if (lat == 0.0 || lon == 0.0) {
            Toast.makeText(this, "Por favor, haz click en el botón GPS para detectar tu ubicación...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(country)) {
            Toast.makeText(this, "Por favor, introduce el pais...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(cp)) {
            Toast.makeText(this, "Por favor, introduce el codigo postal...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Por favor, introduce la ciudad...", Toast.LENGTH_SHORT).show();
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email inválido, por favor, introduce tu email correctamente...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Por favor, introduce tu direccion...", Toast.LENGTH_SHORT).show();
        }
        if (pass.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres...", Toast.LENGTH_SHORT).show();
        }
        if (!pass.equals(cPass)) {
            Toast.makeText(this, "La contaseña no coincide...", Toast.LENGTH_SHORT).show();
        }

        createAccount();
    }

    private void createAccount() {
        progressDialog.setMessage("Creando cuenta. Por favor, espera...");
        progressDialog.show();

        // crear cuenta de usuario
        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(authResult -> {
                    // Cuenta creada correctamente
                    saverFirebaseData();
                })
                .addOnFailureListener(e -> {
                    // Error al crear la cuenta
                    progressDialog.dismiss();
                    Toast.makeText(RegisterUserActivity.this,
                            "Error al crear la cuenta, por favor, intenta de nuevo..." + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void saverFirebaseData() {
        progressDialog.setMessage("Guardando datos. Por favor, espera...");
        String timestamp = "" + System.currentTimeMillis();

        if (image_uri == null) {
            // guardar info sin imagen

            // configurar datos a guardar
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid", "" + firebaseAuth.getUid());
            hashMap.put("name", "" + name);
            hashMap.put("phone", "" + phone);
            hashMap.put("country", "" + country);
            hashMap.put("cp", "" + cp);
            hashMap.put("city", "" + city);
            hashMap.put("email", "" + email);
            hashMap.put("address", "" + address);
            hashMap.put("lat", "" + lat);
            hashMap.put("lon", "" + lon);
            hashMap.put("timestamp", "" + timestamp);
            hashMap.put("accountType", "Usuario");
            hashMap.put("online", "true");
            hashMap.put("profileImg", "");

            // guardar datos en la base de datos de Firebase
            DatabaseReference ref = FirebaseDatabase.getInstance("https://grocery-26758-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
            ref.child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(hashMap)
                    .addOnSuccessListener(aVoid -> {
                        // Datos guardados correctamente
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUserActivity.this,
                                "Cuenta creada correctamente...",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterUserActivity.this, MainUserActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // Error al guardar los datos
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUserActivity.this,
                                "Error al guardar los datos, por favor, intenta de nuevo..." + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });

        } else {
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
                            hashMap.put("uid", "" + firebaseAuth.getUid());
                            hashMap.put("name", "" + name);
                            hashMap.put("phone", "" + phone);
                            hashMap.put("country", "" + country);
                            hashMap.put("cp", "" + cp);
                            hashMap.put("city", "" + city);
                            hashMap.put("email", "" + email);
                            hashMap.put("address", "" + address);
                            hashMap.put("lat", "" + lat);
                            hashMap.put("lon", "" + lon);
                            hashMap.put("timestamp", "" + timestamp);
                            hashMap.put("accountType", "Usuario");
                            hashMap.put("online", "true");
                            hashMap.put("profileImg", "" + downloadUri); // url de la imagen

                            // guardar los datos en la base de datos de firebase
                            DatabaseReference ref = FirebaseDatabase.getInstance("https://grocery-26758-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
                            ref.child(Objects.requireNonNull(firebaseAuth.getUid())).setValue(hashMap)
                                    .addOnSuccessListener(aVoid -> {
                                        // Datos guardados correctamente
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterUserActivity.this,
                                                "Cuenta creada correctamente...",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterUserActivity.this, MainUserActivity.class));
                                        finish();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Error al cargar la imagen
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUserActivity.this,
                                "Error al cargar la imagen, por favor, intenta de nuevo..." + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });

        }
    }

    private void showImagePickDialog() {
        // seleccionar imagen de perfil desde la galeria
        String[] options = {"Camera", "Gallery"};
        // crear un nuevo dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen desde")
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
                }).show();
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
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void detectLocation() {
        Toast.makeText(this, "Detectando ubicación, por favor, espere...", Toast.LENGTH_SHORT).show();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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
