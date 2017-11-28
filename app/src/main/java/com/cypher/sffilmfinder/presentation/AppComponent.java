package com.cypher.sffilmfinder.presentation;

import com.cypher.sffilmfinder.data.films.FilmDatabaseModule;
import com.cypher.sffilmfinder.data.films.FilmNetworkModule;
import com.cypher.sffilmfinder.data.maps.GeocodeNetworkModule;
import com.cypher.sffilmfinder.presentation.detail.FilmDetailFragment;
import com.cypher.sffilmfinder.presentation.detail.FilmDetailModule;
import com.cypher.sffilmfinder.presentation.overview.FilmOverviewFragment;
import com.cypher.sffilmfinder.presentation.overview.FilmOverviewModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        FilmDetailModule.class,
        FilmOverviewModule.class,
        FilmNetworkModule.class,
        FilmDatabaseModule.class,
        GeocodeNetworkModule.class
})
public interface AppComponent {
    void inject(FilmOverviewFragment filmOverviewFragment);

    void inject(FilmDetailFragment filmDetailFragment);
}
