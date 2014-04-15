package com.myclient.twit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    TextView charactersLeft;
    private String stringCharacters;
    private Menu optionsMenu;

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

        stringCharacters = getString(R.string.characters);
        charactersLeft.setText("140 " + stringCharacters);
        charactersLeft.setTextColor(Color.GREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.twit, menu);

        updateOptionsMenu(this.optionsMenu = menu);

        return true;
    }

    @Override
    public void onClick(View v) {
        String status = editText.getText().toString();
        new PostToTwitter().execute(status);
        Log.d(TAG, "onClicked");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, PrefsActivity.class));
                break;
            case R.id.action_start:
                startService(new Intent(this, UpdaterService.class));
                break;
            case R.id.action_stop:
                stopService(new Intent(this, UpdaterService.class));
                break;
        }

//        updateOptionsMenu(this.optionsMenu);

        return true;
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

    private void updateOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_start).setEnabled(!((TwitApplication) getApplication()).isServiceRunning());
        menu.findItem(R.id.action_stop).setEnabled(((TwitApplication) getApplication()).isServiceRunning());
    }

    // Async post to twitter
    class PostToTwitter extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Twitter.Status status = ((TwitApplication) getApplication()).getTwitter().updateStatus(params[0]);
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
