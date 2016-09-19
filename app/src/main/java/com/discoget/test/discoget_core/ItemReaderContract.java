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
        public static final String COLUMN_NAME_RESOURCEID = "resourceid";
        public static final String COLUMN_NAME_CATALOGID = "catalogid";
        public static final String COLUMN_NAME_ALBUMTITLE = "albumtitle";
        public static final String COLUMN_NAME_ALBUMARTIST = "albumartist";
        public static final String COLUMN_NAME_ALBUMLABEL = "albumlabel";
        public static final String COLUMN_NAME_ALBUMYEAR = "albumyear";
        public static final String COLUMN_NAME_COVERURL = "coverurl";
        public static final String COLUMN_NAME_BARCODE = "barcode";
        public static final String COLUMN_NAME_SHORT_DESCRIPTION = "shortdescription";
        public static final String COLUMN_NAME_LISTTYPE = "listtype";
        public static final String COLUMN_NAME_DELETEFLAG = "deleteflag";

        /*
        was -->(owner, itemurl, imageurl, barcode, shortdescription, whichlist, artist, album, albumyear, catalogid )"

        now -->(owner,resourceid,catalogid,albumtilte,albumartist,albumlabel,albumyear,coverurl,barcode,shortdescription,listtype,deleteflag)

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
                    ItemEntry.COLUMN_NAME_RESOURCEID + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_CATALOGID + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_ALBUMTITLE + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_ALBUMARTIST + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_ALBUMLABEL + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_ALBUMYEAR + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_COVERURL + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_BARCODE + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_SHORT_DESCRIPTION + VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_LISTTYPE+ VARCHAR_TYPE + COMMA_SEP +
                    ItemEntry.COLUMN_NAME_DELETEFLAG + VARCHAR_TYPE +  " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME;
}

