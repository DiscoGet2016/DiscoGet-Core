package com.discoget.test.discoget_core;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


//import static com.discoget.test.outtestproject.R.id.the_list_view;

/**
 * Created by steve on 8/17/2016.
 * ============================================
 * 9/14/16 - SAW
 * modified from SearchResults... and WantList classes...
 *
 * cleaned up and removed unused options...
 *
 * fixed async portion...
 *
 */
public class DisplaySearchResults extends AppCompatActivity {

    private static String SEARCH_TYPE_BARCODE = "barcode";
    private static String SEARCH_TYPE_TITLE = "title";

    private boolean debug = false; // use true for testing

    private SQLiteDatabase discogetDB;
    private MySQLiteHelper dbHelper;

    private String discogsJSONString;


    CollectionItems newItem;   // declare globally..

    String username = "";
    String password = "";
    String listType = "";
    String searchType = "";
    String searchValue = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_list_view2);


        // get Extra info passed from calling screen
        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            username = (String) b.get("username");
            password = (String) b.get("password");
           //listType = (String) b.get("listType");
            searchType = (String) b.get("searchType");
            searchValue = (String) b.get("searchValue");
        }

        // title for toolbar...
         // WordUtils.capitalize("your string")
        String searchTitle = "";

        if (searchType.equals(SEARCH_TYPE_BARCODE)) {
            searchTitle = "Barcode";
        } else {
            searchTitle = "Title";
        }

        String paneTitle = searchTitle + " search for: " + searchValue;  //username + "'s" + " " + listType;


        // add toolbar... --------------------------------------------------------------
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle(paneTitle);
        getSupportActionBar().setIcon(R.drawable.ic_menu);

        my_toolbar.findViewById(R.id.my_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(DisplaySearchResults.this, v);
                // This activity implements OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        final int result = 1;
                        Intent goToNextScreen;
                        finish();
                        switch (item.getItemId()) {
                            case R.id.menu_profile:

                                goToNextScreen = new Intent(DisplaySearchResults.this, UserProfile.class);
                                goToNextScreen.putExtra("username", username);
                                goToNextScreen.putExtra("password", password);

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_search:
                                goToNextScreen = new Intent(DisplaySearchResults.this, SearchActivity.class);
                                goToNextScreen.putExtra("username", username);
                                goToNextScreen.putExtra("password", password);

                                startActivity(goToNextScreen);

                                return true;

                            //----- changed - SAW  09/16/16 ---
                            case R.id.menu_collection:

                                goCollectionList();

                                return true;
                            case R.id.menu_wantlist:

                                goWantList();

                                return true;
                            // ---- end of chagne -------------

                            case R.id.menu_friends:
                                goToNextScreen = new Intent(DisplaySearchResults.this, Friends.class);
                                goToNextScreen.putExtra("username", username);
                                goToNextScreen.putExtra("password", password);
                                //goToNextScreen.putExtra("listType","Collection");

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_logout:
                                goToNextScreen = new Intent(DisplaySearchResults.this, AccountAccess.class);
                                startActivity(goToNextScreen);

                                return true;
                            default:
                                goToNextScreen = new Intent(DisplaySearchResults.this, UserProfile.class);
                                startActivity(goToNextScreen);

                                return false;
                        }

                    }
                });

                popup.inflate(R.menu.actions);
                popup.show();

            }
        });
        //=================== end of tool bar stuff... ==========================================

        // build array adapter
        ArrayList<CollectionItems> arrayOfItems = new ArrayList<CollectionItems>();
        final CollectionListAdapter adapter = new CollectionListAdapter(this, arrayOfItems);

        // get lists  - Search Discogs for JSON results...
        //  add list to adapter..

            getSearchJSONData(adapter);  // gets JSON data from Discogs and add it to the adapter...


            // Attach the adapter to a ListView
            ListView listView = (ListView) findViewById(R.id.list_view2);
            listView.setAdapter(adapter);

            // list is displayed and wait for user to select an item...
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int menuSelected, long l) {

                    //String menuItemSelected = "Menu item selected was at position: " + menuSelected + "--> " + adapter.getItem(menuSelected).itemArtist;

                    // set intent for Item selectio0n screen...
                    Intent goToNextScreen;
                    // to to selected item screen
                    goToNextScreen = new Intent(DisplaySearchResults.this, ItemScreen.class);
                    goToNextScreen.putExtra("artist", adapter.getItem(menuSelected).itemArtist);
                    goToNextScreen.putExtra("label", adapter.getItem(menuSelected).itemLabel);
                    goToNextScreen.putExtra("year", adapter.getItem(menuSelected).itemYear);
                    goToNextScreen.putExtra("URL", adapter.getItem(menuSelected).itemCoverURL);
                    goToNextScreen.putExtra("release_id", adapter.getItem(menuSelected).itemResourceID);
                    goToNextScreen.putExtra("listtype", adapter.getItem(menuSelected).itemListType);
                    goToNextScreen.putExtra("albumtitle", adapter.getItem(menuSelected).itemTitle);
                    goToNextScreen.putExtra("searchType", adapter.getItem(menuSelected).searchType);
                    startActivity(goToNextScreen);

                    // Toast.makeText(WantList.this,menuItemSelected, Toast.LENGTH_SHORT).show();
                }
            });

    }






    private void getSearchJSONData(CollectionListAdapter adapter) {

        String DEFAULT_USER_TOKEN = "PwmXNjrBWHFcWsiqSfLKlouUaCGHPTVWrjZRpGHC";

        // get JSON String  ... need async task

        // parse JSON string

        // add values to adapter...

        // done.


        //-------------
        //https://api.discogs.com/users/mrsangha/collection
        //String userNameString = "mrSangha";

        /**
         * Build url search string from Discogs abics and use4r Token
         *
         * Discogs basic serach string:  http://api.discogs.com/database/serach?
         *
         */

        //String urlBaseString = "https://api.discogs.com/oauth/request_token";
        String urlBaseString = "https://api.discogs.com/database/search?";

        if (searchType.equals("barcode")) {
            urlBaseString += "barcode=" + searchValue;
        } else {
            urlBaseString += "title=" + searchValue;
        }

        urlBaseString += "&token=" + DEFAULT_USER_TOKEN;

        // string should look like this...
        //urlBaseString = "https://api.discogs.com/database/search?barcode=831596202&token=PwmXNjrBWHFcWsiqSfLKlouUaCGHPTVWrjZRpGHC";

        // build urlCalling String...
        String urlCallingString =  urlBaseString;

        // Show resulting string for testing
        if (debug) { Toast.makeText(this,urlCallingString,Toast.LENGTH_LONG); }


        // establish connection to internet...
        //-----------------------------------------------------------------------------
        // check if you are connected or not
        if(isConnected()){
            // call AsynTask to perform network operation on separate thread
            String jsonTestString;   // temp JSON string

            try {
              // Toast.makeText(this,"Sync Started: Please wait",Toast.LENGTH_LONG).show();

              if (!debug) {
                  // call async - esle use test data...
                  jsonTestString = new HttpAsyncTask().execute(urlCallingString).get();
              } else {
                  //using test data...
                  jsonTestString = "{'pagination': {'per_page': 50, 'items': 4, 'page': 1, 'urls': {}, 'pages': 1}, 'results': [{'style': ['Pop Rock', 'Disco'], 'thumb': '', 'format': ['CD', 'Album', 'Reissue'], 'country': 'Germany', 'barcode': ['831 596-2 02 %'], 'uri': '/ABBA-ABBA/release/7528186', 'community': {'have': 1, 'want': 8}, 'label': ['Polydor'], 'catno': '0040 170', 'year': '1992', 'genre': ['Electronic', 'Rock', 'Pop'], 'title': 'ABBA - ABBA', 'resource_url': 'https://api.discogs.com/releases/7528186', 'type': 'release', 'id': 7528186}, {'style': ['Europop'], 'thumb': 'https://api-img.discogs.com/DmeiS2YNs97-061-GiRqsgaDjhQ=/fit-in/150x150/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-6295126-1415806005-8529.jpeg.jpg', 'format': ['CD', 'Album'], 'country': 'Europe', 'barcode': ['0 42283 15962 4', 'GEMA', 'LC 0309', 'AAD', '831 596-2 02 %', 'MADE IN GERMANY BY PMDC'], 'uri': '/ABBA-ABBA/release/6295126', 'community': {'have': 9, 'want': 18}, 'label': ['Polydor', 'Polar Music AB', 'Polar Music AB', 'Glenstudio', 'Metronome Studio, Stockholm', 'Metronome Studio, Stockholm', 'Union Songs AB', 'PMDC, Germany'], 'catno': '831 596-2', 'genre': ['Pop'], 'title': 'ABBA - ABBA', 'resource_url': 'https://api.discogs.com/releases/6295126', 'type': 'release', 'id': 6295126}, {'style': ['Pop Rock', 'Disco'], 'thumb': 'https://api-img.discogs.com/Bm7BanmTVA18gcthMbgSQyit0_I=/fit-in/150x150/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-7471706-1442158809-4351.jpeg.jpg', 'format': ['CD', 'Album'], 'country': 'Germany', 'barcode': ['831 596-2 02 /', 'BIEM / STEMRA', 'LC 0309', 'AAD', 'Made in Germany by PMDC'], 'uri': '/ABBA-ABBA/release/7471706', 'community': {'have': 1, 'want': 8}, 'label': ['Polydor', 'PMDC, Germany'], 'catno': '521 155-2', 'genre': ['Electronic', 'Rock', 'Pop'], 'title': 'ABBA - ABBA', 'resource_url': 'https://api.discogs.com/releases/7471706', 'type': 'release', 'id': 7471706}, {'style': ['Pop Rock'], 'thumb': 'https://api-img.discogs.com/yr9bbyx1DNql5NRSw4jFOaqEvrc=/fit-in/150x150/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-3209742-1320601400.jpeg.jpg', 'format': ['CD', 'Album', 'Reissue'], 'country': 'Germany', 'barcode': ['0 42283 15962 4', '831 596-2 02 %', 'GEMA', 'LC 0309', 'POL 899', 'AAD'], 'uri': '/ABBA-ABBA/release/3209742', 'community': {'have': 15, 'want': 22}, 'label': ['Polydor', 'Polar Music AB', 'Polar Music AB', 'Glenstudio', 'Metronome Studio, Stockholm'], 'catno': '831 596-2', 'year': '1987', 'genre': ['Pop'], 'title': 'ABBA - ABBA', 'resource_url': 'https://api.discogs.com/releases/3209742', 'type': 'release', 'id': 3209742}]}";
              }

                // assign
               // Toast.makeText(this,jsonTestString,Toast.LENGTH_LONG);

                addResultsToList(adapter,jsonTestString);

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


        String searchListToProcess = jsonData;//getJSONStringFromDiscogs(username, userToken, listtype);

        String whichListType = "search";

        String searchItemJSONString = searchListToProcess; // assign value to parse here...

        String jsonListSelector = "results";

        //Toast.makeText(this,"List selector: " + jsonListSelector,Toast.LENGTH_LONG).show();


        JSONObject searchItemList;   // generic name...

        try {

            /* basic layout of  List here
                // TODO need to add layout text here...
             */
            searchItemList = new JSONObject(searchItemJSONString);

            // get page info
            JSONObject page = searchItemList.getJSONObject("pagination");  // same for both

            String searchType = "title";

            // set page info
            String numOfItems = page.getString("items");
            String numOFPages = page.getString("pages");
            //String nextPage   = page.getString("nextpage");   // TODO need to verify...
            //String item_ownerid = username;        // username
            // create rest if items...  not all itesm are used at this time...
            String item_itemurl = "";               // release-basicinfo = "resource_url"
            String item_imageurl = "";              // release-basicinfo = "thumb"
            String item_barcode = "";               // ?
            String item_shortdescription = "";      // ?
            String item_whichlist = whichListType;  // Collections
            String item_albumtitle = "";
            String item_albumartist = "";             // release-basicinfo-artist = "name"
            String item_albumlabel = "";              // release-basicinfo-labels = "name"
            String item_albumYear = "";               // release-basicinfo = "year"
            String item_catalogid = "";      // ?
            String item_releaseid = "";

            // for additional parsine and selction
            String item_type = "";


            // get releases array
            JSONArray searchResultsArray = searchItemList.getJSONArray(jsonListSelector);  //was "releases"

           // Toast.makeText(this,"made it this far...",Toast.LENGTH_LONG).show();

            // set release array length
            String arrayLength = Integer.toString(searchResultsArray.length());


            // set defaults...
            item_itemurl    =  "";
            item_imageurl   =  ""; //checkURL(item.getString("thumb"));
            item_albumYear  =  ""; //item.getString("year");
            item_albumtitle =  ""; //item.getString("title");
            item_releaseid  =  "";


            // loop thru releases (for this page...  // TODO need to process multiple pages...
            for (int i=0;i < searchResultsArray.length(); i++) {  // loop through array

               /* // set defaults...
                item_itemurl    =  "";
                item_imageurl   =  ""; //checkURL(item.getString("thumb"));
                item_albumYear  =  ""; //item.getString("year");
                item_albumtitle =  ""; //item.getString("title");
                item_releaseid  =  "";
                */

                // get current release array object
                JSONObject item = searchResultsArray.getJSONObject(i);

                // break it down... Basic INFO
                //JSONObject basicinfo = item.getJSONObject("basic_information");

                item_type = item.getString("type");

                // for release_id use 'id'
                String jsonKeyStr = "id";  // or release_id
                //if (item.has(jsonKeyStr)) {
                    item_releaseid = item.getString(jsonKeyStr);
                //}

                if (item_type.equals("release")) {
                    // get info for this item... else skip

                    if (item.has("resource_url")) {
                        item_itemurl = item.getString("resource_url");
                    }
                    if (item.has("thumb")) {
                        item_imageurl = checkURL(item.getString("thumb"));
                    }
                    if (item.has("year")) {
                        item_albumYear = item.getString("year");
                    }
                    if (item.has("title")) {
                        item_albumtitle = item.getString("title");
                    }

                    if (item.has("label")) {
                        JSONArray label = item.getJSONArray("label");
                        // EXAMPLE... int value=itemArray.getInt(i);
                        // TODO need to fix this...
                        item_albumlabel = label.getString(0); //"Album label"; //label1.getString("name");
                    } else {
                        item_albumlabel = "Album label";
                    }

                    // add adapter info
                    //newItem = new CollectionItems("Beatles", "Columbia", "1962", itemImageURL);
                    // old-->newItem = new CollectionItems(item_albumTitle, item_albumLabel, item_albumYear, item_imageurl);
                    // new 091816-SAW

                    if (item_releaseid.length() > 1) {
                        newItem = new CollectionItems(searchType, item_albumartist, item_albumlabel, item_albumYear, item_imageurl, item_releaseid, listType, item_albumtitle);
                        //public CollectionItems(String itemArtist, String itemLabel, String itemYear,String itemCoverURL, String itemResourceID, String itemListType, String itemTitle) {
                    } else {
                        // return no rows found...
                        String brokenRecordImalgeURL = "http://1.bp.blogspot.com/_E5tv7Vm0fsY/TLzFjB74OvI/AAAAAAAAATU/LpgJ1s6a0b4/s1600/broken-record-765056.jpg";
                        newItem = new CollectionItems("No search results found: ", "try search again", "", brokenRecordImalgeURL, "", "", "");
                        adapter.add(newItem);

                    }

                    adapter.add(newItem);
                }

            }   // end of release loop...


        } catch (JSONException e) {
            e.printStackTrace();

            if (debug) { Toast.makeText(this,"collection list - JSON Error...",Toast.LENGTH_LONG).show(); }
        }
    }


    private String checkURL(String thumb) {

        if (thumb.length() < 1) {
            return "http://images.clipartpanda.com/record-clipart-blue-record-hi.png";
        }

        return thumb;

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

        /*if (listType.equals("Collection")) {

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
        */

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
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_RESOURCEID, tempURL);

        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_COVERURL, imageurl);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_LISTTYPE, whichlist);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_ALBUMARTIST, artist);
        values.put(ItemReaderContract.ItemEntry.COLUMN_NAME_ALBUMLABEL, album);
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
            newItem = new CollectionItems(itemArtist, itemAlbumLabel, itemAlbumYear, itemAlbumCoverURL, "", listType); // TODO -error...
            //newItem = new CollectionItems(item_albumTitle, item_albumLabel, item_albumYear, item_imageurl, item_resourceid);


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
                ItemReaderContract.ItemEntry.COLUMN_NAME_ALBUMARTIST,
                ItemReaderContract.ItemEntry.COLUMN_NAME_ALBUMLABEL,
                ItemReaderContract.ItemEntry.COLUMN_NAME_ALBUMYEAR,
                ItemReaderContract.ItemEntry.COLUMN_NAME_COVERURL
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
        if (debug) {  Toast.makeText(DisplaySearchResults.this,toastString, Toast.LENGTH_SHORT).show(); }

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



