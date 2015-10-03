package com.ukgeek.sheetcounter.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        RecognitionListener {

    private FloatingActionButton mFabCatchPhrase;
    private ProgressBar progressBar;

    private SpeechRecognizer speech;

    private Intent recognizerIntent;

    private boolean isSpeech;

    private String phrase;
    private String text;

    private MaterialDialog mListenDialog;
    private MaterialDialog mInfoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupViews();

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                Locale.getDefault().getLanguage());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
    }

    private void initViews() {
        mFabCatchPhrase = (FloatingActionButton) findViewById(R.id.fab_catch_sheet_phrase);
    }

    private void setupViews() {
        mFabCatchPhrase.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_catch_sheet_phrase) {
//            showProgress1();
            showProgressDeterminateDialog(R.string.dialog_wait);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null) {
            speech.destroy();
            Logger.d("destroy");
        }

    }

    public void showProgressDeterminateDialog(final int title) {
        mListenDialog = new MaterialDialog.Builder(this)
                .title(title)
                .contentGravity(GravityEnum.CENTER)
                .progress(false, 10, false)
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

        progressBar = mListenDialog.getProgressBar();
        Logger.d("show listen");
        mListenDialog.show();
    }

    private void startSpeech() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        speech.startListening(recognizerIntent);
    }

    private void stopSpeech() {
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.INVISIBLE);
        speech.stopListening();
    }

    private void showBasic(String phrase) {
        Logger.d("show basic");
        mInfoDialog = new MaterialDialog.Builder(this)
                .title(R.string.dialog_caught_title)
                .content(phrase)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        isSpeech = true;
                        showProgressDeterminateDialog(R.string.dialog_wait);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        showProgressDeterminateDialog(R.string.dialog_wait);
                    }
                })
                .show();
    }

    @Override
    public void onBeginningOfSpeech() {
        Logger.d("onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Logger.d("onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Logger.d("onEndOfSpeech");
        progressBar.setIndeterminate(true);
        if (isSpeech)
            mListenDialog.dismiss();
    }


    String mErrorMessage;

    @Override
    public void onError(int errorCode) {
        mErrorMessage = getErrorText(errorCode);
        Logger.d("FAILED " + mErrorMessage);
        mListenDialog.getProgressBar().setIndeterminate(false);

        if (mErrorMessage != null && mErrorMessage.length() > 0)
            mListenDialog.setContent(mErrorMessage);
//        returnedText.setText(mErrorMessage);
//        toggleButton.setChecked(false);
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
        if (isSpeech)
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
        if (!isSpeech) {
            mListenDialog.dismiss();
            showBasic(text);
            phrase = text;
        } else {
            this.text = text;
            calc2();
        }
    }

    private void calc() {
        String str = text;
        String findStr = phrase;
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {

            lastIndex = str.indexOf(findStr, lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += findStr.length();
            }
        }
        Logger.d("count=" + count);
    }

    private void calc2() {
        String str = text;
        String findStr = phrase;
        int count = 0;

        StringTokenizer st = new StringTokenizer(str, " ");
        ArrayList<String> list = new ArrayList<>();

        while (st.hasMoreElements()) {
            list.add(st.nextElement().toString());
        }

        Logger.d(list.toString());

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equalsIgnoreCase(findStr))
                count++;
        }

        Logger.d("count=" + count);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
//        Logger.d("onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);

    }

    private void restartSpeech() {
        speech.cancel();
        speech.destroy();
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
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
}
