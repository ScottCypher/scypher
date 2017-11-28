package com.cypher.sffilmfinder.data.films;


import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FilmDatabaseModule {

    @Singleton
    @Provides
    FilmDao providesFilmDatabase(Context context) {
        return Room.databaseBuilder(context, FilmDatabase.class, FilmDatabase.NAME)
                .build()
                .filmDao();
    }
}
