package com.example.buysellhouse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.NoteViewHolder> {

    private List<Property> propertyList;

    public ListingAdapter(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listing_layout, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Property listing = propertyList.get(position);

        // Bind property details
        holder.PropertyAddressView.setText(listing.getPropertyAddress());
        holder.PropertyTypeView.setText(listing.getType());
        holder.AskingPriceView.setText(listing.getPrice());
        holder.NumOfBedView.setText(listing.getBeds());
        holder.NumOfBathView.setText(listing.getBaths());

        // Load image using Glide
        Glide.with(holder.imageView.getContext())
                .load(listing.getUri())
                .into(holder.imageView);

        // Initialize and configure the MapView
        holder.mapView.onCreate(null);
        holder.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                // Example: Add a marker at a fixed location (modify for dynamic data)
                LatLng propertyLocation = new LatLng(43.8976, 78.8636); // Replace with actual latitude and longitude
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(propertyLocation, 10f));
            }
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    @Override
    public void onViewRecycled(@NonNull NoteViewHolder holder) {
        super.onViewRecycled(holder);
        holder.mapView.onDestroy();
    }

    // Call these lifecycle methods from your hosting activity or fragment
    public void onMapViewResume(@NonNull RecyclerView recyclerView) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View view = recyclerView.getChildAt(i);
            NoteViewHolder holder = (NoteViewHolder) recyclerView.getChildViewHolder(view);
            if (holder != null && holder.mapView != null) {
                holder.mapView.onResume();
            }
        }
    }

    public void onMapViewPause(@NonNull RecyclerView recyclerView) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View view = recyclerView.getChildAt(i);
            NoteViewHolder holder = (NoteViewHolder) recyclerView.getChildViewHolder(view);
            if (holder != null && holder.mapView != null) {
                holder.mapView.onPause();
            }
        }
    }

    public void onMapViewDestroy(@NonNull RecyclerView recyclerView) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View view = recyclerView.getChildAt(i);
            NoteViewHolder holder = (NoteViewHolder) recyclerView.getChildViewHolder(view);
            if (holder != null && holder.mapView != null) {
                holder.mapView.onDestroy();
            }
        }
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView PropertyTypeView;
        TextView PropertyAddressView;
        TextView AskingPriceView;
        TextView NumOfBedView;
        TextView NumOfBathView;
        ImageView imageView;
        MapView mapView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find views by ID
            PropertyTypeView = itemView.findViewById(R.id.PropertyTypeTxt);
            PropertyAddressView = itemView.findViewById(R.id.PropertyAddressTxt);
            AskingPriceView = itemView.findViewById(R.id.AskingPriceTxt);
            NumOfBedView = itemView.findViewById(R.id.NumBedsTxt);
            NumOfBathView = itemView.findViewById(R.id.NumBathTxt);
            imageView = itemView.findViewById(R.id.imageViewProperty);
            mapView = itemView.findViewById(R.id.mapView);
        }
    }
}
