package com.cypher.sffilmfinder.data.films;


import io.reactivex.Single;
import retrofit2.http.GET;

interface FilmApi {
    @GET("/api/views/yitu-d5am/rows.json?accessType=DOWNLOAD")
    Single<FilmTableEntity> getFilmDump();
}
