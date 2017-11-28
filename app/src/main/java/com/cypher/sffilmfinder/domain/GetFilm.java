package com.cypher.sffilmfinder.domain;

import com.cypher.sffilmfinder.data.films.Film;
import com.cypher.sffilmfinder.data.films.FilmDao;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GetFilm {

    private final FilmDao filmDao;

    @Inject
    GetFilm(FilmDao filmDao) {
        this.filmDao = filmDao;
    }

    public Single<Film> getFilm(String id) {
        return Single.fromCallable(() -> filmDao.findById(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
