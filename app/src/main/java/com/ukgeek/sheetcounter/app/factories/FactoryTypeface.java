package com.ukgeek.sheetcounter.app.factories;

import android.content.Context;
import android.graphics.Typeface;

public class FactoryTypeface {
    public static Typeface createTypeface(Context context, int typeface) {
        return Typeface.createFromAsset(context.getAssets(),
                String.format("fonts/%s.ttf", context.getString(typeface)));
    }
}
