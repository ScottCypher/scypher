package com.cypher.sffilmfinder.data.films;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class FilmService {

    private final FilmDao filmDao;
    private final FilmApi filmApi;
    private final FilmTableEntityMapper filmTableEntityMapper;
    private final FilmConsolidator filmConsolidator;

    @Inject
    FilmService(FilmDao filmDao,
                FilmApi filmApi,
                FilmTableEntityMapper filmTableEntityMapper,
                FilmConsolidator filmConsolidator) {
        this.filmDao = filmDao;
        this.filmApi = filmApi;
        this.filmTableEntityMapper = filmTableEntityMapper;
        this.filmConsolidator = filmConsolidator;
    }

    public Single<List<Film>> getFilmsFromServer() {
        return filmApi.getFilmDump()
                .map(filmTableEntityMapper::filmTableEntityToFilms)
                .map(filmConsolidator::consolidateFilms)
                .doOnSuccess(filmDao::insertAll);
    }

}
