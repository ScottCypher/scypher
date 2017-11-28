package com.cypher.sffilmfinder.data.films;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Film.class}, version = 1)
@TypeConverters({StringListConverter.class})
abstract class FilmDatabase extends RoomDatabase {
    static final String NAME = "films";

    public abstract FilmDao filmDao();
}
