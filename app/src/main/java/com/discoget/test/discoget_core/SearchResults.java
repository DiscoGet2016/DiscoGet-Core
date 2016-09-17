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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;



//import static com.discoget.test.outtestproject.R.id.the_list_view;

/**
 * Created by steve on 8/17/2016.
 */
public class SearchResults extends AppCompatActivity {

    private boolean debug = false; // use true for testing

    private SQLiteDatabase discogetDB;
    private MySQLiteHelper dbHelper;

    private String discogsJSONString;

    private static final String WANT_LIST = "Want-List";
    private static final String COLLECTION_LIST = "Collection";

    CollectionItems newItem;   // declear globally..

    String username = "";
    String password = "";
    String listType = "";
    String searchType = "";
    String searchValue = "";



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
           // username = (String) b.get("username");
           // password = (String) b.get("password");
           //listType = (String) b.get("listType");
            searchType = (String) b.get("searchType");
            searchValue = (String) b.get("searchValue");
        }


        //TextView tvUsername = (TextView) findViewById(R.id.txt_wantlist_username);
        //TextView tvListtype = (TextView) findViewById(R.id.txt_wantlist_listtype);

        //tvUsername.setText(username+"'s");
        //tvListtype.setText(listtype);

        // add toolbar... --------------------------------------------------------------

        String paneTitle = searchType + " search for: " + searchValue;  //username + "'s" + " " + listType;
        //String paneTitle = "My collections";

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle(paneTitle);
        getSupportActionBar().setIcon(R.drawable.ic_menu);

        my_toolbar.findViewById(R.id.my_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(SearchResults.this, v);

                // This activity implements OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        final int result = 1;
                        Intent goToNextScreen;
                        finish();

                        switch (item.getItemId()) {
                            case R.id.menu_profile:
                                goToNextScreen = new Intent(SearchResults.this, UserProfile.class);
                                goToNextScreen.putExtra("username", username);
                                goToNextScreen.putExtra("password", password);

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_search:
                                goToNextScreen = new Intent(SearchResults.this, SearchActivity.class);
                                goToNextScreen.putExtra("username", username);
                                goToNextScreen.putExtra("password", password);

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_collection:
                                goToNextScreen = new Intent(SearchResults.this, SearchResults.class);
                                goToNextScreen.putExtra("username", username);
                                goToNextScreen.putExtra("password", password);
                                goToNextScreen.putExtra("listType", "Collection");

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_wantlist:
                                goToNextScreen = new Intent(SearchResults.this, SearchResults.class);
                                goToNextScreen.putExtra("username", username);
                                goToNextScreen.putExtra("password", password);
                                goToNextScreen.putExtra("listType", "Want-List");
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_friends:
                                goToNextScreen = new Intent(SearchResults.this, Friends.class);
                                goToNextScreen.putExtra("username", username);
                                goToNextScreen.putExtra("password", password);
                                //goToNextScreen.putExtra("listType","Collection");

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_logout:
                                finish();
                                goToNextScreen = new Intent(SearchResults.this, AccountAccess.class);
                                startActivity(goToNextScreen);

                                return true;

                            default:


                                goToNextScreen = new Intent(SearchResults.this, UserProfile.class);
                                startActivity(goToNextScreen);

                                return false;
                        }

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

        //makeBasiclist(adapter);
        //readFromDataBase(adapter, listType);

        getSearchJSONData(adapter);  // gets JSON data from Discogs and add it to the adapter...


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
                goToNextScreen = new Intent(SearchResults.this, ItemScreen.class);
                goToNextScreen.putExtra("artist", adapter.getItem(menuSelected).itemArtist);
                goToNextScreen.putExtra("label", adapter.getItem(menuSelected).itemLabel);
                goToNextScreen.putExtra("year", adapter.getItem(menuSelected).itemYear);
                goToNextScreen.putExtra("URL", adapter.getItem(menuSelected).itemCoverURL);
                startActivity(goToNextScreen);

                // Toast.makeText(WantList.this,menuItemSelected, Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getSearchJSONData(CollectionListAdapter adapter) {

        // get JSON String  ... need async task

        // parse JSON string

        // add values to adapter...

        // done.


        //-------------
        //https://api.discogs.com/users/mrsangha/collection
        //String userNameString = "mrSangha";


        //String urlBaseString = "https://api.discogs.com/oauth/request_token";
        String urlBaseString = "https://api.discogs.com/database/search?";

        String userToken = "PwmXNjrBWHFcWsiqSfLKlouUaCGHPTVWrjZRpGHC";

        if (searchType.equals("barcode")) {

            urlBaseString += "barcode=" + "831596202"; //searchValue;

        } else {
            urlBaseString += "title=" + searchValue;
        }


        urlBaseString += "&token=" + userToken;


        urlBaseString = "https://api.discogs.com/database/search?barcode=831596202&token=PwmXNjrBWHFcWsiqSfLKlouUaCGHPTVWrjZRpGHC";



        /*  SAVE - junk...
        //barcode=801061939113"; //+ userNameString;
        //String urlSelection = "";
        //https://api-img.discogs.com/HC32UPFMHdy4Scd77aPTJvXH0Vs=/fit-in/150x150/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-4663501-1371492072-2956.jpeg.jpg&token=PwmXNjrBWHFcWsiqSfLKlouUaCGHPTVWrjZRpGHC

        String urlBasicString = "https://api.discogs.com/";
        String userIDString  = "users/" + userName;

        //String databaseSearch = "database/search";
        //String urlQuery = "q=Beatles";
        //String urlCallingString = urlBasic + databaseSearch + "?" + urlQuery + "&token=" + userToken;
        /* --------------------------------------------------------------------------------------------------------
            // these work!!!  they get my info!!!
            String urlCallingStringProfile = "https://api.discogs.com/users/sawerdeman55?" +
                "token=PwmXNjrBWHFcWsiqSfLKlouUaCGHPTVWrjZRpGHC";
            String urlCallingStringCollection = "https://api.discogs.com/users/sawerdeman55/collection?" +
                    "token=PwmXNjrBWHFcWsiqSfLKlouUaCGHPTVWrjZRpGHC";
            String urlCallingStringWantList = "https://api.discogs.com/users/sawerdeman55/wants?" +
                    "token=PwmXNjrBWHFcWsiqSfLKlouUaCGHPTVWrjZRpGHC";
        * ----------------------------------------------------------------------------------------------------------
        */

        // build account processing strings
        String urlCallingString =  urlBaseString;

        Toast.makeText(this,urlCallingString,Toast.LENGTH_LONG);


        /* more junt to bmremove // TODO -- remove...
        //steve1; //"https://api.discogs.com/database/search?barcode=801061939113"; //urlBaseString + urlUsername + urlSelection;
        //"https://api.discogs.com/users/sawerdeman55";
        // show response on the EditText etResponse
        //etResponse.setText(GET("http://hmkcode.com/examples/index.php"));

        // get profile info...
        //new HttpAsyncTask().execute("https://api.discogs.com/users/sawerdeman55");
        */


        // check if you are connected or not
        if(isConnected()){
            // call AsynTask to perform network operation on separate thread
            String jsonTestString;   // temp JSON string

            try {
               Toast.makeText(this,"Sync Started: Please wait",Toast.LENGTH_LONG).show();

                jsonTestString = new HttpAsyncTask().execute(urlCallingString).get();

                Toast.makeText(this,jsonTestString,Toast.LENGTH_LONG);

                //addResultsToList(adapter,jsonTestString);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this,"Connection not available",Toast.LENGTH_LONG).show();
         }
     }



    private void addResultsToList(CollectionListAdapter adapter, String jsonData) {


        String userLsitToProcess = jsonData;//getJSONStringFromDiscogs(username, userToken, listtype);

        String whichListType = "search";

        String userItemJSONString = userLsitToProcess; // assign value to parse here...

        String jsonListSelector = "";  // used to select releases or wants

        if (whichListType == "collection") {
            jsonListSelector = "releases";
        } else if(whichListType == "want-list"){
            jsonListSelector = "wants";
        } else { // assume search
            jsonListSelector = "results";
        }



        Toast.makeText(this,"List selector: " + jsonListSelector,Toast.LENGTH_LONG).show();


        JSONObject userItemList;   // generic name...

        //  discogetDB = dbHelper.getWritableDatabase();

        try {

            /* basic layout of  List here
                // TODO need to add layout text here...
             */


            userItemList = new JSONObject(userItemJSONString);

            // get page info

            JSONObject page = userItemList.getJSONObject("pagination");  // same for both

            // set page info
            String numOfItems = page.getString("items");
            String numOFPages = page.getString("pages");
            //String nextPage   = page.getString("nextpage");   // TODO need to verify...

            String item_ownerid = username;        // username
            String item_itemurl = "";     // release-basicinfo = "resource_url"
            String item_imageurl = "";           // release-basicinfo = "thumb"
            String item_barcode = "";            // ?
            String item_shortdescription = "";   // ?
            String item_whichlist = whichListType;  // Collections
            String item_artist = "";             // release-basicinfo-artist = "name"
            String item_album = "";              // release-basicinfo-labels = "name"
            String item_albumYear = "";               // release-basicinfo = "year"
            String item_catalognumber = "";      // ?



            // get releases array
            JSONArray releases = userItemList.getJSONArray(jsonListSelector);  //was "releases"

            Toast.makeText(this,"made it this far...",Toast.LENGTH_LONG).show();

            // set release array length
            String arrayLength = Integer.toString(releases.length());


            // loop thru releases (for this page...  // TODO need to process multiple pages...

            /*  // EXAMPLE TABLE LAYOUT...
                discogetDB.execSQL("CREATE TABLE IF NOT EXISTS items " +
                    "(id integer primary key autoincrement , owner VARCHAR, itemurl VARCHAR," +
                    "imageurl VARCHAR, barcode VARCHAR, shortdescription VARCHAR," +
                    "whichlist VARCHAR, artist VARCHAR, album VARCHAR, albumyear VARCHAR," +
                    "catalogid VARCHAR, deleteflag VARCHAR);");

            */

            for (int i=0;i < releases.length(); i++) {  // loop through array

                // get current release array object
                JSONObject item = releases.getJSONObject(i);

                // break it down... Basic INFO
                JSONObject basicinfo = item.getJSONObject("basic_information");

                item_itemurl = basicinfo.getString("resource_url");
                item_imageurl = basicinfo.getString("thumb");
                item_albumYear =  basicinfo.getString("year");


               // JSONArray labels = basicinfo.getJSONArray("labels");
               // JSONObject label1 = labels.getJSONObject(0);

                item_album = "label"; //label1.getString("name");

                JSONArray artist = basicinfo.getJSONArray("artists");
                JSONObject artist1 = artist.getJSONObject(0);

                item_artist = artist1.getString("name");

                // create query string
                /*String queryStrValues =
                        "'" + username + "', " +
                                "'" + item_itemurl  + "', " +
                                "'" + item_imageurl + "', " +
                                "'" + item_barcode + "', " +
                                "'" + item_shortdescription + "', " +
                                "'" + item_whichlist + "', " +
                                "'" + item_artist.replace("'","''") + "', " +
                                "'" + item_album.replace("'","''") + "', " +   // added .replace -- SAW  09/13/16
                                "'" + item_albumYear + "', " +
                                "'" + item_catalognumber + "'";
                */

                // add adapter info
                //newItem = new CollectionItems("Beatles", "Columbia", "1962", itemImageURL);
                newItem = new CollectionItems(item_artist, item_album, item_albumYear, item_imageurl);
                adapter.add(newItem);


               /* if (debug) { Toast.makeText(this, "Query String = " + queryStrValues, Toast.LENGTH_LONG).show(); }

                // insert data into user table
                discogetDB.execSQL("INSERT INTO items (owner, itemurl, imageurl, barcode, shortdescription, whichlist, artist, album, albumyear, catalogid )" +
                        " VALUES (" + queryStrValues + ")");

                */

            }   // end of release loop...

            //close DB
            // dbHelper.close();

        } catch (JSONException e) {
            e.printStackTrace();

            if (debug) { Toast.makeText(this,"collection list - JSON Error...",Toast.LENGTH_LONG).show(); }
        }
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

    public void readFromDataBase(CollectionListAdapter adapter, String listType) {

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
                "owner= '" + username +"' AND whichlist= '" + listType + "'", null);

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
        if (debug) {  Toast.makeText(SearchResults.this,toastString, Toast.LENGTH_SHORT).show(); }

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent (this, AccountAccess.class);
        final int result = 1;
        startActivity(goToNextScreen);
    }




    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        //private TextView message = (TextView) findViewById(R.id.tv_createAccount_message);

        @Override
        protected String doInBackground(String... urls) {

          //  Toast.makeText(getBaseContext(), "starting Get with: " , Toast.LENGTH_SHORT).show();
            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (debug) {  Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_SHORT).show(); }
            //etResponse.setText(result);
            discogsJSONString = result.toString();

            if (debug) { Toast.makeText(getBaseContext(),discogsJSONString,Toast.LENGTH_LONG).show();}

        }


    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


}



