package com.cypher.sffilmfinder.presentation.overview;


import com.cypher.sffilmfinder.presentation.FilmModel;

import java.util.Comparator;

/**
 * Sorts FilmModels in the following order
 * 1) Title
 * 2) Year
 */
class TitleFilmComparator implements Comparator<FilmModel> {
    @Override
    public int compare(FilmModel o1, FilmModel o2) {
        int titleComp = ComparatorUtils.compareNullable(o1.getTitle(), o2.getTitle());
        if (titleComp != 0) return titleComp;

        return ComparatorUtils.compareNullable(o1.getYear(), o2.getYear());
    }
}
