package com.ukgeek.sheetcounter.app.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.ukgeek.sheetcounter.app.R;
import com.ukgeek.sheetcounter.app.managers.ManagerTypeface;
import com.ukgeek.sheetcounter.app.utils.Api;
import com.ukgeek.sheetcounter.app.utils.Logger;
import com.ukgeek.sheetcounter.app.utils.Navigator;
import com.ukgeek.sheetcounter.app.utils.Utils;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        RecognitionListener {

    private FloatingActionButton mFabCatchPhrase;
    private ProgressBar mProgressBar;
    private MaterialDialog mListenDialog;
    private TextView mTvTap;

    private SpeechRecognizer mSpeechRecognizer;
    private Intent recognizerIntent;

    private String mErrorMessage;
    private String mPhrase;
    private boolean mIsSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupViews();
    }

    private void initViews() {
        mFabCatchPhrase = (FloatingActionButton) findViewById(R.id.fab_catch_sheet_phrase);
        mTvTap = (TextView) findViewById(R.id.tv_tap);
    }

    private void setupViews() {
        mFabCatchPhrase.setOnClickListener(this);
        Utils.setTypefaceRobotoRegular(MainActivity.this, mTvTap);
    }

    private void setRecognizerIntent() {
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                Locale.getDefault().getLanguage());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_catch_sheet_phrase)
            showListeningDialog(R.string.dialog_wait);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPhrase = null;
        mIsSpeech = false;
        restartSpeech();
        setRecognizerIntent();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
            Logger.d("destroy");
        }

    }

    public void showListeningDialog(final int title) {
        mListenDialog = new MaterialDialog.Builder(this)
                .title(title)
                .contentGravity(GravityEnum.CENTER)
                .progress(false, 10, false)
                .typeface(ManagerTypeface.getTypeface(MainActivity.this, R.string.typeface_roboto_medium),
                        ManagerTypeface.getTypeface(MainActivity.this, R.string.typeface_roboto_regular))
                .widgetColorRes(R.color.material_indigo_500)
                .positiveColor(getResources().getColor(R.color.material_indigo_500))
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        startSpeech();
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        Logger.d("onDismiss " + title);
                        stopSpeech();
                    }
                })
                .positiveText("Stop")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                        stopSpeech();
                    }
                })
                .build();

        mProgressBar = mListenDialog.getProgressBar();
        Logger.d("show listen");
        mListenDialog.show();
    }

    private void startSpeech() {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(true);
        mSpeechRecognizer.startListening(recognizerIntent);
    }

    private void stopSpeech() {
        mProgressBar.setIndeterminate(false);
        mProgressBar.setVisibility(View.INVISIBLE);
        mSpeechRecognizer.stopListening();
    }

    private void showConfirmationDialog(String phrase) {
        Logger.d("show basic");
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_caught_title)
                .content(phrase)
                .typeface(ManagerTypeface.getTypeface(MainActivity.this, R.string.typeface_roboto_medium),
                        ManagerTypeface.getTypeface(MainActivity.this, R.string.typeface_roboto_regular))
                .widgetColorRes(R.color.material_indigo_500)
                .positiveColor(getResources().getColor(R.color.material_indigo_500))
                .negativeColor(getResources().getColor(R.color.material_indigo_500))
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        mIsSpeech = true;
                        showListeningDialog(R.string.dialog_wait);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        showListeningDialog(R.string.dialog_wait);
                    }
                })
                .show();
    }

    @Override
    public void onBeginningOfSpeech() {
        Logger.d("onBeginningOfSpeech");
        mProgressBar.setIndeterminate(false);
        mProgressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Logger.d("onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Logger.d("onEndOfSpeech");
        mProgressBar.setIndeterminate(true);
        if (mIsSpeech)
            mListenDialog.dismiss();
    }


    @Override
    public void onError(int errorCode) {
        mErrorMessage = getErrorText(errorCode);
        Logger.d("FAILED " + mErrorMessage);
        mListenDialog.getProgressBar().setIndeterminate(false);

        if (mErrorMessage != null && mErrorMessage.length() > 0)
            mListenDialog.setContent(mErrorMessage);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Logger.d("onEvent" + arg0);
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Logger.d("onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Logger.d("onReadyForSpeech");
        if (mIsSpeech)
            mListenDialog.setTitle(R.string.dialog_speak);
        else
            mListenDialog.setTitle(R.string.dialog_title);

    }

    @Override
    public void onResults(Bundle results) {
        Logger.d("onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = matches.get(0);
        Logger.d(text);
        if (!mIsSpeech) {
            mListenDialog.dismiss();
            showConfirmationDialog(text);
            mPhrase = text;
        } else {
            int count = Utils.getCount(mPhrase, text);
            Api.sendToServer(mPhrase, text);
            Navigator.getInstance().showDetailsActivity(MainActivity.this, count, mPhrase, Utils.makeList(text));
        }
    }

    @Override
    public void onRmsChanged(float rmsdB) {
//        Logger.d("onRmsChanged: " + rmsdB);
        mProgressBar.setProgress((int) rmsdB);
    }

    private void restartSpeech() {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.cancel();
            mSpeechRecognizer.destroy();
        }
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(this);
    }

    public String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                restartSpeech();
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
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
            Navigator.getInstance().showHistoryActivity(MainActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
