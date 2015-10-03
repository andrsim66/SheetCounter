package com.ukgeek.sheetcounter.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ukgeek.sheetcounter.app.R;
import com.ukgeek.sheetcounter.app.utils.Logger;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private TextView mTvCount;
    private TextView mTvPhrase;
    private TextView mTvText;

    private ArrayList<String> mText;
    private String mPhrase;
    private int mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

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
        mTvText = (TextView) findViewById(R.id.tv_result_text);
    }

    private void setupViews() {
        mTvCount.setText("" + mCount);
        Logger.d(mPhrase);
        Logger.d(mText.toString());
        if (mPhrase != null && mText != null && mText.size() > 0) {
            mTvPhrase.setText(mPhrase);

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < mText.size(); i++) {
                if (i > 0)
                    sb.append(" ");
                if (mText.get(i).equalsIgnoreCase(mPhrase)) {
                    sb.append("<u><b><font color=\"#DC6262\">");
                    sb.append(mText.get(i));
                    sb.append("</font></b></u>");
                } else {
                    sb.append(mText.get(i));
                }
            }

            Logger.d(sb.toString());

            mTvText.setText(Html.fromHtml(sb.toString()));
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
