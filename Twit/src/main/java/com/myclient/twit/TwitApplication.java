package com.myclient.twit;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import winterwell.jtwitter.Twitter;

/**
 * Created by distill on 4/5/14.
 */
public class TwitApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {
    public Twitter twitter;
    SharedPreferences prefs;
    private boolean serviceRunning;

    public void onCreate() {
        super.onCreate();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        twitter = null;
    }

    public synchronized Twitter getTwitter() {
        if (twitter == null) { //
            String username, password, apiRoot;
            username = prefs.getString("username", "");
            password = prefs.getString("password", "");
            apiRoot = prefs.getString("apiRoot", "http://yamba.marakana.com/api");
            // Connect to twitter.com
            twitter = new Twitter(username, password);
            twitter.setAPIRootUrl(apiRoot); //
        }
        return twitter;
    }

    public boolean isServiceRunning() {
        return serviceRunning;
    }

    public void setServiceRunning(boolean serviceRunning) {
        this.serviceRunning = serviceRunning;
    }

}
