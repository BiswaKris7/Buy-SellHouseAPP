package com.example.buysellhouse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
// import android.widget.SearchView;
import androidx.appcompat.widget.SearchView;
import android.speech.RecognizerIntent;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BuyerDashboard extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PropertyAdapter adapter;
    private ArrayList<PropertyBuyer> propertyList;
    private SearchView searchView;
    private ImageButton voiceSearchButton;

    private static final int REQUEST_SPEECH_INPUT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_dashboard);

        recyclerView = findViewById(R.id.propertyRecyclerView);
        searchView = findViewById(R.id.searchView);
        voiceSearchButton = findViewById(R.id.voiceSearchButton);
        voiceSearchButton.setOnClickListener(v -> startVoiceRecognition());

        // Initialize property list
        propertyList = new ArrayList<>();

        // Get selected property types from Intent
        String propertyTypes = getIntent().getStringExtra("propertyTypes");
        Log.d("BuyerDashboard", "Property Types from Intent: " + propertyTypes);

        // Check if propertyTypes is not null
        if (propertyTypes != null && !propertyTypes.isEmpty()) {
            String[] typesArray = propertyTypes.split(",");
            for (String type : typesArray) {
                type = type.trim();
                // Create test property for each type
                propertyList.add(createTestProperty(type));
            }
        } else {
            // If no property types passed, display all test properties
            propertyList.add(new PropertyBuyer("Apartment", "101 River Rd", "Modern apartment with river views.", "$250,000"));
            propertyList.add(new PropertyBuyer("Bungalow", "202 Hilltop Ln", "Charming bungalow in a quiet neighborhood.", "$280,000"));
            propertyList.add(new PropertyBuyer("Single Family", "303 Forest Dr", "Large single-family home with a backyard.", "$420,000"));
            propertyList.add(new PropertyBuyer("Townhome", "404 Lakeview Blvd", "Stylish townhome with lake access.", "$360,000"));
            propertyList.add(new PropertyBuyer("Multi-Family", "505 Mountain Rd", "Multi-family property in prime location.", "$550,000"));
            propertyList.add(new PropertyBuyer("Condo", "606 Ocean Ave", "Condo with beachside views.", "$470,000"));
            propertyList.add(new PropertyBuyer("Apartment", "707 Valley Rd", "Cozy apartment near downtown.", "$210,000"));
            propertyList.add(new PropertyBuyer("Bungalow", "808 Sunset Dr", "Peaceful bungalow with mountain views.", "$290,000"));
            propertyList.add(new PropertyBuyer("Single Family", "909 Garden Way", "Single-family home with a spacious garden.", "$410,000"));
            propertyList.add(new PropertyBuyer("Townhome", "111 Brook St", "Modern townhome in a gated community.", "$370,000"));
            propertyList.add(new PropertyBuyer("Multi-Family", "222 Park Ln", "Multi-family property with great rental income.", "$530,000"));
            propertyList.add(new PropertyBuyer("Condo", "333 Birch St", "Upscale condo with rooftop amenities.", "$460,000"));
            propertyList.add(new PropertyBuyer("Apartment", "444 Maplewood Ave", "Affordable apartment in the suburbs.", "$230,000"));
            propertyList.add(new PropertyBuyer("Bungalow", "555 Cedarwood Dr", "Rustic bungalow with lots of character.", "$310,000"));
            propertyList.add(new PropertyBuyer("Single Family", "666 Evergreen Ln", "Family home in a school district.", "$450,000"));

        }

        // Set up adapter
        adapter = new PropertyAdapter(propertyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Set up SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false; // let the SearchView handle the default action
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false; // let the SearchView handle the default action
            }
        });
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        // Specify free form input
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        // Provide a hint to the recognizer about what the user is going to say
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now to search");

        // Start the activity, the intent will be populated with the speech text
        try {
            startActivityForResult(intent, REQUEST_SPEECH_INPUT);
        } catch (Exception e) {
            // Handle the exception if speech recognition is not available
            Toast.makeText(this, "Speech recognition is not available on this device.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SPEECH_INPUT && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (matches != null && matches.size() > 0) {
                String spokenText = matches.get(0);
                // Set the recognized text to the SearchView and perform search
                searchView.setQuery(spokenText, true);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private PropertyBuyer createTestProperty(String type) {
        switch (type) {
            case "Apartment":
                return new PropertyBuyer("Apartment", "123 Main St", "A lovely apartment in the city.", "$200,000");
            case "Bungalow":
                return new PropertyBuyer("Bungalow", "456 Elm St", "Cozy bungalow with garden.", "$300,000");
            case "Single Family":
                return new PropertyBuyer("Single Family", "789 Oak St", "Spacious single family home.", "$400,000");
            case "Townhome":
                return new PropertyBuyer("Townhome", "321 Pine St", "Modern townhome near park.", "$350,000");
            case "Multi-Family":
                return new PropertyBuyer("Multi-Family", "654 Maple St", "Multi-family property with great ROI.", "$500,000");
            case "Condo":
                return new PropertyBuyer("Condo", "987 Cedar St", "Luxury condo with amenities.", "$450,000");
            default:
                return new PropertyBuyer(type, "Unknown Address", "No description available.", "Price not available");
        }
    }
}
