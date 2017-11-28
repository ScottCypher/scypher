package com.cypher.sffilmfinder.presentation.detail;


import com.cypher.sffilmfinder.data.maps.Location;

import javax.inject.Inject;

class CoordinatesModelMapper {
    @Inject
    CoordinatesModelMapper() {

    }

    LocationModel toLocationModel(Location location) {
        return new LocationModel(location.getLatitude(), location.getLongitude());
    }
}
