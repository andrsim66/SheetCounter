package com.ukgeek.sheetcounter.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.ukgeek.sheetcounter.app.adapters.HistoryListAdapter;
import com.ukgeek.sheetcounter.app.database.Speech;

import java.util.ArrayList;

/**
 * Created by voronsky on 03.10.15.
 */
public class HistoryActivity extends ActionBarActivity {

    private ListView mHistoryList;
    private SingletonDBAccess dbAccess;
    private ArrayList<Speech> badWords;
    private HistoryListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbAccess = SingletonDBAccess.getInstance();

        initView();
        setupView();
        loadBadWords();
    }

    private void initView() {
        mHistoryList = (ListView) findViewById(R.id.historyList);
        mHistoryList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void setupView() {
        mAdapter = new HistoryListAdapter(getApplicationContext(),
                R.layout.history_list_item, badWords);
        mHistoryList.setAdapter(mAdapter);
    }

    private void loadBadWords() {
        badWords = dbAccess.getDbh().selectAllRecords(dbAccess.getDbh().getWritableDatabase());
    }
}
