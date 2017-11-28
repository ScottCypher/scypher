package com.cypher.sffilmfinder.data.maps;


import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("lat")
    double latitude;

    @SerializedName("lng")
    double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
