package com.discoget.test.discoget_core;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Steven on 8/31/2016.
 */
public class BuildItemList extends Activity{

    private String owner;
    private String resourceid;
    private String coverurl;
    private String listtype;
    private String title;
    private String artist;
    private String album;
    private String year;

    SQLiteDatabase discogetDB;
    MySQLiteHelper dbHelper;

    //private Context context;

    public BuildItemList() {
       // this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       dbHelper = new MySQLiteHelper(this);

    }

    public String AddItemToDb(String owner, String resourceid, String coverurl, String listtype, String title, String artist, String album, String year) {

        String theResult;


        //MySQLiteHelper dbHelper = new MySQLiteHelper(getApplicationContext());

        // Gets the data repository in write mode

        discogetDB = dbHelper.getWritableDatabase();

        // Records to DB - items table

        /*
          // Items -- store all items for user and friends
            discogetDB.execSQL("CREATE TABLE IF NOT EXISTS items " +
                    "(id integer primary key autoincrement , owner VARCHAR, discogsitemurl VARCHAR," +
                    "imageurl VARCHAR, barcode VARCHAR, shortdescription VARCHAR," +
                    "whichlist VARCHAR, artist VARCHAR, album VARCHAR, year VARCHAR," +
                    "catalognumber VARCHAR, deleteflag VARCHAR);");
         */
        String tableName = "items";
        //String tempURL = "http://software55.net/images/integrationLogo2.gif";

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_OWNER, owner);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_RESOURCEID, resourceid);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_COVERURL,coverurl);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_LISTTYPE,listtype);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_ALBUMTITLE,title);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_ALBUMARTIST, artist);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_ALBUMLABEL, album);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_ALBUMYEAR, year);

        //values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = discogetDB.insert(ItemReaderContract.ItemEntry.TABLE_NAME, null, values);

        theResult = "New Record Added " + newRowId;

        //Toast.makeText(this, "New Record Added " + newRowId, Toast.LENGTH_SHORT).show();
        return theResult;

    }
}
