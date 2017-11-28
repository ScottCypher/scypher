package com.cypher.sffilmfinder.presentation.overview;

import dagger.Module;
import dagger.Provides;

@Module
public class FilmOverviewModule {

    @Provides
    FilmOverviewContract.Presenter providesFilmOverviewPresenter(
            FilmOverviewPresenter filmOverviewPresenter) {
        return filmOverviewPresenter;
    }

}
