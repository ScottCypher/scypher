package com.cypher.sffilmfinder.presentation.overview;


public final class ComparatorUtils {
    private ComparatorUtils() {

    }

    public static int compareNullable(String o1, String o2) {
        if (o1 == null && o2 == null) return 0;
        if (o1 == null) return 1;
        if (o2 == null) return -1;
        else return o1.compareToIgnoreCase(o2);
    }
}
