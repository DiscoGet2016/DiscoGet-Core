package com.discoget.test.discoget_core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Steven on 8/29/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DiscoGetDB.db";
    private static final int DATABASE_VERSION = 1;

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

        public MySQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(MySQLiteHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
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

}

