package com.ukgeek.sheetcounter.app.database;

/**
 * Created by voronsky on 03.10.15.
 */
public class SpeechItem {

    private String id;
    private String phrase;
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
