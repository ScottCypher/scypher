package com.cypher.sffilmfinder.data.films;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

class FilmConsolidator {

    @Inject
    FilmConsolidator() {

    }

    List<Film> consolidateFilms(List<Film> films) {
        Map<Film, Film> filmMap = new HashMap<>();
        for (Film film : films) {
            if (filmMap.containsKey(film)) {
                Film existingFilm = filmMap.get(film);
                List<String> newLocations = film.getLocations();
                if (newLocations != null) {
                    existingFilm.locations.addAll(newLocations);
                }
            } else {
                filmMap.put(film, film);
            }
        }
        return Collections.list(Collections.enumeration(filmMap.values()));
    }
}
