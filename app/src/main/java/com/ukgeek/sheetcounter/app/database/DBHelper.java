package com.ukgeek.sheetcounter.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by voronsky on 03.10.15.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "badWordsDB";
    public static final String BAD_WORD = "badWord";
    public static final String SPEECH = "speech";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
