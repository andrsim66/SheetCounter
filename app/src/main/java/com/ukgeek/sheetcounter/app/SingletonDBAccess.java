package com.ukgeek.sheetcounter.app;

import com.ukgeek.sheetcounter.app.database.DBHelper;
import com.ukgeek.sheetcounter.app.database.Speech;

/**
 * Created by voronsky on 03.10.15.
 */
public class SingletonDBAccess {

    private static SingletonDBAccess mInstance = null;
    private DBHelper dbh;
    private Speech speech;

    public static SingletonDBAccess getInstance() {
        if (mInstance == null) {
            mInstance = new SingletonDBAccess();
        }
        return mInstance;
    }

    public DBHelper getDbh() {
        return dbh;
    }

    public void setDbh(DBHelper dbh) {
        this.dbh = dbh;
    }

    public Speech getSpeech() {
        return speech;
    }

    public void setSpeech(Speech speech) {
        this.speech = speech;
    }
}
