package com.myclient.twit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by distill on 4/5/14.
 */
public class TimelineDBHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "timeline.db";
    static final String TABLE_NAME = "timeline";
    static final int VERSION = 1;
    static final String TAG = "DbHelper";
    static final String C_ID = BaseColumns._ID;
    static final String C_USER = "user";
    static final String C_MESSAGE = "message";
    static final String C_PICTURE = "picture";
    Context context;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    C_ID + " INTEGER PRIMARY KEY," +
                    C_USER + TEXT_TYPE + COMMA_SEP +
                    C_MESSAGE + TEXT_TYPE + COMMA_SEP +
                    C_PICTURE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.d(TAG, "onCreate sql:" + SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL(SQL_DELETE_ENTRIES);
        Log.d(TAG, "onUpdated");
        onCreate(db);
    }

    public TimelineDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }
}
