package com.cypher.sffilmfinder.presentation;


import android.content.Context;

import com.cypher.sffilmfinder.data.films.Film;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FilmModelMapper {

    private final Context context;

    @Inject
    FilmModelMapper(Context context) {
        this.context = context;
    }

    public List<FilmModel> toFilmModels(List<Film> films) {
        List<FilmModel> filmModels = new ArrayList<>();
        for (Film film : films) {
            FilmModel filmModel = toFilmModel(film);
            filmModels.add(filmModel);
        }
        return filmModels;
    }

    public FilmModel toFilmModel(Film film) {
        // Database contains duplicate actor names. Simplify this for view consumption
        String actor1 = film.getActor1();
        String actor2 = film.getActor2();
        String actor3 = film.getActor3();

        actor2 = actor1 == null || !actor1.equalsIgnoreCase(actor2) ? actor2 : null;
        actor3 = actor2 == null || !actor2.equalsIgnoreCase(actor3) ? actor3 : null;

        return new FilmModel(context, film.getId(), film.getTitle(),
                film.getReleaseYear(), film.getLocations(), film.getFunFacts(),
                actor1, actor2, actor3);
    }
}
