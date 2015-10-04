package com.ukgeek.sheetcounter.app.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ukgeek.sheetcounter.app.R;
import com.ukgeek.sheetcounter.app.adapters.HistoryListAdapter;
import com.ukgeek.sheetcounter.app.database.SpeechItem;
import com.ukgeek.sheetcounter.app.utils.Api;
import com.ukgeek.sheetcounter.app.utils.Navigator;
import com.ukgeek.sheetcounter.app.utils.Utils;

import java.util.ArrayList;

/**
 * Created by voronsky on 03.10.15.
 */
public class HistoryActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSrlHistory;
    private ListView mHistoryList;
    private ArrayList<SpeechItem> badWords;
    private HistoryListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initView();
        setupViews();
        getList();
    }

    private void initView() {
        mSrlHistory = (SwipeRefreshLayout) findViewById(R.id.srl_history);
        mHistoryList = (ListView) findViewById(R.id.historyList);
    }

    private void setupViews() {
        mHistoryList.setOnItemClickListener(this);
        mSrlHistory.setColorSchemeResources(R.color.material_indigo_500, R.color.logo);
        mSrlHistory.setOnRefreshListener(this);
    }

    private void getList() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return Api.receive();
            }

            @Override
            protected void onPostExecute(String result) {
                badWords = Utils.parseJsonResponse(result);
                mAdapter = new HistoryListAdapter(HistoryActivity.this,
                        R.layout.item_history, badWords);
                mHistoryList.setAdapter(mAdapter);
                mSrlHistory.setRefreshing(false);
            }
        }.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String phrase = mAdapter.getItem(i).getPhrase();
        String text = mAdapter.getItem(i).getText();
        int count = Utils.getCount(phrase, text);
        Navigator.getInstance().showDetailsActivity(HistoryActivity.this, count, phrase, text);
    }

    @Override
    public void onRefresh() {
        getList();
    }
}
