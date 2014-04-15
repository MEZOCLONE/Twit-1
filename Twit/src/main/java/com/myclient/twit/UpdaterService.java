package com.myclient.twit;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

/**
 * Created by distill on 4/5/14.
 */
public class UpdaterService extends Service {

    static final int DELAY = 60000;
    static final String TAG = "UpdaterService";
    TimelineDBHelper dbHelper;
    SQLiteDatabase db;
    private boolean runFlag = false;
    private Updater updater;
    private TwitApplication twitApplication;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreated");
        twitApplication = (TwitApplication) getApplication();
        updater = new Updater();
//        dbHelper = new TimelineDBHelper(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStarted");
        runFlag = true;
        updater.start();
        twitApplication.setServiceRunning(true);
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroyed");

        runFlag = false;
        updater.interrupt();
        updater = null;
        twitApplication.setServiceRunning(false);
    }

    private class Updater extends Thread {
        List<Twitter.Status> timeline;

        public Updater() {
            super("UpdaterService-Updater");
        }

        @Override
        public void run() {
            UpdaterService updaterService = UpdaterService.this;
            while (updaterService.runFlag) {
                try {
                    try {
                        timeline = twitApplication.getTwitter().getFriendsTimeline();
                    } catch (TwitterException e) {
                        Log.e(TAG, "Failed to connect to twitter", e);
                    }

                    for (Twitter.Status status : timeline) {
                        Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
                    }

//                    db = dbHelper.getWritableDatabase();
//                    ContentValues values = new ContentValues();
//                    for (Twitter.Status status : timeline) {
//                        values.put(TimelineDBHelper.C_ID, status.id);
//                        values.put(TimelineDBHelper.C_USER, status.user.name);
//                        values.put(TimelineDBHelper.C_MESSAGE, status.text);
//                        db.insertWithOnConflict(TimelineDBHelper.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
//                    }
//
//                    db.close();

                    Log.d(TAG, "Updater ran");
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    updaterService.runFlag = false;
                }
            }
        }
    } // Updater Class


}
