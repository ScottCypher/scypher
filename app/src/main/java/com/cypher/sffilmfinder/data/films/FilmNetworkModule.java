package com.cypher.sffilmfinder.data.films;

import com.cypher.sffilmfinder.data.RetrofitUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FilmNetworkModule {

    private static final String BASE_URL = "https://data.sfgov.org";

    @Provides
    @Singleton
    FilmApi providesFilmApi() {
        return RetrofitUtil.createRetrofit(BASE_URL)
                .create(FilmApi.class);
    }
}
