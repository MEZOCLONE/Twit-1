package com.myclient.twit;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

/**
 * Created by sergioalmecijarodriguez on 4/12/14.
 */
public class StatusActivity extends Activity implements OnClickListener, TextWatcher {
    private static final String TAG = "StatusActivity";
    EditText editText;
    Button publishButton;
    Twitter twitter;
    TextView charactersLeft;
    private String stringCharacters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);

        // Find Views
        publishButton = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        charactersLeft = (TextView)findViewById(R.id.textView2);

        editText.addTextChangedListener(this);
        publishButton.setOnClickListener(this);

        twitter = new Twitter("student", "password");
        twitter.setAPIRootUrl("http://yamba.marakana.com/api");

        stringCharacters = getString(R.string.characters);
        charactersLeft.setText("140 " + stringCharacters);
        charactersLeft.setTextColor(Color.GREEN);
    }

    @Override
    public void onClick(View v) {
        String status = editText.getText().toString();
        new PostToTwitter().execute(status);
        Log.d(TAG, "onClicked");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int left = 140 - s.length();
        charactersLeft.setText(String.valueOf(left) + " " + stringCharacters);
        if(left < 10){
            charactersLeft.setTextColor(Color.RED);
        }else if(left < 50){
            charactersLeft.setTextColor(Color.YELLOW);
        }else {
            charactersLeft.setTextColor(Color.GREEN);
        }
    }

    // Async post to twitter
    class PostToTwitter extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Twitter.Status status = twitter.updateStatus(params[0]);
                return status.text;
            } catch (TwitterException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
                return "Failed to post";
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(StatusActivity.this, s, Toast.LENGTH_LONG).show();
        }
    }
}
