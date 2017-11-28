package com.cypher.sffilmfinder.data.films;


import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class FilmTableEntity {

    @SerializedName("data")
    List<JsonArray> filmRows;

    @SerializedName("meta")
    MetaEntity metaEntity;

    static class MetaEntity {

        @SerializedName("view")
        ViewEntity viewEntity;

        static class ViewEntity {
            @SerializedName("columns")
            List<ColumnEntity> columnEntities;

            static class ColumnEntity {
                @SerializedName("fieldName")
                String fieldName;
            }
        }

    }

}
