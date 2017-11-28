package com.cypher.sffilmfinder.data.films;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

class FilmTableEntityMapper {

    @Inject
    FilmTableEntityMapper() {

    }

    private Map<String, Field> getSerializedNameFieldMap(Class clazz) {
        Map<String, Field> serializedNameMap = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            SerializedName serializedName = field.getAnnotation(SerializedName.class);
            if (serializedName != null) {
                serializedNameMap.put(serializedName.value(), field);
            }
        }
        return serializedNameMap;
    }

    List<Film> filmTableEntityToFilms(FilmTableEntity filmTableEntity) throws IllegalAccessException {
        Map<String, Field> serializedNameMap = getSerializedNameFieldMap(Film.class);

        List<FilmTableEntity.MetaEntity.ViewEntity.ColumnEntity> columnEntities
                = filmTableEntity.metaEntity.viewEntity.columnEntities;
        List<Film> films = new LinkedList<>();
        for (JsonArray jsonElements : filmTableEntity.filmRows) {
            Film film = new Film();
            for (int i = 0; i < columnEntities.size(); i++) {
                FilmTableEntity.MetaEntity.ViewEntity.ColumnEntity columnEntity =
                        columnEntities.get(i);
                String fieldName = columnEntity.fieldName;

                if (serializedNameMap.containsKey(fieldName)) {
                    Field field = serializedNameMap.get(fieldName);
                    JsonElement value = jsonElements.get(i);
                    if (!value.isJsonNull()) {
                        String strValue = value.getAsString();

                        if (field.getType() == List.class) {
                            List<String> locations = new LinkedList<>();
                            locations.add(strValue);
                            field.set(film, locations);
                        } else {
                            field.set(film, strValue);
                        }
                    }
                }
            }
            films.add(film);
        }
        return films;
    }
}
