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
public class DisplayFriendsSearchResults extends AppCompatActivity {

    String DEFAULT_USER_TOKEN = "PwmXNjrBWHFcWsiqSfLKlouUaCGHPTVWrjZRpGHC";

    private boolean debug = false; // use true for testing

    private SQLiteDatabase discogetDB;
    private MySQLiteHelper dbHelper;

    private String discogsJSONString;


    FriendsItems newFriend;   // declare globally..

    String username = "";
    String password = "";
    String listType = "";
    String searchType = "";
    String searchValue = "";

    // save user variables...
    String friendsUserName;




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
        String searchTitle = searchType;

        String paneTitle = searchTitle + " search for: " + searchValue;  //username + "'s" + " " + listType;


        // add toolbar... --------------------------------------------------------------
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle(paneTitle);
        getSupportActionBar().setIcon(R.drawable.ic_menu);

        my_toolbar.findViewById(R.id.my_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(DisplayFriendsSearchResults.this, v);
                // This activity implements OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        final int result = 1;
                        Intent goToNextScreen;
                        switch (item.getItemId()) {
                            case R.id.menu_profile:
                                goToNextScreen = new Intent(DisplayFriendsSearchResults.this, UserProfile.class);
                                goToNextScreen.putExtra("username", username);
                                goToNextScreen.putExtra("password", password);

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_search:
                                goToNextScreen = new Intent(DisplayFriendsSearchResults.this, SearchActivity.class);
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
                                goToNextScreen = new Intent(DisplayFriendsSearchResults.this, Friends.class);
                                goToNextScreen.putExtra("username", username);
                                goToNextScreen.putExtra("password", password);
                                //goToNextScreen.putExtra("listType","Collection");

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_logout:
                                goToNextScreen = new Intent(DisplayFriendsSearchResults.this, AccountAccess.class);
                                startActivity(goToNextScreen);

                                return true;

                            default:
                                goToNextScreen = new Intent(DisplayFriendsSearchResults.this, UserProfile.class);
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
        ArrayList<FriendsItems> arrayOfItems = new ArrayList<FriendsItems>();
        final FriendsListAdapter2 adapter = new FriendsListAdapter2(this, arrayOfItems);

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

                    String menuItemSelected = "Menu item selected was at position: " + menuSelected + "--> " + adapter.getItem(menuSelected).friendUserName;

                   /* // set intent for Item selectio0n screen...
                    Intent goToNextScreen;
                    // to to selected item screen
                    goToNextScreen = new Intent(DisplayFriendsSearchResults.this, ItemScreen.class);
                    goToNextScreen.putExtra("artist", adapter.getItem(menuSelected).itemArtist);
                    goToNextScreen.putExtra("label", adapter.getItem(menuSelected).itemLabel);
                    goToNextScreen.putExtra("year", adapter.getItem(menuSelected).itemYear);
                    goToNextScreen.putExtra("URL", adapter.getItem(menuSelected).itemCoverURL);
                    startActivity(goToNextScreen);

                    */
                    // Toast.makeText(WantList.this,menuItemSelected, Toast.LENGTH_SHORT).show();
                }
            });

    }






    private void getSearchJSONData(FriendsListAdapter2 adapter) {



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
        // to search for friends...
        String urlBaseString = "https://api.discogs.com/users/"; //{friends username}?token=token";

        // if (searchType.equals("barcode")) {
            urlBaseString +=  searchValue;
        //} else {
        //    urlBaseString += "title=" + searchValue;
        // }

        urlBaseString += "?token=" + DEFAULT_USER_TOKEN;

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



    private void addResultsToList(FriendsListAdapter2 adapter, String jsonData) {


        //String searchListToProcess = jsonData;//getJSONStringFromDiscogs(username, userToken, listtype);
        //JSONObject searchItemList;   // generic name...


        try {

            // Error message
            /*
                    { "message":"User does not exist or may have been deleted."  }
             */

            String jsonUsername;    // = jObject.optString("username");
            String jsonFullName;    // = jObject.optString("name");
            String jsonUserBio;     //  = jObject.optString("profile");
            String jsonUserEmail;   //
            String jsonUserImgUrl;  // = jObject.optString("avatar_url");

            // Start of JSON parsing...
            JSONObject jObject = new JSONObject(jsonData);

            if (!(jObject.has("message"))) {
                // should be a good JSON String...

                // now get the profile data needed...
                // ### we could verify user name here... if we want...

                //if (array.getJSONObject(i).has("link"))

                jsonUsername = jObject.optString("username");
                jsonFullName = jObject.optString("name");
                jsonUserBio  = jObject.optString("profile");

                //jsonUserEmail;

                if (jObject.has("email")) {
                    jsonUserEmail = jObject.optString("email");
                } else {
                    jsonUserEmail = "";
                }

                jsonUserImgUrl = jObject.optString("avatar_url");

            } else {
                // error message returned...
                jsonUsername = " ";
                jsonFullName = "No username match...";
                jsonUserImgUrl = " ";
            }

            friendsUserName = jsonUsername;   // save for future use... if added to friends list...

            // add adapter info
            //newItem = new CollectionItems("Beatles", "Columbia", "1962", itemImageURL);
            newFriend = new FriendsItems(jsonUsername, jsonFullName, jsonUserImgUrl);
                adapter.add(newFriend);


        } catch (JSONException e) {
            e.printStackTrace();

            if (debug) { Toast.makeText(this,"Friends list - JSON Error...",Toast.LENGTH_LONG).show(); }
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

       /* if (listType.equals("Collection")) {

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
        if (debug) {  Toast.makeText(DisplayFriendsSearchResults.this,toastString, Toast.LENGTH_SHORT).show(); }

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent (this, AccountAccess.class);
        final int result = 1;
        startActivity(goToNextScreen);
    }

    public void addToFriendsDB(View view) {

        //Toast.makeText(this,"add friend: " + friendsUserName ,Toast.LENGTH_SHORT).show();
        Intent goToNextScreen;
        goToNextScreen = new Intent(this, CreateFriendsAccount.class);
        goToNextScreen.putExtra("friendsUserName", friendsUserName);
        goToNextScreen.putExtra("token", DEFAULT_USER_TOKEN);

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



