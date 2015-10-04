package com.ukgeek.sheetcounter.app.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ukgeek.sheetcounter.app.R;
import com.ukgeek.sheetcounter.app.utils.Navigator;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Navigator.getInstance().startMainActivity(SplashActivity.this);
    }
}
