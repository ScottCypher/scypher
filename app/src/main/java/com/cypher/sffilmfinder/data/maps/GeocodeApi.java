package com.cypher.sffilmfinder.data.maps;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface GeocodeApi {
    @GET("/maps/api/geocode/json")
    Single<Geocode> getGeocode(@Query("address") String address,
                               @Query("key") String key,
                               @Query("bounds") String bounds);
}
