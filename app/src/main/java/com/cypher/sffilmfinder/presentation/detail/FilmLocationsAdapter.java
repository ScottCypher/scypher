package com.cypher.sffilmfinder.presentation.detail;


import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cypher.sffilmfinder.R;

import java.util.Collections;
import java.util.List;

class FilmLocationsAdapter extends RecyclerView.Adapter<FilmLocationViewHolder> {

    private final LocationSelectionListener locationSelectionListener;
    private List<String> locations;
    private String currentLocation;

    FilmLocationsAdapter(LocationSelectionListener locationSelectionListener) {
        this.locationSelectionListener = locationSelectionListener;
        locations = Collections.emptyList();
    }

    void setLocations(List<String> locations, @Nullable String currentLocation) {
        this.currentLocation = currentLocation;
        this.locations = locations;
        notifyDataSetChanged();
    }

    @Override
    public FilmLocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.film_detail_location_item, parent, false);
        return new FilmLocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FilmLocationViewHolder holder, int position) {
        String location = locations.get(position);
        boolean isSelected = location.equals(currentLocation);
        holder.show(location, isSelected);
        holder.itemView.setOnClickListener(view -> {
            int lastSelected = locations.indexOf(currentLocation);
            currentLocation = location;
            notifyItemChanged(lastSelected);
            locationSelectionListener.onLocationSelected(location);
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public interface LocationSelectionListener {
        void onLocationSelected(String location);
    }

}
