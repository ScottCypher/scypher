package com.cypher.sffilmfinder.data.films;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface FilmDao {
    @Query("SELECT * FROM film")
    List<Film> getAll();

    @Insert
    void insertAll(List<Film> films);

    @Query("SELECT * FROM film WHERE id = :id LIMIT 1")
    Film findById(String id);
}
