package com.ukgeek.sheetcounter.app.managers;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;

import com.ukgeek.sheetcounter.app.factories.FactoryTypeface;

public class ManagerTypeface {
    private static final SparseArray<Typeface> typefacesCache = new SparseArray<>();

    public static Typeface getTypeface(Context context, int typeface) {
        synchronized (typefacesCache) {
            if (typefacesCache.indexOfKey(typeface) < 0) {
                typefacesCache.put(typeface, FactoryTypeface.createTypeface(context, typeface));
            }

            return typefacesCache.get(typeface);
        }
    }
}
