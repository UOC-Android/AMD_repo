package com.andresgqjob.expenseapp.ui;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.andresgqjob.expenseapp.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class TripEditActivity extends AppCompatActivity {
    ImageView tripImage;
    ImageButton btnChangeImage;
    String pictureAux;
    EditText tripName;
    Button btnSelectDate;
    TextView txtTripDate;
    String selectedDate;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_edit);
        tripName = findViewById(R.id.input_tripName);
        tripImage = findViewById(R.id.img_trip);
        btnChangeImage = findViewById(R.id.btn_changeImage);
        btnSelectDate = findViewById(R.id.btn_selectDate);
        txtTripDate = findViewById(R.id.txt_tripDate);
        txtTripDate.setText("Date:  ----");

        btnChangeImage.setOnClickListener(v -> {
            if (checkAndRequestPermissions(TripEditActivity.this)) {
                chooseImage(TripEditActivity.this);
            }
        });

        btnSelectDate.setOnClickListener(v -> {
            DatePickerDialog mDlgDatePicker = new DatePickerDialog(TripEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String date = year + "-" + (monthOfYear + 1 < 10 ? "0" : "") + (monthOfYear + 1) + "-"
                            + (dayOfMonth < 10 ? "0" : "") + dayOfMonth;
                    //Toast.makeText(view.getContext(),"Date selected: "+date,Toast.LENGTH_LONG).show();
                    selectedDate = date;
                    txtTripDate.setText("Date:  " + date);
                }
            }, 2014, 1, 1);
            mDlgDatePicker.show();
        });
    }

    private void chooseImage(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"}; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, (dialogInterface, i) -> {
            if (optionsMenu[i].equals("Take Photo")) {
                // Open the camera and get the photo
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            } else if (optionsMenu[i].equals("Choose from Gallery")) {
                // choose from  external storage
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            } else if (optionsMenu[i].equals("Exit")) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    // function to check permission
    public static boolean checkAndRequestPermissions(final Activity context) {
        int wExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (wExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[0]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(TripEditActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(TripEditActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(TripEditActivity.this);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        tripImage.setImageBitmap(selectedImage);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        pictureAux = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                tripImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                BitmapFactory.decodeFile(picturePath).compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                pictureAux = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            }
                        }
                    }
                    break;
            }
        }
    }
}

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
//                Map<String, Integer> perms = new HashMap<>();
//                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
//                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                // Fill with actual results from user
//                if (grantResults.length > 0) {
//                    for (int i = 0; i < permissions.length; i++)
//                        perms.put(permissions[i], grantResults[i]);
//                    // Check for both permissions
//                    if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
//                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                        // process the normal flow
//                        //else any one or both the permissions are not granted
//                    } else {
//                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
////                        // shouldShowRequestPermissionRationale will return true
//                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(TripEditActivity.this, Manifest.permission.CAMERA) ||
//                                ActivityCompat.shouldShowRequestPermissionRationale(TripEditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                            showDialogOK("Camera and Storage Permission required for this app",
//                                    (dialog, which) -> {
//                                        switch (which) {
//                                            case DialogInterface.BUTTON_POSITIVE:
//                                                checkAndRequestPermissions(TripEditActivity.this);
//                                                break;
//                                            case DialogInterface.BUTTON_NEGATIVE:
//                                                // proceed with logic by disabling the related features or quit the app.
//                                                break;
//                                        }
//                                    });
//                        }
//                        //permission is denied (and never ask again is  checked)
//                        //shouldShowRequestPermissionRationale will return false
//                        else {
//                            Toast.makeText(TripEditActivity.this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
//                                    .show();
//                            //                            //proceed with logic by disabling the related features or quit the app.
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
//        new AlertDialog.Builder(TripEditActivity.this)
//                .setMessage(message)
//                .setPositiveButton("OK", okListener)
//                .setNegativeButton("Cancel", okListener)
//                .create()
//                .show();
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (isEdit) {
//            Intent intent = new Intent();
//            intent.putExtra("trip", trip);
//            setResult(RESULT_OK, intent);
//        }
//        super.onBackPressed();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
//                Map<String, Integer> perms = new HashMap<>();
//                // Initial
//                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
//                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                // Fill with results
//                for (int i = 0; i < permissions.length; i++)
//                    perms.put(permissions[i], grantResults[i]);
//                // Check for ACCESS_FINE_LOCATION
//                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
//                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    // All Permissions Granted
//                    //proceed with logic by disabling the related features or quit the app.
//                } else {
//                    // Permission Denied
//                    Toast.makeText(TripEditActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
//                            .show();
//                }
//            }
//            break;
//            default:
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_CODE_PICK_IMAGE) {
//                if (data.getData() != null) {
//                    Uri uri = data.getData();
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                        trip.setImage(bitmap);
//                        imageView.setImageBitmap(bitmap);
//                    }
//                    catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.imageView:
//                if (ContextCompat.checkSelfPermission(TripEditActivity.this, Manifest.permission.CAMERA)
//                        != PackageManager.PERMISSION_GRANTED
//                        || ContextCompat.checkSelfPermission(TripEditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(TripEditActivity.this,
//                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//                } else {
//                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
//                }
//                break;
//            case R.id.button_save:
//                trip.setName(editTextName.getText().toString());
//                trip.setDescription(editTextDescription.getText().toString());
//                trip.setStartDate(editTextStartDate.getText().toString());
//                trip.setEndDate(editTextEndDate.getText().toString());
//                trip.setLatitude(editTextLatitude.getText().toString());
//                trip.setLongitude(editTextLongitude.getText().toString());
//                trip.setAddress(editTextAddress.getText().toString());
//                trip.setImage(imageView.getDrawingCache());
//                trip.setTripId(tripId);
//                if (isEdit) {
//                    db.updateTrip(trip);
//                } else {
//                    db.addTrip(trip);
//                }
//                Intent intent = new Intent();
//                intent.putExtra("trip", trip);
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
//            case R.id.button_cancel:
//                finish();
//                break;
//        }
//    }