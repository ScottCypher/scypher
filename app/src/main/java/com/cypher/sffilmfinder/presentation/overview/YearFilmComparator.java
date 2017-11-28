package com.cypher.sffilmfinder.presentation.overview;


import com.cypher.sffilmfinder.presentation.FilmModel;

import java.util.Comparator;

/**
 * Sorts FilmModels in the following order
 * 1) Year
 * 2) Title
 */
class YearFilmComparator implements Comparator<FilmModel> {
    @Override
    public int compare(FilmModel o1, FilmModel o2) {
        int yearComp = ComparatorUtils.compareNullable(o1.getYear(), o2.getYear());
        if (yearComp != 0) return yearComp;

        return ComparatorUtils.compareNullable(o1.getTitle(), o2.getTitle());
    }
}
