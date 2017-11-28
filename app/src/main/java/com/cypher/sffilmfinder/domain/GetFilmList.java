package com.cypher.sffilmfinder.domain;

import com.cypher.sffilmfinder.data.films.Film;
import com.cypher.sffilmfinder.data.films.FilmDao;
import com.cypher.sffilmfinder.data.films.FilmService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class GetFilmList {

    private final FilmDao filmDao;
    private final FilmService filmService;
    private Single<List<Film>> querySource;

    @Inject
    GetFilmList(FilmDao filmDao, FilmService filmService) {
        this.filmDao = filmDao;
        this.filmService = filmService;
    }

    public Single<List<Film>> getFilms() {
        if (querySource == null) {
            querySource = Single.fromCallable(filmDao::getAll)
                    .flatMap(list -> list.isEmpty()
                            ? filmService.getFilmsFromServer()
                            : Single.just(list))
                    .cache()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
        return querySource;
    }

    public Single<List<Film>> retryGetFilms() {
        querySource = null;
        return getFilms();
    }

}
