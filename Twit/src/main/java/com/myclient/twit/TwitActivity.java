package com.myclient.twit;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import winterwell.jtwitter.Twitter;

public class TwitActivity extends Activity {

    public TimelineDBHelper dbHelper;
    public SQLiteDatabase db;
    Twitter twitter;
    Cursor c;
    ListView list;
    SimpleCursorAdapter adapter;
    static final String[] FROM = {TimelineDBHelper.C_USER, TimelineDBHelper.C_MESSAGE};
    static final int[] TO = {R.id.textView, R.id.textView2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twit);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        list = (ListView) findViewById(R.id.listView);
        dbHelper = new TimelineDBHelper(this);
        startService(new Intent(this, UpdaterService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.twit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_twit, container, false);
        }

    }

    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, UpdaterService.class));
        db.close();
    }

    public void onResume() {
        super.onResume();
        //get data from database

        db = dbHelper.getReadableDatabase();

        c = db.query(TimelineDBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();

            adapter = new SimpleCursorAdapter(this, R.layout.row, c, FROM, TO, 0);
            list.setAdapter(adapter);
        }
    }

}
