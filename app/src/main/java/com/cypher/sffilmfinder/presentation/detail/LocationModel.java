package com.cypher.sffilmfinder.presentation.detail;


class LocationModel {
    private final double latitude;
    private final double longitude;

    LocationModel(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }

    double getLongitude() {
        return longitude;
    }

    double getLatitude() {
        return latitude;
    }
}
