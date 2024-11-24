package com.example.buysellhouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SellerDashboard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListingAdapter listingAdapter;
    private List<Property> propertyList;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        Button addListingBtn = findViewById(R.id.button_add_new_listing);

        Button deleteAll = findViewById(R.id.deleteAll);

        dbHelper = new DatabaseHelper(this);

        // Fetch initial data for properties
        propertyList = dbHelper.getAllProperties();

        recyclerView = findViewById(R.id.viewListing);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up the adapter with the property list
        listingAdapter = new ListingAdapter(propertyList);
        recyclerView.setAdapter(listingAdapter);

        // Button to add a new listing
        addListingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerDashboard.this, ListProperty.class);
                startActivity(intent);
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbHelper.deleteAllProperties();
                onResume();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPropertiesList();  // Refresh the list when returning to this activity
    }

    // Method to refresh the property list
    private void refreshPropertiesList() {
        // Fetch updated properties from the database
        propertyList.clear(); // Clear the existing list
        propertyList.addAll(dbHelper.getAllProperties()); // Add all new properties
        listingAdapter.notifyDataSetChanged(); // Notify adapter to refresh RecyclerView
    }
}
