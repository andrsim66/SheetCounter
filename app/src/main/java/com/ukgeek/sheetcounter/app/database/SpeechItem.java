package com.ukgeek.sheetcounter.app.database;

/**
 * Created by voronsky on 03.10.15.
 */
public class SpeechItem {
    private String id;
    private String badWord;
    private String speech;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBadWord() {
        return badWord;
    }

    public void setBadWord(String badWord) {
        this.badWord = badWord;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }
}
