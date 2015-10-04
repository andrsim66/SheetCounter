package com.ukgeek.sheetcounter.app.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.ukgeek.sheetcounter.app.R;
import com.ukgeek.sheetcounter.app.adapters.HistoryListAdapter;
import com.ukgeek.sheetcounter.app.database.SpeechItem;
import com.ukgeek.sheetcounter.app.utils.Api;
import com.ukgeek.sheetcounter.app.utils.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by voronsky on 03.10.15.
 */
public class HistoryActivity extends ActionBarActivity {

    private ListView mHistoryList;
    private ArrayList<SpeechItem> badWords;
    private HistoryListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);

        initView();
        getList();
    }

    private void initView() {
        mHistoryList = (ListView) findViewById(R.id.historyList);
    }

    private void getList() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return Api.receive();
            }

            @Override
            protected void onPostExecute(String s) {
                badWords = parseJsonResponse(s);
                mAdapter = new HistoryListAdapter(HistoryActivity.this,
                        R.layout.item_history, badWords);
                mHistoryList.setAdapter(mAdapter);
            }
        }.execute();
    }

    private ArrayList<SpeechItem> parseJsonResponse(String json) {
        try {
            JSONArray array = new JSONArray(json);
            ArrayList<SpeechItem> speechItems = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                SpeechItem speechItem = new SpeechItem();
                JSONObject jsonObject = array.getJSONObject(i);

                speechItem.setId(jsonObject.get("id").toString());
                try {
                    String s = jsonObject.get("message").toString();
                    JSONObject message = new JSONObject(s);
                    speechItem.setBadWord(message.get("word").toString());
                    speechItem.setSpeech(message.get("text").toString());
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
}
