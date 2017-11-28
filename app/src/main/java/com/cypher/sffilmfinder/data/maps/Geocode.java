package com.cypher.sffilmfinder.data.maps;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Geocode {
    @SerializedName("results")
    public List<Result> results;

    public static class Result {
        @SerializedName("geometry")
        public Geometry geometry;

        public static class Geometry {
            @SerializedName("location")
            public Location location;
        }
    }
}
