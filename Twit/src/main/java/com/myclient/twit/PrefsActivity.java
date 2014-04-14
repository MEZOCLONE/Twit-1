package com.myclient.twit;

import android.os.Bundle;
import android.preference.PreferenceActivity;


/**
 * Created by sergioalmecijarodriguez on 4/13/14.
 */
public class PrefsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}
