package com.cypher.sffilmfinder.presentation.detail;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;

import com.cypher.sffilmfinder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class FilmLocationViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.film_detail_location_radio)
    RadioButton filmLocation;

    FilmLocationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void show(String location, boolean isSelected) {
        filmLocation.setChecked(isSelected);
        filmLocation.setText(location);
    }
}
