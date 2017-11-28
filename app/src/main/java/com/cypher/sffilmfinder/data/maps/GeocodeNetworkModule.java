package com.cypher.sffilmfinder.data.maps;

import com.cypher.sffilmfinder.data.RetrofitUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GeocodeNetworkModule {

    private static final String BASE_URL = "https://maps.googleapis.com";

    @Provides
    @Singleton
    GeocodeApi providesGeocodeApi() {
        return RetrofitUtil.createRetrofit(BASE_URL)
                .create(GeocodeApi.class);
    }
}
