package com.ukgeek.sheetcounter.app.utils;

import android.content.Context;
import android.widget.TextView;

import com.ukgeek.sheetcounter.app.R;
import com.ukgeek.sheetcounter.app.database.SpeechItem;
import com.ukgeek.sheetcounter.app.managers.ManagerTypeface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by andrii on 04.10.15.
 */
public class Utils {


    public static int getCount(String phrase, String text) {
        if (phrase.contains(" "))
            return getCountPhrase(phrase, text);
        else
            return getCountWord(phrase, text);
    }

    public static int getCountWord(String phrase, String text) {
        int count = 0;
        List<String> list = makeList(text);
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).equalsIgnoreCase(phrase))
                count++;
        return count;
    }

    public static int getCountPhrase(String phrase, String text) {
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {
            lastIndex = text.indexOf(phrase, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += phrase.length();
            }
        }
        return count;
    }

    public static ArrayList<String> makeList(String text) {
        StringTokenizer st = new StringTokenizer(text, " ");
        ArrayList<String> list = new ArrayList<>();

        while (st.hasMoreElements()) {
            list.add(st.nextElement().toString());
        }
        return list;
    }

    public static ArrayList<SpeechItem> parseJsonResponse(String json) {
        try {
            JSONArray array = new JSONArray(json);
            ArrayList<SpeechItem> speechItems = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                SpeechItem speechItem = new SpeechItem();
                JSONObject jsonObject = array.getJSONObject(i);

                speechItem.setId(jsonObject.get("id").toString());
                try {
                    String s = jsonObject.get("message").toString();
                    s = s.substring(1, s.lastIndexOf("\""));
                    JSONObject message = new JSONObject(s);
                    speechItem.setPhrase(message.get("word").toString());
                    speechItem.setText(message.get("text").toString());
                } catch (JSONException e) {
                    Logger.e(e.getMessage(), e);
                }
                speechItems.add(speechItem);
            }
            return speechItems;
        } catch (JSONException e) {
            Logger.e(e.getMessage(), e);
        }
        return null;
    }

    public static String makeHighLight(String phrase, List<String> list) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            if (i > 0)
                sb.append(" ");
            if (list.get(i).equalsIgnoreCase(phrase)) {
                sb.append("<u><b><font color=\"#DC6262\">");
                sb.append(list.get(i));
                sb.append("</font></b></u>");
            } else {
                sb.append(list.get(i));
            }
        }
        return sb.toString();
    }

    public static String makePhraseHighLight(String phrase, String text) {
        String styled = "<u><b><font color=\"#DC6262\">" + phrase + "</font></b></u>";
        text = text.replace(phrase, styled);
        return text;
    }

    public static void setTypefaceRobotoRegular(Context context, TextView... textViews) {
        if (textViews != null && textViews.length > 0) {
            for (int i = 0; i < textViews.length; i++) {
                textViews[i].setTypeface(
                        ManagerTypeface.getTypeface(context, R.string.typeface_roboto_regular));
            }
        }
    }

    public static void setTypefaceRobotoLight(Context context, TextView... textViews) {
        if (textViews != null && textViews.length > 0) {
            for (int i = 0; i < textViews.length; i++) {
                textViews[i].setTypeface(
                        ManagerTypeface.getTypeface(context, R.string.typeface_roboto_light));
            }
        }
    }
}
