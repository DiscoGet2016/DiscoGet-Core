package com.discoget.test.discoget_core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;


/**
 * Created by Steven on 8/29/2016.
 */
public class MySQLiteHelper2 extends SQLiteOpenHelper {
    /*
    see link: https://developer.android.com/training/basics/data-storage/databases.html#ReadDbRow
     */

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "DiscoGetDB.db";

    public MySQLiteHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(ItemReaderContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(ItemReaderContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean dbExists() {

        File extStore = Environment.getExternalStorageDirectory();
        File myFile = new File(extStore.getAbsolutePath() + DATABASE_NAME);

        if(myFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

/*
    public static final String TABLE_NAME = "agrement";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AGREE = "agree";
    public static final String COLUMN_COMMENT = "comment";


        // Database creation sql statement
        private static final String DATABASE_CREATE = "create table "
                + TABLE_NAME + "( " + COLUMN_ID
                + " integer primary key autoincrement, " + COLUMN_AGREE
                + " varchar, " + COLUMN_COMMENT
                + " text);";

*/
}

