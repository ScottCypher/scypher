package com.cypher.sffilmfinder.presentation.overview;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cypher.sffilmfinder.R;
import com.cypher.sffilmfinder.presentation.FilmModel;

import java.util.Collections;
import java.util.List;

class FilmsAdapter extends RecyclerView.Adapter<FilmViewHolder> {

    private final FilmClickListener filmClickListener;
    private List<FilmModel> filmModels;

    FilmsAdapter(FilmClickListener filmClickListener) {
        this.filmClickListener = filmClickListener;
        filmModels = Collections.emptyList();
    }

    void setFilmModels(List<FilmModel> filmModels) {
        this.filmModels = filmModels;
        notifyDataSetChanged();
    }

    @Override
    public FilmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.film_overview_item, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmViewHolder holder, int position) {
        final FilmModel filmModel = filmModels.get(position);
        holder.show(filmModel);

        holder.itemView.setOnClickListener(v -> filmClickListener.onFilmClick(filmModel));
    }

    @Override
    public int getItemCount() {
        return filmModels.size();
    }

    interface FilmClickListener {
        void onFilmClick(FilmModel filmModel);
    }
}
