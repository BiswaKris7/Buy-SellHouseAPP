package com.example.buysellhouse;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.provider.MediaStore;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;

public class ListProperty extends AppCompatActivity {

    DatabaseHelper dbHelper;

    private Uri selectedImageUri;

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_CAPTURE_IMAGE = 2;

    private static final int REQUEST_PERMISSION = 3;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_property);
        dbHelper = new DatabaseHelper(this);

        // Find views by ID
        Button submitBtn = findViewById(R.id.submit_button);
        Button uploadImage = findViewById(R.id.uploadImageButton);
        imageView = findViewById(R.id.propertyImageView);
        EditText address = findViewById(R.id.streetAddressInput);
        EditText numOfBeds = findViewById(R.id.bedsInput);
        EditText numOfBath = findViewById(R.id.bathInput);
        EditText propertyType = findViewById(R.id.PropertyTypeInput);
        EditText squareFeet = findViewById(R.id.SquareFeetInput);
        EditText askingPrice = findViewById(R.id.AskingPriceInput);





        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionAndOpenCamera();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Retrieve the values when the button is clicked
                        String propertyAddress = address.getText().toString().trim();
                        String beds = numOfBeds.getText().toString().trim();
                        String baths = numOfBath.getText().toString().trim();
                        String type = propertyType.getText().toString().trim();
                        String area = squareFeet.getText().toString().trim();
                        String price = askingPrice.getText().toString().trim();

                        String imageUriString = selectedImageUri != null ? selectedImageUri.toString() : "N/A";

                        // Create the Property object
                        Property listing = new Property(propertyAddress, beds, baths, type, area, price, imageUriString);

                        // Insert into the database
                        String message = dbHelper.addListing(listing);

                        // Display toast message
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

            }
        });
    }


    private void checkPermissionAndOpenCamera(){

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION);

        } else {

            openCamera();
        }
    }

    private void openCamera(){

        File photoFile = createImageFile();

        if(photoFile !=null){

            selectedImageUri = FileProvider.getUriForFile(this,"com.example.buysellhouse.fileprovider",photoFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
        }
    }

    private File createImageFile() {
        // Create an image file name
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAPTURE_IMAGE) {
                // Use selectedImageUri to display the image captured from the camera
                imageView.setImageURI(selectedImageUri); // Ensure you're using the same variable
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                selectedImageUri = data.getData(); // Store the selected image URI
                if (selectedImageUri != null) {
                    // Display the selected image in the ImageView
                    imageView.setImageURI(selectedImageUri);
                }
            }
        }
    }
}
