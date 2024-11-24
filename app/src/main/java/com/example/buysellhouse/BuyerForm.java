package com.example.buysellhouse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BuyerForm extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneNumEditText;
    private EditText numOfBedroomsEditText, numOfBathEditText, areaLocationEditText, squareFootageEditText;
    private CheckBox apartmentCheckBox, bungalowCheckBox, singleFamCheckBox, townhomeCheckBox, multiFamCheckBox, condoCheckBox;
    private Button submitBtn;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_form);

        // Initialize fields
        firstNameEditText = findViewById(R.id.FirstNameEditText);
        lastNameEditText = findViewById(R.id.LastNameEditText);
        emailEditText = findViewById(R.id.EmailEditText);
        phoneNumEditText = findViewById(R.id.PhoneNumEditText);
        numOfBedroomsEditText = findViewById(R.id.NumOfBedroomsEditText);
        numOfBathEditText = findViewById(R.id.NumOfBathEditText);
        areaLocationEditText = findViewById(R.id.AreaLocationEditText);
        squareFootageEditText = findViewById(R.id.SquareFootageEditText);
        apartmentCheckBox = findViewById(R.id.Apartment);
        bungalowCheckBox = findViewById(R.id.Bungalow);
        singleFamCheckBox = findViewById(R.id.SingleFam);
        townhomeCheckBox = findViewById(R.id.Townhome);
        multiFamCheckBox = findViewById(R.id.MultiFam);
        condoCheckBox = findViewById(R.id.Condo);
        submitBtn = findViewById(R.id.submitBtn);

        db = new DatabaseHelper(this);

        // Set click listener for submit button
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collect data from form fields
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phoneNumber = phoneNumEditText.getText().toString().trim();
                String numberOfBeds = numOfBedroomsEditText.getText().toString().trim();
                String numberOfBath = numOfBathEditText.getText().toString().trim();
                String areaLocation = areaLocationEditText.getText().toString().trim();
                String squareFootage = squareFootageEditText.getText().toString().trim();

                // Collect selected property types
                StringBuilder propertyTypesBuilder = new StringBuilder();
                if (apartmentCheckBox.isChecked()) propertyTypesBuilder.append("Apartment,");
                if (bungalowCheckBox.isChecked()) propertyTypesBuilder.append("Bungalow,");
                if (singleFamCheckBox.isChecked()) propertyTypesBuilder.append("Single Family,");
                if (townhomeCheckBox.isChecked()) propertyTypesBuilder.append("Townhome,");
                if (multiFamCheckBox.isChecked()) propertyTypesBuilder.append("Multi-Family,");
                if (condoCheckBox.isChecked()) propertyTypesBuilder.append("Condo,");

                // Remove trailing comma, if any
                String propertyTypes = propertyTypesBuilder.toString();
                if (propertyTypes.endsWith(",")) {
                    propertyTypes = propertyTypes.substring(0, propertyTypes.length() - 1);
                }

                Log.d("BuyerForm", "Property Types: " + propertyTypes);



                // Validate fields (simple validation)
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() ||
                        numberOfBeds.isEmpty() || numberOfBath.isEmpty() || areaLocation.isEmpty() ||
                        squareFootage.isEmpty() || propertyTypes.isEmpty()) {
                    Toast.makeText(BuyerForm.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add data to the database
                String result = db.addBuyerPreferences(firstName, lastName, email, phoneNumber, areaLocation, numberOfBeds, numberOfBath, squareFootage, propertyTypes);

                // Show toast message
                Toast.makeText(BuyerForm.this, result, Toast.LENGTH_SHORT).show();

                // Redirect to BuyerDashboardActivity if record was added successfully
                if (result.equals("Record Added Successfully!!")) {
                    Intent intent = new Intent(BuyerForm.this, BuyerDashboard.class);
                    intent.putExtra("propertyTypes", propertyTypes);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
