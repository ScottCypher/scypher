package com.cypher.sffilmfinder.data.maps;


import com.cypher.sffilmfinder.BuildConfig;

import javax.inject.Inject;

import io.reactivex.Single;

public class GeocodeService {
    private static final String API_KEY = BuildConfig.GEOCODING_API_KEY;
    private static final String SF_BOUNDS = "37.7077410, -122.5085260|37.8094634, -122.3969350";

    private final GeocodeApi geocodeApi;

    @Inject
    GeocodeService(GeocodeApi geocodeApi) {
        this.geocodeApi = geocodeApi;
    }

    public Single<Geocode> getGeoCode(String address) {
        return geocodeApi.getGeocode(address, API_KEY, SF_BOUNDS);
    }
}
