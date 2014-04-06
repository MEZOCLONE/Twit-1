package com.myclient.twit;

import android.app.Application;

import winterwell.jtwitter.Twitter;

/**
 * Created by distill on 4/5/14.
 */
public class TwitApplication extends Application {
    public Twitter twitter;
    private boolean serviceRunning;

    public void onCreate() {
        super.onCreate();
    }

    public void onTerminate() {
        super.onTerminate();
    }

    public synchronized Twitter getTwitter() {
        if (twitter == null) { //
            String username, password, apiRoot;
//            username = prefs.getString("username", ""); //
//            password = prefs.getString("password", "");
//            apiRoot = prefs.getString("apiRoot", "http://yamba.marakana.com/api");
            username = "student";
            password = "password";
            apiRoot = "http://yamba.marakana.com/api";
// Connect to twitter.com
            twitter = new Twitter(username, password); //
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
