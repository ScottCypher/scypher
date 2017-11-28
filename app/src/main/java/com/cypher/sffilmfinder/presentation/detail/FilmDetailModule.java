package com.cypher.sffilmfinder.presentation.detail;

import dagger.Module;
import dagger.Provides;

@Module
public class FilmDetailModule {

    @Provides
    FilmDetailContract.Presenter providesFilmOverviewPresenter(
            FilmDetailPresenter filmDetailPresenter) {
        return filmDetailPresenter;
    }

}
