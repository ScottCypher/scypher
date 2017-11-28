package com.cypher.sffilmfinder.domain;


import com.cypher.sffilmfinder.data.films.Film;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class SearchFilmList {

    private final GetFilmList getFilmList;

    @Inject
    SearchFilmList(GetFilmList getFilmList) {
        this.getFilmList = getFilmList;
    }

    public Single<List<Film>> searchFilms(String query) {
        return getFilmList.getFilms()
                .flatMap(films -> getMatchingFilms(films, query));
    }

    private Single<List<Film>> getMatchingFilms(List<Film> films, String query) {
        String lowerQuery = query.toLowerCase();
        return Observable.just(films)
                .flatMapIterable(x -> x)
                .filter(film -> {
                    LinkedList<String> checkedFields = new LinkedList<>();
                    checkedFields.add(film.getTitle());
                    checkedFields.add(film.getReleaseYear());
                    List<String> locations = film.getLocations();
                    if (locations != null) {
                        checkedFields.addAll(film.getLocations());
                    }
                    for (String checkedField : checkedFields) {
                        if (checkedField != null
                                && checkedField.toLowerCase().contains(lowerQuery)) {
                            return true;
                        }
                    }
                    return false;
                })
                .toList();
    }

}
