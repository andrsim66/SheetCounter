package com.ukgeek.sheetcounter.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by voronsky on 03.10.15.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "badWordsDB";
    public static final String COLUMN_ID = "_id";
    public static final String BAD_WORD = "badWord";
    public static final String SPEECH = "speech";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + "INTEGER PRIMARY KEY  AUTOINCREMENT"
                + BAD_WORD + " TEXT, "
                + SPEECH + " TEXT, " + " )"
                + "UNIQUE (_id) ON CONFLICT REPLACE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertSpeech(SQLiteDatabase db, SpeechItem speech) {
        ContentValues cv = new ContentValues();
        cv.put(BAD_WORD, speech.getBadWord());
        cv.put(SPEECH, speech.getSpeech());
        db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public ArrayList<SpeechItem> selectAllRecords(SQLiteDatabase db) {
        ArrayList<SpeechItem> records = new ArrayList<>();

        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        try {
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        SpeechItem speech = new SpeechItem();
                        speech.setBadWord(c.getString(c.getColumnIndexOrThrow(BAD_WORD)));
                        speech.setSpeech(c.getString(c.getColumnIndexOrThrow(SPEECH)));
                        records.add(speech);
                    } while (c.moveToNext());
                }
            }
            c.close();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return records;
    }
}
