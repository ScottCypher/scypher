package com.cypher.sffilmfinder.presentation;


import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public final class ViewUtils {
    private ViewUtils() {

    }

    public static String toString(List<?> list, String delimiter) {
        StringBuilder b = new StringBuilder();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                b.append(list.get(i).toString());
                if (i != list.size() - 1) {
                    b.append(delimiter);
                }
            }
        }
        return b.toString();
    }

    public static void setEnabled(boolean enable, View view) {
        view.setEnabled(enable);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setEnabled(enable, child);
            }
        }
    }
}
