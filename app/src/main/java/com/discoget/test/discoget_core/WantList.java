package com.discoget.test.discoget_core;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


//import static com.discoget.test.outtestproject.R.id.the_list_view;

/**
 * Created by steve on 8/17/2016.
 */
public class WantList extends AppCompatActivity {

    private boolean debug = false; // use true for testing

    private SQLiteDatabase discogetDB;
    private MySQLiteHelper dbHelper;




    private static final String WANT_LIST = "Want-List";
    private static final String COLLECTION_LIST = "Collection";

    CollectionItems newItem;   // declsre globally..

    String username = "";
    String password = "";
    String listType = "";
    String friendsUserName = "";   // default


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_list_view2);


        // create database helper object...
        dbHelper = new MySQLiteHelper(this);


        // get Extra info passed from calling screen
        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {

            username = (String) b.get("username");
            password = (String) b.get("password");
            listType = (String) b.get("listType");
            friendsUserName =(String) b.get("friendsUserName");
        }

        listType.toLowerCase();


        // set variable for empty friends name
        Boolean getFriendsList = false;

        if (!(friendsUserName == null)) {

            if (!(friendsUserName.length() < 1)) {
                getFriendsList = true;  // friend name was sent...
            }
        }


        //TextView tvUsername = (TextView) findViewById(R.id.txt_wantlist_username);
        //TextView tvListtype = (TextView) findViewById(R.id.txt_wantlist_listtype);

        //tvUsername.setText(username+"'s");
        //tvListtype.setText(listtype);


        // add toolbar... --------------------------------------------------------------
        String paneTitle = "";

        if (!getFriendsList) {
            paneTitle = username + "'s" + " " + listType;   // get use stuff...
        } else {
            paneTitle = friendsUserName + "'s" + " " + listType;  // get friends stuff
        }


        //String paneTitle = "My collections";

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle(paneTitle);
        getSupportActionBar().setIcon(R.drawable.ic_menu);

        my_toolbar.findViewById(R.id.my_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(WantList.this, v);

                // This activity implements OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        //------- from here ---------------------------------------
                        final int result = 1;
                        Intent goToNextScreen;

                        switch (item.getItemId()) {
                            case R.id.menu_profile:
                                finish();
                                goToNextScreen = new Intent(WantList.this, UserProfile.class);
                                goToNextScreen.putExtra("username", username);
                                goToNextScreen.putExtra("password", password);

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_search:
                                finish();
                                goToNextScreen = new Intent(WantList.this, SearchActivity.class);
                                goToNextScreen.putExtra("username", username);
                                goToNextScreen.putExtra("password", password);

                                startActivity(goToNextScreen);

                                return true;

                            //----- changed - SAW  09/16/16 ---
                            case R.id.menu_collection:
                                finish();
                                goCollectionList();

                                return true;
                            case R.id.menu_wantlist:

                                finish();
                                goWantList();

                                return true;
                            // ---- end of chagne -------------

                            case R.id.menu_friends:
                                finish();
                                goToNextScreen = new Intent(WantList.this, Friends.class);
                                goToNextScreen.putExtra("username", username);
                                goToNextScreen.putExtra("password", password);
                                //goToNextScreen.putExtra("listType","Collection");

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_logout:
                                finish();
                                goToNextScreen = new Intent(WantList.this, AccountAccess.class);
                                startActivity(goToNextScreen);

                                return true;

                            default:

                               finish();
                               goToNextScreen = new Intent(WantList.this, UserProfile.class);
                               startActivity(goToNextScreen);

                                return false;
                        }
                        //--------------- to here ---------------------------------

                    }
                });

                popup.inflate(R.menu.actions);
                popup.show();

            }
        });
        //=======================================================================================


        ArrayList<CollectionItems> arrayOfItems = new ArrayList<CollectionItems>();
        final CollectionListAdapter adapter = new CollectionListAdapter(this, arrayOfItems);
        // get lists

        //Toast.makeText(this, "User: " + username + "   List: " + listType, Toast.LENGTH_SHORT).show();

        //makeBasiclist(adapter);

        if (!getFriendsList) {
            // get users info
            readFromDataBase(adapter, listType, username);
        } else {
            // get friends info
            readFromDataBase(adapter, listType, friendsUserName);
        }
        //readFromDataBase(adapter, listType);

        //-------------------------------------------------------------------
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.list_view2);
        listView.setAdapter(adapter);


        //-------------------------------------------------------------------
        // get list view in xml screen
        //SAW//ListView theListView = (ListView) findViewById(R.id.list_view2);
        //SAW//theListView.setAdapter(theAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int menuSelected, long l) {

                //String menuItemSelected = "Menu item selected was at position: " + menuSelected + "--> " + adapter.getItem(menuSelected).itemArtist;

                Intent goToNextScreen;
                // to to selected item screen
                goToNextScreen = new Intent(WantList.this, ItemScreen.class);
                goToNextScreen.putExtra("artist", adapter.getItem(menuSelected).itemArtist);
                goToNextScreen.putExtra("label", adapter.getItem(menuSelected).itemLabel);
                goToNextScreen.putExtra("year", adapter.getItem(menuSelected).itemYear);
                goToNextScreen.putExtra("URL", adapter.getItem(menuSelected).itemCoverURL);
                startActivity(goToNextScreen);

                // Toast.makeText(WantList.this,menuItemSelected, Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void makeBasiclist(CollectionListAdapter adapter) {

        String itemURL = "";
        // String array for menu items
        //SAW//String [] myListOfItems =  {"Beatles", "Madonna", "ABBA", "Prince", "R&B", "Rock"};;
        // Construct the data source
        //    ArrayList<CollectionItems> arrayOfItems = new ArrayList<CollectionItems>();
        // create list adapter


        // add item to DB
        // public BuildItemList(String owner, String imageurl, String whichlist, String artist, String album, String year) {


        //BuildItemList itemDB = new BuildItemList(this);


        itemURL = "http://1.bp.blogspot.com/-7k2Jnvoaigw/T9kzBww-rXI/AAAAAAAAC3M/c0xk-sgU7wM/s1600/The+Beatles+-+Beatles+for+Sale.jpg";
        //AddItemToDb("Steve",itemURL, WANT_LIST, "Beatles", "Columbia", "1962");

        // add item to DB
        //Toast.makeText(WantList.this, AddItemToDb("Steve", itemURL, WANT_LIST, "Beatles", "Columbia", "1962"), Toast.LENGTH_SHORT).show();

        // read DB

        //Toast.makeText(WantList.this, readFromDB(),Toast.LENGTH_LONG).show();


        //SAW//ListAdapter theAdapter = new MyAdapter2(this,myListOfItems);
        // Create the adapter to convert the array to views
        //   final CollectionListAdapter adapter = new CollectionListAdapter(this, arrayOfItems);

        // Add item to adapter
        CollectionItems newItem;

        if (listType.equals("Collection")) {

            callGetCollection();
            itemURL = "http://1.bp.blogspot.com/-7k2Jnvoaigw/T9kzBww-rXI/AAAAAAAAC3M/c0xk-sgU7wM/s1600/The+Beatles+-+Beatles+for+Sale.jpg";
            newItem = new CollectionItems("Beatles", "Columbia", "1962", itemURL);
            adapter.add(newItem);

            itemURL = "https://s.yimg.com/fz/api/res/1.2/7GzBaZrJQJMNhFfKJmMY8A--/YXBwaWQ9c3JjaGRkO2g9NTAwO3E9OTU7dz01MDA-/http://www.amiright.com/album-covers/images/album-Kiss-Destroyer.jpg";
            newItem = new CollectionItems("ABBA", "Apple Records", "1968", itemURL);
            adapter.add(newItem);

        }

        if (listType.equals("Want-List")) {
            itemURL = "https://api-img.discogs.com/geB50ZYvOZV0Rr1WGKLaQebURkE=/fit-in/150x150/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-3958459-1459319302-8511.jpeg.jpg?token=PwmXNjrBWHFcWsiqSfLKlouUaCGHPTVWrjZRpGHC";  //"http://cps-static.rovicorp.com/3/JPG_400/MI0002/285/MI0002285831.jpg?partner=allrovi.com";
            newItem = new CollectionItems("R&B", "Alantic Records", "1972", itemURL);
            adapter.add(newItem);
        }

        /*//-------------------------------------------------------------------
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.list_view2);
        listView.setAdapter(adapter);


        //-------------------------------------------------------------------
        // get list view in xml screen
        //SAW//ListView theListView = (ListView) findViewById(R.id.list_view2);
        //SAW//theListView.setAdapter(theAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int menuSelected, long l) {

                //String menuItemSelected = "Menu item selected was at position: " + menuSelected + "--> " + adapter.getItem(menuSelected).itemArtist;

                Intent goToNextScreen;
                // to to selected item screen
                goToNextScreen = new Intent(WantList.this, ItemScreen.class);
                goToNextScreen.putExtra("artist", adapter.getItem(menuSelected).itemArtist);
                goToNextScreen.putExtra("label", adapter.getItem(menuSelected).itemLabel);
                goToNextScreen.putExtra("year", adapter.getItem(menuSelected).itemYear);
                goToNextScreen.putExtra("URL", adapter.getItem(menuSelected).itemCoverURL);
                startActivity(goToNextScreen);

                // Toast.makeText(WantList.this,menuItemSelected, Toast.LENGTH_SHORT).show();

            }
        });
        */
    }

    public void callGetCollection() {

        // Add item to adapter
        CollectionItems newItem;

        // get json file...

/*
        // process json file...
        if (listType.equals("Collection")) {

            callGetCollection();
            itemURL = "http://1.bp.blogspot.com/-7k2Jnvoaigw/T9kzBww-rXI/AAAAAAAAC3M/c0xk-sgU7wM/s1600/The+Beatles+-+Beatles+for+Sale.jpg";
            newItem = new CollectionItems("Beatles", "Columbia", "1962", itemURL);
            adapter.add(newItem);

            itemURL = "https://s.yimg.com/fz/api/res/1.2/7GzBaZrJQJMNhFfKJmMY8A--/YXBwaWQ9c3JjaGRkO2g9NTAwO3E9OTU7dz01MDA-/http://www.amiright.com/album-covers/images/album-Kiss-Destroyer.jpg";
            newItem = new CollectionItems("ABBA", "Apple Records", "1968", itemURL);
            adapter.add(newItem);

        }

        if (listType.equals("Want-List")) {
            itemURL = "http://cps-static.rovicorp.com/3/JPG_400/MI0002/285/MI0002285831.jpg?partner=allrovi.com";
            newItem = new CollectionItems("R&B", "Alantic Records", "1972", itemURL);
            adapter.add(newItem);
        }
        */

    }

    public void goGetCollectionList() {

    }

    public String AddItemToDb(String owner, String imageurl, String whichlist, String artist, String album, String year) {

        String theResult;

        //===========================================================
        SQLiteDatabase discogetDB;
        //MySQLiteHelper dbHelper;
        MySQLiteHelper dbHelper = new MySQLiteHelper(getApplicationContext());

        // Gets the data repository in write mode

        discogetDB = dbHelper.getWritableDatabase();

        // Records to DB - items table

        String tempURL = "http://software55.net/images/integrationLogo2.gif";

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_OWNER, owner);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_ITEMURL, tempURL);

        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_IMAGEURL, imageurl);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_WHICHLIST, whichlist);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_ARTIST, artist);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_ALBUM, album);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_ALBUMYEAR, year);

        //values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = discogetDB.insert(ItemReaderContract.ItemEntry.TABLE_NAME, null, values);

        theResult = "New Record Added " + newRowId;

        //Toast.makeText(this, theResult, Toast.LENGTH_SHORT).show();
        return theResult;
        //================================================

    }

    public void readFromDataBase(CollectionListAdapter adapter, String listType, String userNameToUse) {

        String itemArtist = ""; // resultSet.getString(0);
        String itemAlbumLabel = ""; //  resultSet.getString(1);
        String itemAlbumYear = ""; //  resultSet.getString(2);
        String itemAlbumCoverURL = ""; //  resultSet.getString(3);

        String tokenString = "?token=PwmXNjrBWHFcWsiqSfLKlouUaCGHPTVWrjZRpGHC";

        // open database
        // add data to text fields...
        discogetDB = dbHelper.getWritableDatabase();

        // get user data...
        /*
        Cursor resultSet = mydatbase.rawQuery("Select * from TutorialsPoint",null);
        resultSet.moveToFirst();
        String username = resultSet.getString(1);
        String password = resultSet.getString(2);
         */
        Cursor resultSet = discogetDB.rawQuery("SELECT artist, album, albumyear, imageurl FROM items WHERE " +
                "owner= '" + userNameToUse +"' AND whichlist= '" + listType + "'", null);

        resultSet.moveToFirst();


        /*  cursor loop example
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            array[i] = cursor.getString(0);
            i++;
            cursor.moveToNext();

         */

        //uid = resultSet.getString(0);
        //pw  = resultSet.getString(1);
        //token  = resultSet.getString(2);

        // loop through database...
        while (!resultSet.isAfterLast()) {

            // get files
            itemArtist = resultSet.getString(0);
            itemAlbumLabel = resultSet.getString(1);
            itemAlbumYear = resultSet.getString(2);
            itemAlbumCoverURL = resultSet.getString(3); //+ tokenString;


            if (debug) {  Toast.makeText(this,itemAlbumCoverURL,Toast.LENGTH_LONG).show(); }
            
            // add to array
            //itemURL = "http://1.bp.blogspot.com/-7k2Jnvoaigw/T9kzBww-rXI/AAAAAAAAC3M/c0xk-sgU7wM/s1600/The+Beatles+-+Beatles+for+Sale.jpg";
            //newItem = new CollectionItems("Beatles", "Columbia", "1962", itemURL);
            newItem = new CollectionItems(itemArtist, itemAlbumLabel, itemAlbumYear, itemAlbumCoverURL);
            adapter.add(newItem);
            

            // go to next record
            resultSet.moveToNext();
        } // end of whileloop...


        // closeDB
        dbHelper.close();


        //Toast.makeText(WantList.this, AddItemToDb("Steve", itemURL, WANT_LIST, "Beatles", "Columbia", "1962"), Toast.LENGTH_SHORT).show();

    }


    public String readFromDB() {

        //MySQLiteHelper dbHelper;
        MySQLiteHelper dbHelper = new MySQLiteHelper(getApplicationContext());

        SQLiteDatabase discogetdb = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ItemReaderContract.ItemEntry._ID,
                ItemReaderContract.ItemEntry.COLUMN_NAME_ARTIST,
                ItemReaderContract.ItemEntry.COLUMN_NAME_ALBUM,
                ItemReaderContract.ItemEntry.COLUMN_NAME_ALBUMYEAR,
                ItemReaderContract.ItemEntry.COLUMN_NAME_IMAGEURL
        };

        // Filter results WHERE "owner" = 'username'
        String selection = ItemReaderContract.ItemEntry.COLUMN_NAME_OWNER + " = sawerdeman55";
        String[] selectionArgs = {"All Owner Items"};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ItemReaderContract.ItemEntry.COLUMN_NAME_OWNER;

        /*Cursor c = discogetdb.query(
                ItemReaderContract.ItemEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        */

        Cursor cursor = discogetdb.rawQuery("Select * FROM items",null);

        int artistColumn = cursor.getColumnIndex("artist");
        int albumColummn = cursor.getColumnIndex("album");

        cursor.moveToFirst();

        String dataList = "";

        // check for data
        if (cursor != null && cursor.getCount() > 0){

            int ctr = 0;
            do {

                dataList += "row "+ ctr++ + ":  "+ cursor.getString(artistColumn) +" -- " + cursor.getString(albumColummn)+ "\n";

            } while (cursor.moveToNext());



            cursor.close();

            return dataList;


        }





       // c.moveToFirst();
        //long itemId = c.getLong(c.getColumnIndexOrThrow(ItemReaderContract.ItemEntry._ID));
       // String itemArtist =  c.getString(c.getColumnIndex("artist"));
        //c.getString(c.getColumnIndexOrThrow(ItemReaderContract.ItemEntry.COLUMN_NAME_ARTIST));

       /* if (cursor.moveToFirst()){
            Toast.makeText(this,"move to first",Toast.LENGTH_LONG).show();
            do{
                String data = c.getString(c.getColumnIndex("data"));
                // do what ever you want here
            }while(c.moveToNext());
        }

        */
        cursor.close();

        return "hi steve";

    }



    public void goHome(View view) {

        //TODO need to finish
        String toastString = "go home...";
        if (debug) {  Toast.makeText(WantList.this,toastString, Toast.LENGTH_SHORT).show(); }

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent (this, AccountAccess.class);
        final int result = 1;
        startActivity(goToNextScreen);
    }


    public void goCollectionList() {

        String listType = "collection";

        //TODO need to finish
        //String toastString = "go Collection...";
        //Toast.makeText(UserProfile.this, toastString, Toast.LENGTH_SHORT).show();

        TextView uid = (TextView) findViewById(R.id.txt_profile_userName);

        //TODO.....
        // go to list screen....
        Intent goToNextScreen = new Intent(this, WantList.class);
        goToNextScreen.putExtra("username",username );
        goToNextScreen.putExtra("listType", listType);
        final int result = 1;
        startActivity(goToNextScreen);
    }

    public void goWantList () {
        String listType = "want-list";
        TextView uid = (TextView) findViewById(R.id.txt_profile_userName);

        //TODO need to finish
        //String toastString = "go Want-List...";
        //Toast.makeText(UserProfile.this, toastString, Toast.LENGTH_SHORT).show();

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent(this, WantList.class);
        final int result = 1;
        goToNextScreen.putExtra("username", username );
        goToNextScreen.putExtra("listType", listType);
        startActivity(goToNextScreen);
        finish();
    }

}



