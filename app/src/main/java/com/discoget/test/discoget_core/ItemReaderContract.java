package com.discoget.test.discoget_core;

import android.provider.BaseColumns;

/**
 * Created by Steven on 8/31/2016.
 */
public final class ItemReaderContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ItemReaderContract() {}

    /* Inner class that defines the table contents */
    public static class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_NAME_OWNER = "owner";
        public static final String COLUMN_NAME_ITEMURL = "itemurl";
        public static final String COLUMN_NAME_IMAGEURL = "imageurl";
        public static final String COLUMN_NAME_BARCODE = "barcode";
        public static final String COLUMN_NAME_SHORT_DESCRIPTION = "shortdescription";
        public static final String COLUMN_NAME_WHICHLIST = "whichlist";
        public static final String COLUMN_NAME_ARTIST = "artist";
        public static final String COLUMN_NAME_ALBUM = "album";
        public static final String COLUMN_NAME_ALBUMYEAR = "albumyear";
        public static final String COLUMN_NAME_CATALOGID = "catalogid";
        public static final String COLUMN_NAME_DELETEFLAG = "deleteflag";

        /*
        (owner, itemurl, imageurl, barcode, shortdescription, whichlist, artist, album, albumyear, catalogid )"

         */




        /*
         "(id integer primary key,
            owner VARCHAR,
            itemurl VARCHAR, " +"
            imageurl VARCHAR,
            barcode VARCHAR,
            shortdescription VARCHAR, " + "
            whichlist VARCHAR,
            artist VARCHAR,
            album VARCHAR,
            albumyear VARCHAR, " + "
            catalognumber VARCHAR,
            deleteflag VARCHAR);");
         */


    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String VARCHAR_TYPE = " VARCHAR";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
                    ItemEntry._ID + " INTEGER PRIMARY KEY," +
                    ItemEntry.COLUMN_NAME_OWNER + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_ITEMURL + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_IMAGEURL + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_BARCODE + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_SHORT_DESCRIPTION + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_WHICHLIST + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_ARTIST + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_ALBUM + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_ALBUMYEAR + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_CATALOGID + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_DELETEFLAG + VARCHAR_TYPE +  " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME;
}

