package com.example.buysellhouse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.ViewHolder> implements Filterable {

    private List<PropertyBuyer> propertyList;
    private List<PropertyBuyer> propertyListFull; // For search functionality

    public PropertyAdapter(List<PropertyBuyer> propertyList) {
        this.propertyList = propertyList;
        propertyListFull = new ArrayList<>(propertyList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView propertyTypeTextView;
        public TextView propertyAddressTextView;
        public TextView propertyDescriptionTextView;
        public TextView propertyPriceTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            propertyTypeTextView = itemView.findViewById(R.id.propertyTypeTextView);
            propertyAddressTextView = itemView.findViewById(R.id.propertyAddressTextView);
            propertyDescriptionTextView = itemView.findViewById(R.id.propertyDescriptionTextView);
            propertyPriceTextView = itemView.findViewById(R.id.propertyPriceTextView);
        }
    }

    @Override
    public PropertyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PropertyBuyer currentItem = propertyList.get(position);

        holder.propertyTypeTextView.setText(currentItem.getType());
        holder.propertyAddressTextView.setText(currentItem.getAddress());
        holder.propertyDescriptionTextView.setText(currentItem.getDescription());
        holder.propertyPriceTextView.setText(currentItem.getPrice());

    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    // Implement Filterable methods
    @Override
    public Filter getFilter() {
        return propertyFilter;
    }

    private Filter propertyFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PropertyBuyer> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(propertyListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PropertyBuyer item : propertyListFull) {
                    if (item.getType().toLowerCase().contains(filterPattern) ||
                            item.getAddress().toLowerCase().contains(filterPattern) ||
                            item.getDescription().toLowerCase().contains(filterPattern) ||
                            item.getPrice().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            propertyList.clear();
            propertyList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
