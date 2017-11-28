package com.cypher.sffilmfinder.data.films;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class Film {

    @PrimaryKey
    @NonNull
    @SerializedName(":id")
    String id;

    @ColumnInfo(name = "actor_1")
    @SerializedName("actor_1")
    String actor1;

    @ColumnInfo(name = "actor_2")
    @SerializedName("actor_2")
    String actor2;

    @ColumnInfo(name = "actor_3")
    @SerializedName("actor_3")
    String actor3;

    @ColumnInfo(name = "fun_facts")
    @SerializedName("fun_facts")
    String funFacts;

    @ColumnInfo(name = "locations")
    @SerializedName("locations")
    List<String> locations;

    @ColumnInfo(name = "release_year")
    @SerializedName("release_year")
    String releaseYear;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    String title;

    Film() {
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLocations() {
        return locations;
    }

    void setLocations(List<String> locations) {
        this.locations = locations;
    }

    @NonNull
    public String getId() {
        return id;
    }

    void setId(@NonNull String id) {
        this.id = id;
    }

    public String getFunFacts() {
        return funFacts;
    }

    void setFunFacts(String funFacts) {
        this.funFacts = funFacts;
    }

    public String getActor1() {
        return actor1;
    }

    void setActor1(String actor1) {
        this.actor1 = actor1;
    }

    public String getActor2() {
        return actor2;
    }

    void setActor2(String actor2) {
        this.actor2 = actor2;
    }

    public String getActor3() {
        return actor3;
    }

    void setActor3(String actor3) {
        this.actor3 = actor3;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Film film = (Film) o;

        if (releaseYear != null ? !releaseYear.equals(film.releaseYear) : film.releaseYear != null)
            return false;
        return title != null ? title.equals(film.title) : film.title == null;
    }

    @Override
    public int hashCode() {
        int result = releaseYear != null ? releaseYear.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}
