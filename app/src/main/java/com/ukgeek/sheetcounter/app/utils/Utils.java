package com.ukgeek.sheetcounter.app.utils;

import com.ukgeek.sheetcounter.app.database.SpeechItem;

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
        int count = 0;
        List<String> list = makeList(text);
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).equalsIgnoreCase(phrase))
                count++;
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
}
