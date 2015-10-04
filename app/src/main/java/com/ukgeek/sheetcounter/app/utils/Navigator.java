package com.ukgeek.sheetcounter.app.utils;

import android.content.Context;
import android.content.Intent;

import com.ukgeek.sheetcounter.app.activities.DetailsActivity;
import com.ukgeek.sheetcounter.app.activities.HistoryActivity;

/**
 * Created by andrii on 04.10.15.
 */
public class Navigator {

    private static Navigator mInstance = null;

    public static Navigator getInstance() {
        if (mInstance == null) {
            mInstance = new Navigator();
        }
        return mInstance;
    }

    public void showHistoryActivity(Context context) {
        context.startActivity(new Intent(context, HistoryActivity.class));
    }

    public void showDetailsActivity(Context context, int count, String phrase,
                                    String textList) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("count", count);
        intent.putExtra("phrase", phrase);
//        intent.putExtra("text", new ArrayList<>(textList));
        intent.putExtra("text", textList);
        context.startActivity(intent);
    }
}
