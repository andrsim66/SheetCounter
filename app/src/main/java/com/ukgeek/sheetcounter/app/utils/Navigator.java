package com.ukgeek.sheetcounter.app.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.ukgeek.sheetcounter.app.activities.DetailsActivity;
import com.ukgeek.sheetcounter.app.activities.HistoryActivity;
import com.ukgeek.sheetcounter.app.activities.MainActivity;

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

    public void startMainActivity(final Context context) {
        new Handler().postDelayed(new Runnable() {
            // Showing splash screen with a timer. This will be useful when you
            // want to show case your app logo / company
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }, 1500);
    }
}
