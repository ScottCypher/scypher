package com.cypher.sffilmfinder.presentation.overview;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cypher.sffilmfinder.R;
import com.cypher.sffilmfinder.presentation.FilmModel;

import butterknife.BindView;
import butterknife.ButterKnife;

class FilmViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.film_image)
    ImageView filmImage;

    @BindView(R.id.film_location)
    TextView filmLocation;

    @BindView(R.id.film_title)
    TextView filmTitle;


    FilmViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void show(FilmModel filmModel) {
        //TODO(scypher) imageProvider.setImage(filmImage, filmModel);
        String title = itemView.getContext().getString(R.string.film_overview_item_title_and_year_f,
                filmModel.getTitle(), filmModel.getYear());
        filmTitle.setText(title);
        filmLocation.setText(filmModel.getLocationsFormatted());
    }
}
