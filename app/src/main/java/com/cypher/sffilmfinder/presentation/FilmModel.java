package com.cypher.sffilmfinder.presentation;


import android.content.Context;

import com.cypher.sffilmfinder.R;

import java.util.Collections;
import java.util.List;

public class FilmModel {
    private final String title;
    private final String year;
    private final List<String> locations;
    private final String id;
    private final Context context;
    private final String funFacts;
    private final String actor1;
    private final String actor2;
    private final String actor3;

    public FilmModel(Context context, String id, String title, String year, List<String> locations,
                     String funFacts, String actor1, String actor2, String actor3) {
        this.context = context;
        this.id = id;
        this.title = title;
        this.year = year;
        this.locations = locations;
        this.funFacts = funFacts;
        this.actor1 = actor1;
        this.actor2 = actor2;
        this.actor3 = actor3;
    }

    public String getFunFacts() {
        return funFacts;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getLocationsFormatted() {
        String delimiter = context.getString(R.string.locations_delimiter);
        return ViewUtils.toString(locations, delimiter);
    }

    public List<String> getLocations() {
        return locations == null ? Collections.emptyList() : locations;
    }

    @Override
    public String toString() {
        return "FilmModel{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getActor1() {
        return actor1;
    }

    public String getActor2() {
        return actor2;
    }

    public String getActor3() {
        return actor3;
    }
}
