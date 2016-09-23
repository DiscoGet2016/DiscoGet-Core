package com.discoget.test.discoget_core;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Steven on 8/30/2016.
 */
public class CollectionListAdapter extends ArrayAdapter<CollectionItems> {
    public CollectionListAdapter(Context context, ArrayList<CollectionItems> collectionList) {
        super(context, 0, collectionList);
    }


    MySQLiteHelper dbHelper;
    SQLiteDatabase discogetdb;

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap", "returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CollectionItems collectionItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout_collection, parent, false);
        }
        // Lookup view for data population
        TextView txtRowOne = (TextView) convertView.findViewById(R.id.txt_artist);
        TextView txtRowTwo = (TextView) convertView.findViewById(R.id.txt_album_title);
        TextView txtRowThree = (TextView) convertView.findViewById(R.id.txt_year);
        TextView txtMatches = (TextView) convertView.findViewById(R.id.tv_row_item_matches);


        ListView lvMatchesFound = (ListView) convertView.findViewById(R.id.lv_row_item_matches);

        //TextView  = (TextView) convertView.findViewById(R.id.tvHome);
        ImageView imgAlbumCover = (ImageView) convertView.findViewById(R.id.img_albumCover);

        String listOfMatches = "";

        if (collectionItem.searchType.equals("title")) {

            txtRowOne.setText(collectionItem.itemTitle);
            txtRowTwo.setText(collectionItem.itemLabel);
            txtRowThree.setText(collectionItem.itemYear + "  [" + collectionItem.itemResourceID + "]");
            //tvHome.setText(collectionItem.itemCoverURL);


        } else {
            // Populate the data into the template view using the data object
            txtRowOne.setText(collectionItem.itemArtist);
            txtRowTwo.setText(collectionItem.itemTitle);
            txtRowThree.setText(collectionItem.itemYear + "  [" + collectionItem.itemResourceID + "]");
            //tvHome.setText(collectionItem.itemCoverURL);

        }

      /*  listOfMatches = checkForItemMatches(collectionItem.itemResourceID);

        if (listOfMatches.length() > 1) {
            txtMatches.setText(listOfMatches);
            txtMatches.setVisibility(View.VISIBLE);
          }
        listOfMatches = ""; // clears string
       */




/*
        //lvMatchesFound.--------------------------------------------
        // -- Get and Display Tracks info...
        ArrayList<ItemMatches> listOfItemMatches = new ArrayList<ItemMatches>();
        ItemMatchesAdapter matchesAdapter = new ItemMatchesAdapter(getContext(), listOfItemMatches);
        // add items to track

        getItemMatches(matchesAdapter, collectionItem.itemResourceID);

        lvMatchesFound.setAdapter(matchesAdapter);

        //----------------------------------------
*/
        String tempURL = "http://www.aptitude.co.uk/img/album-covers/The-Beatles-Abbey-road.png";

        String tempURL2 = collectionItem.itemCoverURL;  //"https://s.yimg.com/fz/api/res/1.2/7GzBaZrJQJMNhFfKJmMY8A--/YXBwaWQ9c3JjaGRkO2g9NTAwO3E9OTU7dz01MDA-/http://www.amiright.com/album-covers/images/album-Kiss-Destroyer.jpg";

        //imgAlbumCover.setImageBitmap(getBitmapFromURL(tempURL));

        new ImageLoadTask(tempURL2, imgAlbumCover).execute();

        // imgAlbumCover.setImageURI(new ImageLoadTask(url, imageView).execute();;
        //imageView.setImageBitmap(getBitmapFromURL(url));

        // Return the completed view to render on screen
        return convertView;
    }

    private void getItemMatches(ItemMatchesAdapter matchesAdapter, String itemReleaseID) {

        ItemMatches newMatch;

        String ownerName = "";
        String ownerList = "";

        // check DB for item - use itemReleaseID
        //MySQLiteHelper dbHelper;
        dbHelper = new MySQLiteHelper(getContext());
        discogetdb = dbHelper.getReadableDatabase();

       /*
         // Items -- store all items for user and friends
            discogetDB.execSQL("CREATE TABLE IF NOT EXISTS items " +
                    "(id integer primary key, owner VARCHAR,resourceid VARCHAR, catalogid VARCHAR, albumtitle VARCHAR," +
                    " albumartist VARCHAR, albumlabel VARCHAR, albumyear VARCHAR, coverurl VARCHAR, barcode VARCHAR," +
                    " shortdescription VARCHAR, listtype VARCHAR, deleteflag VARCHAR);");
        */


        Cursor cursor = discogetdb.rawQuery("Select owner, listtype FROM items WHERE resourceid = '" + itemReleaseID + "'", null);

        cursor.moveToFirst();

        // check for data
        if (cursor != null && cursor.getCount() > 0) {

            do {

                ownerName = cursor.getString(0);
                ownerList = cursor.getString(1);

                // add found item to adapter...
                newMatch = new ItemMatches(ownerName, itemReleaseID, ownerList);
                matchesAdapter.add(newMatch);
            } while (cursor.moveToNext());

            cursor.close();

            dbHelper.close();

        }
    }


    private String checkForItemMatches(String itemReleaseID) {

        ItemMatches newMatch;

        String strToReturn = "";


        String ownerName = "";
        String ownerList = "";
        String releaseID0 = "";


        // check DB for item - use itemReleaseID
        //MySQLiteHelper dbHelper;
        dbHelper = new MySQLiteHelper(getContext());
        discogetdb = dbHelper.getReadableDatabase();

       /*
         // Items -- store all items for user and friends
            discogetDB.execSQL("CREATE TABLE IF NOT EXISTS items " +
                    "(id integer primary key, owner VARCHAR,resourceid VARCHAR, catalogid VARCHAR, albumtitle VARCHAR," +
                    " albumartist VARCHAR, albumlabel VARCHAR, albumyear VARCHAR, coverurl VARCHAR, barcode VARCHAR," +
                    " shortdescription VARCHAR, listtype VARCHAR, deleteflag VARCHAR);");
        */


        Cursor cursor = discogetdb.rawQuery("Select owner, listtype, resourceid FROM items WHERE resourceid = '" + itemReleaseID + "'", null);

        cursor.moveToFirst();

        // check for data
        if (cursor != null && cursor.getCount() > 0) {


            do {
                if (strToReturn.length() > 1) {
                    strToReturn = strToReturn + "\n";
                }

                ownerName = cursor.getString(0);
                ownerList = cursor.getString(1);
                releaseID0 = cursor.getString(2);


                // build string
                strToReturn += "This item is in " + itemReleaseID + "'s " + releaseID0;

                // add found item to adapter...
               // newMatch = new ItemMatches(ownerName, itemReleaseID, ownerList);
               // matchesAdapter.add(newMatch);
            } while (cursor.moveToNext());

            cursor.close();

            dbHelper.close();

        }

       return strToReturn;
    }
}
