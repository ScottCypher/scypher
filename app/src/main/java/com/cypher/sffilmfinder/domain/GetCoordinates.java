package com.cypher.sffilmfinder.domain;


import com.cypher.sffilmfinder.data.maps.Geocode;
import com.cypher.sffilmfinder.data.maps.GeocodeService;
import com.cypher.sffilmfinder.data.maps.Location;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GetCoordinates {
    private static final Location DEFAULT_LOCATION =
            new Location(37.7749, -122.4194);
    private static final String DEFAULT_LOCATION_NAME = "San Francisco";

    private final GeocodeService geocodeService;

    @Inject
    GetCoordinates(GeocodeService geocodeService) {
        this.geocodeService = geocodeService;
    }

    public Location getDefaultLocation() {
        return DEFAULT_LOCATION;
    }

    public String getDefaultLocationName() {
        return DEFAULT_LOCATION_NAME;
    }

    public Single<Location> getCoordinates(String address) {
        return geocodeService.getGeoCode(address)
                .map(geocode -> {
                    if (geocode.results.isEmpty()) {
                        return DEFAULT_LOCATION;
                    } else {
                        Geocode.Result result = geocode.results.get(0);
                        return result.geometry.location;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
