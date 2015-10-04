package com.ukgeek.sheetcounter.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ukgeek.sheetcounter.app.R;
import com.ukgeek.sheetcounter.app.utils.Navigator;
import com.ukgeek.sheetcounter.app.utils.Utils;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private TextView mTvCount;
    private TextView mTvPhrase;
    private TextView mTvLabel;
    private TextView mTvText;

    private ArrayList<String> mText;
    private String mPhrase;
    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details2);

        getExtras();
        initViews();
        setupViews();
    }

    private void getExtras() {
        Intent intent = getIntent();
        mCount = intent.getIntExtra("count", 0);
        mPhrase = intent.getStringExtra("phrase");
        mText = intent.getStringArrayListExtra("text");
    }

    private void initViews() {
        mTvCount = (TextView) findViewById(R.id.tv_matches_count);
        mTvPhrase = (TextView) findViewById(R.id.tv_phrase);
        mTvLabel = (TextView) findViewById(R.id.tv_label);
        mTvText = (TextView) findViewById(R.id.tv_result_text);
    }

    private void setupViews() {
        mTvCount.setText("" + mCount);
        if (mPhrase != null && mText != null && mText.size() > 0) {
            mTvPhrase.setText(mPhrase);
            mTvText.setText(Html.fromHtml(Utils.makeHighLight(mPhrase, mText)));
        }
        Utils.setTypefaceRobotoLight(DetailsActivity.this, mTvText, mTvPhrase, mTvCount, mTvLabel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_history) {
            Navigator.getInstance().showHistoryActivity(DetailsActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
