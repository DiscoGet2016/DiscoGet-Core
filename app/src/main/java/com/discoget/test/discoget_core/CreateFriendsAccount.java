package com.discoget.test.discoget_core;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.concurrent.ExecutionException;

// added for Discogs JSON interface...


/**
 * Created by Steven on 9/11/2016.
 */

public class CreateFriendsAccount extends AppCompatActivity {

    private SQLiteDatabase discogetDB;
    private MySQLiteHelper dbHelper;

    private boolean debug = false;  // change to false for production

    private String discogsJSONString;


    // ---
    /*
    String userNameString = "djens";
    String steve1 = "https://api.discogs.com/database/search?q=Nirvana&token=PwmXNjrBWHFcWsiqSfLKlouUaCGHPTVWrjZRpGHC";
    String userKey = "";
    String userSecret = "";
    String userToken = "PwmXNjrBWHFcWsiqSfLKlouUaCGHPTVWrjZRpGHC";
    String urlBasic = "https://api.discogs.com/";
    String databaseSearch = "database/search";
    String urlQuery = "q=Beatles";
    */
    // ---


/*    EditText uid;
    EditText uPassword0;
    EditText uPassword1;

    EditText uToken;
*/
    // used for JSON object
    String jsonUsername; // = jObject.optString("username");
    String jsonFullName; // = jObject.optString("name");
    String jsonUserBio; //  = jObject.optString("profile");
    String jsonUserEmail; // = jObject.optString("email");
    String jsonUserImgUrl; // = jObject.optString("avatar_url");

  //  Button button; // = (Button) findViewById(R.id.btn_createAccount_save);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.create_user_account);


        String friendsUserName = "";
        String username="";    // =(String) b.get("username");
        String token ="";       // =(String) b.get("token");


        // get extras passed from calling page...
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            username =(String) b.get("username");
            friendsUserName = (String) b.get("friendsUserName");
            token =(String) b.get("token");
        }


        //button = (Button) findViewById(R.id.btn_createAccount_save);

        //---
        // set up http connection...
        // get reference to the views
        //-etResponse = (EditText) findViewById(R.id.etResponse);
        //-tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);

        discogsJSONString = null; // inits string...

         // set user fields
        //uid = (EditText) findViewById(R.id.et_createAccount_username);
        //uPassword0 = (EditText) findViewById(R.id.et_createAccount_password0);
        //uPassword1 = (EditText) findViewById(R.id.et_createAccount_password1);

        //uToken = (EditText) findViewById(R.id.et_createAccount_usertoken);


        // create database helper object...
        dbHelper = new MySQLiteHelper(this);


        //button.setClickable(true);


        // display screen and wait for input...
       // this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //addFriendToDB(username,token);

        processAccountInfo(friendsUserName,token);


        finish();  // close this activity

        Intent goToNextScreen = new Intent (this, Friends.class);
        // put username as pass to screenqaz!1234

        startActivity(goToNextScreen);


    }

    public void processAccountInfo(String userNameToUse, String userToken) {
        /*
            This method will get user info from screen, then request profile data from Discogs
            the Discogs JSON will be processed and stored into SQLite data base. - user table.

         */


        // get data from screen
        /*String userName = uid.getText().toString();
        String userPass0 = uPassword0.getText().toString();
        String userPass1 = uPassword1.getText().toString();
        String userToken = uToken.getText().toString();
        */

        // open DB
        discogetDB = dbHelper.getWritableDatabase();


        //String userToken = userToken;   //"PwmXNjrBWHFcWsiqSfLKlouUaCGHPTVWrjZRpGHC";
        String urlBasicString = "https://api.discogs.com/";
        String userIDString  = "users/" + userNameToUse;

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
            String urlCallingStringProfile    =  urlBasicString + userIDString + "?token=" + userToken;
            String urlCallingStringCollection =  urlBasicString + userIDString + "/collection" + "?token=" + userToken;
            String urlCallingStringWantList   =  urlBasicString + userIDString + "/wants" + "?token=" + userToken;

            //steve1; //"https://api.discogs.com/database/search?barcode=801061939113"; //urlBaseString + urlUsername + urlSelection;

        //"https://api.discogs.com/users/sawerdeman55";

        // show response on the EditText etResponse
        //etResponse.setText(GET("http://hmkcode.com/examples/index.php"));


        // get profile info...
        //new HttpAsyncTask().execute("https://api.discogs.com/users/sawerdeman55");

        // get want list data
        // check if you are connected or not
        if(isConnected()){
            // tvIsConnected.setBackgroundColor(0xFF00CC00);
            // tvIsConnected.setText("You are connected");

            // call AsynTask to perform network operation on separate thread
            String jsonTestString;


            try {
                //Toast.makeText(this,"Sync Started: Please wait",Toast.LENGTH_LONG).show();

                jsonTestString = new HttpAsyncTask().execute(urlCallingStringCollection).get();
                saveUserLists0(userNameToUse,userToken,"collection",jsonTestString);

                jsonTestString = new HttpAsyncTask().execute(urlCallingStringWantList).get();
                saveUserLists0(userNameToUse,userToken,"want-list",jsonTestString);

                jsonTestString = new HttpAsyncTask().execute(urlCallingStringProfile).get();
                saveFriendsProfileData(userNameToUse, userToken,jsonTestString);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            //EXMAPLE... String str_result= new RunInBackGround().execute().get();

            //Toast.makeText(this,"Please wait... getting Data...",Toast.LENGTH_LONG).show();

            //Toast.makeText(this,"Please wait... getting Data...",Toast.LENGTH_LONG).show();

            //Toast.makeText(this,discogsJSONString,Toast.LENGTH_LONG).show();

            //saveProfileData(userName, userToken);

            // working of these two... to combine into one...
            //saveUserCollection(userName,userToken);
            // saveUserWants(userName,userToken);
            //saveUserLists(userName,userToken,"collection");  // save collection list to DB
            //saveUserLists(userName,userToken,"want-list");  // save want list to DB

            // TODO need an edit option ... of what???

            //close DB
            dbHelper.close();

            // goto next screen...
        /*
            finish();  // close this activity

            Intent goToNextScreen = new Intent (this, AccountAccess.class);
            // put username as pass to screen

            startActivity(goToNextScreen);

           // Toast.makeText(this,discogsJSONString,Toast.LENGTH_LONG).show();
           */
        }
        else{
            // tvIsConnected.setText("You are NOT connected");
        /*
            Toast.makeText(this,"Connection not available",Toast.LENGTH_LONG).show();
           */
        }
    }


    private void saveFriendsProfileData(String userName, String userToken, String jsonString) {

        // get JSON string from Discogs - store in userJSONString

        String userJSONString = jsonString; //getJSONFromDiscogs(userName, userToken);

        try {
            JSONObject jObject = new JSONObject(userJSONString);

            // now get the profile data needed...
            // ### we could verify user name here... if we want...

            //if (array.getJSONObject(i).has("link"))


            jsonUsername = jObject.optString("username");
            jsonFullName = jObject.optString("name");
            jsonUserBio  = jObject.optString("profile");

            if (jObject.has("email")) {
                jsonUserEmail = jObject.optString("email");
            } else {
                jsonUserEmail = "";
            }

            jsonUserImgUrl = jObject.optString("avatar_url");


        } catch (JSONException e) {
            e.printStackTrace();
        }


        // create SQL string

        /* // Create DB tables
        // Agreement table  -- store agreement info and ...?
        discogetDB.execSQL("CREATE TABLE IF NOT EXISTS agreement " +
                "(id integer primary key, approved VARCHAR, agreementdate VARCHAR);");

        // add data to this table
        discogetDB.execSQL("INSERT INTO agreement (approved, agreementdate) VALUES ('yes!!!', 'today')");

        */

        // User table  -- store user data and user authication token with Discogs
        /* discogetDB.execSQL("CREATE TABLE IF NOT EXISTS user " +
                "(id integer primary key, uid VARCHAR, usertype VARCHAR, username VARCHAR," +
                "password VARCHAR, firstname VARCHAR, lastname VARCHAR, fullname VARCHAR, userbio, VARCHAR, emailaddress VARCHAR," +
                "mobilenumber VARCHAR, imageurl VARCHAR, discogstoken VARCHAR, discogskey VARCHAR,);");
        */

        // create temp imageURL string
        String imageURL = jsonUserImgUrl;    //"https://secure.gravatar.com/avatar/8f6328a88899ed68d8c913de6a10006d?s=500&r=pg&d=mm";

        // add data to this table

        // create query string
        String queryStrValues =
                "'" + userName + "', " +
                        "'friend', " +
                        "'" + "no password" + "', " +
                        "'" + "no token"  + "', " +
                        "'" + jsonFullName + "', " +
                        "'" + jsonUserBio + "', " +
                        "'" + jsonUserEmail + "', " +
                        "'" + imageURL + "'";

        /*String queryStrValues =
               "'" + uid.getText().toString() + "', " +
               "'primary', " +
               "'" + Password0.getText().toString() + "', " +
               "'" + uToken.getText().toString() + "', " +
               "'" + imageURL + "'";

          */

        //Toast.makeText(this, "Query String = "+ queryStrValues,Toast.LENGTH_LONG).show();
        // insert data into user table
        discogetDB.execSQL("INSERT INTO user (userid, usertype, password, discogstoken, fullname, profile, emailaddress, photourl ) VALUES (" + queryStrValues + ")");



        // TODO add other databases later...
        /*
        // Items -- store all items for user and friends
        discogetDB.execSQL("CREATE TABLE IF NOT EXISTS items " +
                "(id integer primary key autoincrement , owner VARCHAR, discogsitemurl VARCHAR," +
                "imageurl VARCHAR, barcode VARCHAR, shortdescription VARCHAR," +
                "whichlist VARCHAR, artist VARCHAR, album VARCHAR, year VARCHAR," +
                "catalognumber VARCHAR, deleteflag VARCHAR);");

        //   discogetDB.close();

        */


    }


    private String getJSONFromDiscogs(String username, String userToken) {

        // TODO add access to Discogs to get USER data...
        String swerdeman = "{\"profile\": \"\", \"wantlist_url\": \"https://api.discogs.com/users/swerdeman/wants\", " +
                "\"seller_num_ratings\": 0, \"rank\": 0.0, \"num_pending\": 0, \"id\": 3875072, \"buyer_rating\": 0.0, " +
                "\"num_for_sale\": 0, \"home_page\": \"\", \"location\": \"\", \"collection_folders_url\":" +
                " \"https://api.discogs.com/users/swerdeman/collection/folders\", \"email\": \"steve@arrrg.com\", \"username\":" +
                " \"swerdeman\", \"collection_fields_url\": \"https://api.discogs.com/users/swerdeman/collection/fields\"," +
                " \"releases_contributed\": 0, \"registered\": \"2016-09-05T19:39:28-07:00\", \"rating_avg\": 0.0," +
                " \"num_collection\": 0, \"releases_rated\": 0, \"curr_abbr\": \"USD\", \"seller_rating_stars\": 0.0, " +
                "\"num_lists\": 0, \"name\": \"\", \"buyer_rating_stars\": 0.0, \"num_wantlist\": 0, \"inventory_url\":" +
                " \"https://api.discogs.com/users/swerdeman/inventory\", \"uri\": \"https://www.discogs.com/user/swerdeman\", " +
                "\"buyer_num_ratings\": 0, \"avatar_url\": " +
                "\"https://secure.gravatar.com/avatar/8f6328a88899ed68d8c913de6a10006d?s=500&r=pg&d=mm\", \"resource_url\":" +
                " \"https://api.discogs.com/users/swerdeman\", \"seller_rating\": 0.0}";

        String djens = "{'username': 'djens', 'profile': '', 'num_collection': 242, 'collection_fields_url': 'https://api.discogs.com/users/djens/collection/fields'," +
                " 'releases_contributed': 0, 'rating_avg': 0.0, 'registered': '2007-12-20T15:27:48-08:00', 'wantlist_url': 'https://api.discogs.com/users/djens/wants'," +
                " 'seller_num_ratings': 0, 'rank': 1.0, 'releases_rated': 0, 'buyer_rating': 100.0, 'num_pending': 0, 'seller_rating_stars': 0.0," +
                " 'resource_url': 'https://api.discogs.com/users/djens', 'num_lists': 0, 'name': '', 'num_for_sale': 0, 'buyer_rating_stars': 5.0," +
                " 'home_page': '', 'num_wantlist': 53, 'inventory_url': 'https://api.discogs.com/users/djens/inventory'," +
                " 'uri': 'https://www.discogs.com/user/djens', 'id': 317613, 'buyer_num_ratings': 1," +
                " 'avatar_url': 'https://secure.gravatar.com/avatar/d4cb23b09d74f33ef8ad43fc0e0896f9?s=500&r=pg&d=mm'," +
                " 'location': 'San Diego, CA', 'collection_folders_url': 'https://api.discogs.com/users/djens/collection/folders'," +
                " 'seller_rating': 0.0,'email':'djens@gmail.com'}"; //sb.toString();

        String sawerdeman550 = "{'username': 'sawerdeman55', 'profile': 'Music Listener/ app developer / avid vinyl record collector, ok really just getting started... lol'," +
                "'num_collection': 2, 'collection_fields_url': 'https://api.discogs.com/users/sawerdeman55/collection/fields', 'releases_contributed': 0," +
                " 'rating_avg': 0.0, 'registered': '2016-07-14T09:26:27-07:00', 'wantlist_url': 'https://api.discogs.com/users/sawerdeman55/wants'," +
                " 'seller_num_ratings': 0, 'rank': 0.0, 'releases_rated': 0, 'buyer_rating': 0.0, 'num_pending': 0, 'seller_rating_stars': 0.0, " +
                "'resource_url': 'https://api.discogs.com/users/sawerdeman55', 'num_lists': 0, 'name': 'Steve Werdeman', 'num_for_sale': 0, " +
                "'buyer_rating_stars': 0.0, 'home_page': 'http://myVinylRecords.us', 'num_wantlist': 1, 'inventory_url': 'https://api.discogs.com/users/sawerdeman55/inventory'," +
                " 'uri': 'https://www.discogs.com/user/sawerdeman55', 'id': 3756980, 'buyer_num_ratings': 0, " +
                "'avatar_url': 'https://img.discogs.com/p1slvhBYwzxo3QvTkb724j3vF88=/500x500/filters:strip_icc():format(jpeg)/discogs-avatars/U-3756980-1469333844.jpeg.jpg', " +
                "'location': 'not specified', 'collection_folders_url': 'https://api.discogs.com/users/sawerdeman55/collection/folders', 'seller_rating': 0.0,'email':'stevewerdeman@gmail.com'}";


        String mrSangha = "{'username': 'MrSangha', 'profile': '', 'num_collection': 1, 'collection_fields_url': 'https://api.discogs.com/users/MrSangha/collection/fields'," +
                "'releases_contributed': 0, 'rating_avg': 0.0, 'registered': '2016-07-08T12:36:09-07:00', 'wantlist_url': 'https://api.discogs.com/users/MrSangha/wants'," +
                "'seller_num_ratings': 0, 'rank': 0.0, 'releases_rated': 0, 'buyer_rating': 0.0, 'num_pending': 0, 'seller_rating_stars': 0.0," +
                "'resource_url': 'https://api.discogs.com/users/MrSangha', 'num_lists': 0, 'name': '', 'num_for_sale': 0, 'buyer_rating_stars': 0.0, 'home_page': ''," +
                "'num_wantlist': 1, 'inventory_url': 'https://api.discogs.com/users/MrSangha/inventory', 'uri': 'https://www.discogs.com/user/MrSangha', 'id': 3743367," +
                "'buyer_num_ratings': 0, 'avatar_url': 'https://secure.gravatar.com/avatar/fe6f2f47c2863665ed1bd9d038fbd1ae?s=500&r=pg&d=mm', 'location': ''," +
                "'collection_folders_url': 'https://api.discogs.com/users/MrSangha/collection/folders', 'seller_rating': 0.0,'email':'sangha@gmail.com'}";



        String sawerdeman55 = "{\"profile\": \"Music Listener/ app developer / avid vinyl record collector, ok really just getting started... lol\", " +
                "\"wantlist_url\": \"https://api.discogs.com/users/sawerdeman55/wants\", \"seller_num_ratings\": 0, \"rank\": 0.0, \"num_pending\": 0," +
                " \"id\": 3756980, \"buyer_rating\": 0.0, \"num_for_sale\": 0, \"home_page\": \"http://myVinylRecords.us\", \"location\": \"not specified\", " +
                "\"collection_folders_url\": \"https://api.discogs.com/users/sawerdeman55/collection/folders\", \"email\": \"steven.werdeman@gmail.com\"," +
                " \"username\": \"sawerdeman55\", \"collection_fields_url\": \"https://api.discogs.com/users/sawerdeman55/collection/fields\", " +
                "\"releases_contributed\": 0, \"registered\": \"2016-07-14T09:26:27-07:00\", \"rating_avg\": 0.0, \"num_collection\": 2, \"releases_rated\": 0," +
                " \"curr_abbr\": \"USD\", \"seller_rating_stars\": 0.0, \"num_lists\": 0, \"name\": \"Steve Werdeman\", \"buyer_rating_stars\": 0.0, \"num_wantlist\": 1," +
                " \"inventory_url\": \"https://api.discogs.com/users/sawerdeman55/inventory\", \"uri\": \"https://www.discogs.com/user/sawerdeman55\", \"buyer_num_ratings\": 0, " +
                "\"avatar_url\": \"https://img.discogs.com/6Xi0RePaY19h5_1LOHzuHo2Bg-I=/500x500/filters:strip_icc():format(jpeg):quality(40)/discogs-avatars/U-3756980-1469333844.jpeg.jpg\", " +
                "\"resource_url\": \"https://api.discogs.com/users/sawerdeman55\", \"seller_rating\": 0.0}";


        if (username.equals("sawerdeman55")) {
            return sawerdeman55;
        }
        if (username.equals("djens")) {
            return  djens;
        }
        if (username.equals("mrSangha")) {
            return mrSangha;
        }

        if (username.equals("swerdeman")) {
            return swerdeman;
        }


       String userJSONToReturn = "";

        return userJSONToReturn;

    }

    private String getJSONStringFromDiscogs(String username, String userToken, String listtype) {

        // this wiil become the httpgeturl method whne done... fore now using canned strings
        // JSON STRINGS


        if (listtype=="collection") {
            return  getJSONListFromDiscogs(username, listtype);
        } else  if (listtype=="want-list") {
            return  getJSONListFromDiscogs(username, listtype);
        } else
            return  getJSONFromDiscogs(username, userToken);



    }

    private String getJSONListFromDiscogs(String username, String listtype) {

        switch (username){
            case "sawerdeman55":
                if (listtype=="collection") {
                    return  "{\"pagination\": {\"per_page\": 50, \"items\": 2, \"page\": 1, \"urls\": {}, \"pages\": 1}, \"releases\": [{\"collection_id\":" +
                            " 180036148, \"release_id\": 1255951, \"rating\": 0, \"basic_information\": {\"labels\": [{\"name\": \"Capitol Records\", \"entity_type\": \"1\", " +
                            "\"catno\": \"ST 2080\", \"resource_url\": \"https://api.discogs.com/labels/654\", \"id\": 654, \"entity_type_name\": \"Label\"}], \"formats\":" +
                            " [{\"descriptions\": [\"LP\", \"Album\", \"Stereo\"], \"name\": \"Vinyl\", \"qty\": \"1\"}], \"thumb\":" +
                            " \"https://api-img.discogs.com/-MlXhLdgQm1VYztTZQkEEs9Q-IM=/fit-in/150x150/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-1255951-1243961962.jpeg.jpg\", " +
                            "\"title\": \"The Beatles' Second Album\", \"artists\": [{\"join\": \",\", \"name\": \"The Beatles\", \"anv\": \"\", \"tracks\": \"\", \"role\": " +
                            "\"\", \"resource_url\": \"https://api.discogs.com/artists/82730\", \"id\": 82730}], \"resource_url\": \"https://api.discogs.com/releases/1255951\"," +
                            " \"year\": 1964, \"id\": 1255951}, \"date_added\": \"2016-07-16T20:03:58-07:00\"}, {\"collection_id\": 179748213, \"release_id\": 4953644, \"rating\":" +
                            " 0, \"basic_information\": {\"labels\": [{\"name\": \"Parlophone\", \"entity_type\": \"1\", \"catno\": \"PMC 1230\", \"resource_url\":" +
                            " \"https://api.discogs.com/labels/2294\", \"id\": 2294, \"entity_type_name\": \"Label\"}], \"formats\": [{\"descriptions\": [\"LP\", \"Album\", " +
                            "\"Mono\"], \"text\": \"Ernest J. Day sleeve\", \"name\": \"Vinyl\", \"qty\": \"1\"}], \"thumb\": " +
                            "\"https://api-img.discogs.com/-6p3iHjKnKRGC5S69ZqHXzwOWvw=/fit-in/150x150/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-4953644-1391695856-1514.jpeg.jpg\", " +
                            "\"title\": \"A Hard Day's Night\", \"artists\": [{\"join\": \"\", \"name\": \"The Beatles\", \"anv\": \"\", " +
                            "\"tracks\": \"\", \"role\": \"\", \"resource_url\": \"https://api.discogs.com/artists/82730\", \"id\": 82730}], " +
                            "\"resource_url\": \"https://api.discogs.com/releases/4953644\", \"year\": 1964, \"id\": 4953644}, \"date_added\": \"2016-07-14T13:03:29-07:00\"}]}";
                } else {
                    return  "{\"wants\": [{\"rating\": 0, \"basic_information\": {\"labels\": [{\"name\": \"Parlophone\"," +
                            "\"entity_type\": \"1\", \"catno\": \"PMC 1255\", \"resource_url\": \"https://api.discogs.com/labels/2294\", \"id\": 2294," +
                            " \"entity_type_name\": \"Label\"}], \"formats\": [{\"descriptions\": [\"LP\", \"Album\", \"Mono\"], \"name\": \"Vinyl\"," +
                            " \"qty\": \"1\"}], \"thumb\": " +
                            "\"https://api-img.discogs.com/3xWxlNlKCSpisMdSuUr5AiSjK5k=/fit-in/150x150/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-735564-1391696740-8760.jpeg.jpg\", " +
                            "\"title\": \"Help!\", \"artists\": [{\"join\": \",\", \"name\": \"The Beatles\", \"anv\": \"\", \"tracks\": \"\", \"role\": \"\", " +
                            "\"resource_url\": \"https://api.discogs.com/artists/82730\", \"id\": 82730}], \"resource_url\": \"https://api.discogs.com/releases/735564\"," +
                            " \"year\": 1965, \"id\": 735564}, \"notes\": \"\", \"date_added\": \"2016-07-14T13:05:15-07:00\", \"resource_url\":" +
                            " \"https://api.discogs.com/users/sawerdeman55/wants/735564\", \"id\": 735564}], \"pagination\": {\"per_page\": 50, \"items\": 1, \"page\": 1, " +
                            "\"urls\": {}, \"pages\": 1}}";

                }


            default:
                break;
        }

        return "error";

    }


    private void saveUserLists0(String usernamepassed, String userToken, String listtype, String jsonData) {


        //Toast.makeText(this, "user name passed: " + usernamepassed, Toast.LENGTH_SHORT).show();

        String userListToProcess = jsonData;//getJSONStringFromDiscogs(username, userToken, listtype);


        String whichListType = listtype;

        String userItemJSONString = userListToProcess; // assign value to parse here...

        String jsonListSelector = "";  // used to select releases or wants

        if (listtype == "collection") {
            jsonListSelector = "releases";
        } else {
            jsonListSelector = "wants";
        }


       // Toast.makeText(this,"List Type: " + listtype,Toast.LENGTH_LONG).show();


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

            String item_ownerid = usernamepassed;        // username
            String item_itemurl = "";     // release-basicinfo = "resource_url"
            String item_imageurl = "";           // release-basicinfo = "thumb"
            String item_barcode = "";            // ?
            String item_shortdescription = "";   // ?
            String item_whichlist = whichListType;  // Collections
            String item_albumtitle = "";
            String item_albumartist = "";             // release-basicinfo-artist = "name"
            String item_albumlabel = "";              // release-basicinfo-labels = "name"
            String item_albumYear = "";               // release-basicinfo = "year"
            String item_catalognumber = "";      // ?
            String item_catalogid = "";      // ?
            String item_resourceid = "";




            // get releases array
            JSONArray releases = userItemList.getJSONArray(jsonListSelector);  //was "releases"

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

                if (basicinfo.has("id")) {
                    item_resourceid = basicinfo.getString("id");
                } else {
                    item_resourceid ="000000";
                }

                item_itemurl = basicinfo.getString("resource_url");
                item_imageurl = basicinfo.getString("thumb");
                item_albumYear =  basicinfo.getString("year");
                item_albumtitle =  basicinfo.getString("title");

                JSONArray labels = basicinfo.getJSONArray("labels");
                JSONObject label1 = labels.getJSONObject(0);

                item_albumlabel = label1.getString("name");

                JSONArray artist = basicinfo.getJSONArray("artists");
                JSONObject artist1 = artist.getJSONObject(0);

                item_albumartist = artist1.getString("name");

                // create query string
             /*   String queryStrValues =
                        "'" + usernamepassed + "', " +
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
                String queryStrValues =
                        "'" + usernamepassed + "', " +
                                "'" + item_resourceid  + "', " +
                                "'" + item_catalogid + "', " +
                                "'" + item_albumtitle.replace("'","''") + "', " +
                                "'" + item_albumartist.replace("'","''") + "', " +
                                "'" + item_albumlabel.replace("'","''") + "', " +   // added .replace -- SAW  09/13/16
                                "'" + item_albumYear + "', " +
                                "'" + item_imageurl + "', " +
                                "'" + item_barcode + "', " +
                                "'" + item_shortdescription + "', " +
                                "'" + item_whichlist + "'";



                //debug = true;
                if (debug) { Toast.makeText(this, "Query String for : " + usernamepassed + " \n\n " + queryStrValues, Toast.LENGTH_LONG).show(); }

                // insert data into user table
            /*    discogetDB.execSQL("INSERT INTO items (owner, itemurl, imageurl, barcode, shortdescription, whichlist, artist, album, albumyear, catalogid )" +
                        " VALUES (" + queryStrValues + ")");
                        */
                discogetDB.execSQL("INSERT INTO items (owner, resourceid, catalogid, albumtitle, albumartist, albumlabel, albumyear, coverurl, barcode, shortdescription, listtype)" +
                        " VALUES (" + queryStrValues + ")");


            }   // end of release loop...

            //close DB
            // dbHelper.close();

        } catch (JSONException e) {
            e.printStackTrace();

            if (debug) { Toast.makeText(this,"collection list - JSON Error...",Toast.LENGTH_LONG).show(); }
        }
    }



    private void saveUserLists(String username, String userToken, String listtype) {


        String userLsitToProcess = getJSONStringFromDiscogs(username, userToken, listtype);


        String whichListType = listtype;

        String userItemJSONString = userLsitToProcess; // assign value to parse here...

        String jsonListSelector = "";  // used to select releases or wants

        if (listtype == "collection") {
            jsonListSelector = "releases";
        } else {
            jsonListSelector = "wants";
        }


        if (debug) {  Toast.makeText(this,"List Type: " + listtype,Toast.LENGTH_LONG).show(); }


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


                JSONArray labels = basicinfo.getJSONArray("labels");
                JSONObject label1 = labels.getJSONObject(0);

                item_album = label1.getString("name");

                JSONArray artist = basicinfo.getJSONArray("artists");
                JSONObject artist1 = artist.getJSONObject(0);

                item_artist = artist1.getString("name");

                // create query string
                String queryStrValues =
                        "'" + username + "', " +
                                "'" + item_itemurl  + "', " +
                                "'" + item_imageurl + "', " +
                                "'" + item_barcode + "', " +
                                "'" + item_shortdescription + "', " +
                                "'" + item_whichlist + "', " +
                                "'" + item_artist + "', " +
                                "'" + item_album + "', " +
                                "'" + item_albumYear + "', " +
                                "'" + item_catalognumber + "'";


                if (debug) { Toast.makeText(this, "Query String = " + queryStrValues, Toast.LENGTH_LONG).show(); }

                // insert data into user table
                discogetDB.execSQL("INSERT INTO items (owner, itemurl, imageurl, barcode, shortdescription, whichlist, artist, album, albumyear, catalogid )" +
                        " VALUES (" + queryStrValues + ")");

            }   // end of release loop...

            //close DB
            // dbHelper.close();

        } catch (JSONException e) {
            e.printStackTrace();

            if (debug) { Toast.makeText(this,"collection list - JSON Error...",Toast.LENGTH_LONG).show(); }
        }
    }



    private void saveUserCollection(String username, String userToken) {


        String sawerdeman55 = "{\"pagination\": {\"per_page\": 50, \"items\": 2, \"page\": 1, \"urls\": {}, \"pages\": 1}, \"releases\": [{\"collection_id\":" +
                " 180036148, \"release_id\": 1255951, \"rating\": 0, \"basic_information\": {\"labels\": [{\"name\": \"Capitol Records\", \"entity_type\": \"1\", " +
                "\"catno\": \"ST 2080\", \"resource_url\": \"https://api.discogs.com/labels/654\", \"id\": 654, \"entity_type_name\": \"Label\"}], \"formats\":" +
                " [{\"descriptions\": [\"LP\", \"Album\", \"Stereo\"], \"name\": \"Vinyl\", \"qty\": \"1\"}], \"thumb\":" +
                " \"https://api-img.discogs.com/-MlXhLdgQm1VYztTZQkEEs9Q-IM=/fit-in/150x150/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-1255951-1243961962.jpeg.jpg\", " +
                "\"title\": \"The Beatles' Second Album\", \"artists\": [{\"join\": \",\", \"name\": \"The Beatles\", \"anv\": \"\", \"tracks\": \"\", \"role\": " +
                "\"\", \"resource_url\": \"https://api.discogs.com/artists/82730\", \"id\": 82730}], \"resource_url\": \"https://api.discogs.com/releases/1255951\"," +
                " \"year\": 1964, \"id\": 1255951}, \"date_added\": \"2016-07-16T20:03:58-07:00\"}, {\"collection_id\": 179748213, \"release_id\": 4953644, \"rating\":" +
                " 0, \"basic_information\": {\"labels\": [{\"name\": \"Parlophone\", \"entity_type\": \"1\", \"catno\": \"PMC 1230\", \"resource_url\":" +
                " \"https://api.discogs.com/labels/2294\", \"id\": 2294, \"entity_type_name\": \"Label\"}], \"formats\": [{\"descriptions\": [\"LP\", \"Album\", " +
                "\"Mono\"], \"text\": \"Ernest J. Day sleeve\", \"name\": \"Vinyl\", \"qty\": \"1\"}], \"thumb\": " +
                "\"https://api-img.discogs.com/-6p3iHjKnKRGC5S69ZqHXzwOWvw=/fit-in/150x150/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-4953644-1391695856-1514.jpeg.jpg\", " +
                "\"title\": \"A Hard Day's Night\", \"artists\": [{\"join\": \"\", \"name\": \"The Beatles\", \"anv\": \"\", " +
                "\"tracks\": \"\", \"role\": \"\", \"resource_url\": \"https://api.discogs.com/artists/82730\", \"id\": 82730}], " +
                "\"resource_url\": \"https://api.discogs.com/releases/4953644\", \"year\": 1964, \"id\": 4953644}, \"date_added\": \"2016-07-14T13:03:29-07:00\"}]}";



        String whichListType = "Collection";

        String colletionJSONString = sawerdeman55; // assign value to parse here...


        if (debug) {Toast.makeText(this,"collection list",Toast.LENGTH_LONG).show();}


        JSONObject collectionList;

      //  discogetDB = dbHelper.getWritableDatabase();

        try {

            /* basic layout of  Collections
                // TODO need to add layour texxt here...
             */


            collectionList = new JSONObject(colletionJSONString);

            // get page ingo

            JSONObject page = collectionList.getJSONObject("pagination");

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
            JSONArray releases = collectionList.getJSONArray("releases");

            // set release array length
            String arrayLength = Integer.toString(releases.length());


            // loop thru releases (for this page...  // TODO need to process multiple pages...

            /* discogetDB.execSQL("CREATE TABLE IF NOT EXISTS items " +
                    "(id integer primary key autoincrement , owner VARCHAR, itemurl VARCHAR," +
                    "imageurl VARCHAR, barcode VARCHAR, shortdescription VARCHAR," +
                    "whichlist VARCHAR, artist VARCHAR, album VARCHAR, albumyear VARCHAR," +
                    "catalognumber VARCHAR, deleteflag VARCHAR);");

                */

            for (int i=0;i < releases.length(); i++) {  // loop through array

                // get current release array object
                JSONObject item = releases.getJSONObject(i);

                // break it down... Basic INFO
                JSONObject basicinfo = item.getJSONObject("basic_information");

                item_itemurl = basicinfo.getString("resource_url");
                item_imageurl = basicinfo.getString("thumb");
                item_albumYear =  basicinfo.getString("year");


                JSONArray labels = basicinfo.getJSONArray("labels");
                JSONObject label1 = labels.getJSONObject(0);

                item_album = label1.getString("name");

                JSONArray artist = basicinfo.getJSONArray("artists");
                JSONObject artist1 = artist.getJSONObject(0);

                item_artist = artist1.getString("name");


                // add item to database...

                // create SQL string
                // Items -- store all items for user and friends
                /*  discogetDB.execSQL("CREATE TABLE IF NOT EXISTS items " +
                        "(id integer primary key autoincrement , ownerid VARCHAR, itemurl VARCHAR," +
                        "imageurl VARCHAR, barcode VARCHAR, shortdescription VARCHAR," +
                        "whichlist VARCHAR, artist VARCHAR, album VARCHAR, albumyear VARCHAR," +
                        "catalognumber VARCHAR, deleteflag VARCHAR);");
                */

                // create query string
                String queryStrValues =
                        "'" + username + "', " +
                                "'" + item_itemurl  + "', " +
                                "'" + item_imageurl + "', " +
                                "'" + item_barcode + "', " +
                                "'" + item_shortdescription + "', " +
                                "'" + item_whichlist + "', " +
                                "'" + item_artist + "', " +
                                "'" + item_album + "', " +
                                "'" + item_albumYear + "', " +
                                "'" + item_catalognumber + "'";


                if (debug) {   Toast.makeText(this, "Query String = " + queryStrValues, Toast.LENGTH_LONG).show();}
                    // insert data into user table
                    discogetDB.execSQL("INSERT INTO items (owner, itemurl, imageurl, barcode, shortdescription, whichlist, artist, album, albumyear, catalogid )" +
                            " VALUES (" + queryStrValues + ")");





            }   // end of release loop...

            //close DB
           // dbHelper.close();


        } catch (JSONException e) {
            e.printStackTrace();

            Toast.makeText(this,"collection list - JSON Error...",Toast.LENGTH_LONG).show();


        }



    }


    private void saveUserWants(String username) {

        String sawerdeman55 = "{\"wants\": [{\"rating\": 0, \"basic_information\": {\"labels\": [{\"name\": \"Parlophone\"," +
                "\"entity_type\": \"1\", \"catno\": \"PMC 1255\", \"resource_url\": \"https://api.discogs.com/labels/2294\", \"id\": 2294," +
                " \"entity_type_name\": \"Label\"}], \"formats\": [{\"descriptions\": [\"LP\", \"Album\", \"Mono\"], \"name\": \"Vinyl\"," +
                " \"qty\": \"1\"}], \"thumb\": " +
                "\"https://api-img.discogs.com/3xWxlNlKCSpisMdSuUr5AiSjK5k=/fit-in/150x150/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-735564-1391696740-8760.jpeg.jpg\", " +
                "\"title\": \"Help!\", \"artists\": [{\"join\": \",\", \"name\": \"The Beatles\", \"anv\": \"\", \"tracks\": \"\", \"role\": \"\", " +
                "\"resource_url\": \"https://api.discogs.com/artists/82730\", \"id\": 82730}], \"resource_url\": \"https://api.discogs.com/releases/735564\"," +
                " \"year\": 1965, \"id\": 735564}, \"notes\": \"\", \"date_added\": \"2016-07-14T13:05:15-07:00\", \"resource_url\":" +
                " \"https://api.discogs.com/users/sawerdeman55/wants/735564\", \"id\": 735564}], \"pagination\": {\"per_page\": 50, \"items\": 1, \"page\": 1, " +
                "\"urls\": {}, \"pages\": 1}}";

        String whichListType = "Want-list";

        String wantlistJSONString = sawerdeman55; // assign value to parse here...



        JSONObject collectionList;

        discogetDB = dbHelper.getWritableDatabase();

        try {

            /* basic layout of  Collections
                // TODO need to add layour texxt here...
             */


            collectionList = new JSONObject(wantlistJSONString);

            // get page ingo

            JSONObject page = collectionList.getJSONObject("pagination");

            // set page info
            String numOfItems = page.getString("items");
            String numOFPages = page.getString("pages");
            String nextPage   = page.getString("nextpage");   // TODO need to verify...

            String item_owner = username;        // username
            String item_discogsitemurl = "";     // release-basicinfo = "resource_url"
            String item_imageurl = "";           // release-basicinfo = "thumb"
            String item_barcode = "";            // ?
            String item_shortdescription = "";   // ?
            String item_whichlist = whichListType;  // Collections
            String item_artist = "";             // release-basicinfo-artist = "name"
            String item_album = "";              // release-basicinfo-labels = "name"
            String item_albumYear = "";               // release-basicinfo = "year"
            String item_catalognumber = "";      // ?



            // get releases array
            JSONArray releases = collectionList.getJSONArray("releases");

            // set release array length
            String arrayLength = Integer.toString(releases.length());


            // loop thru releases (for this page...  // TODO need to process multiple pages...

            for (int i=0;i < releases.length(); i++) {  // loop through array

                // get current release array object
                JSONObject item = releases.getJSONObject(i);

                // break it down... Basic INFO
                    JSONObject basicinfo = item.getJSONObject("basic_information");

                    item_discogsitemurl = basicinfo.getString("resource_url");
                    item_imageurl = basicinfo.getString("thumb");
                    item_albumYear =  basicinfo.getString("year");


                    JSONArray labels = basicinfo.getJSONArray("labels");
                    JSONObject label1 = labels.getJSONObject(0);

                    item_album = label1.getString("name");

                    JSONArray artist = basicinfo.getJSONArray("artists");
                    JSONObject artist1 = artist.getJSONObject(0);

                    item_artist = artist1.getString("name");


                // add item to database...

                // create SQL string
                // Items -- store all items for user and friends
                /*  discogetDB.execSQL("CREATE TABLE IF NOT EXISTS items " +
                        "(id integer primary key autoincrement , owner VARCHAR, discogsitemurl VARCHAR," +
                        "imageurl VARCHAR, barcode VARCHAR, shortdescription VARCHAR," +
                        "whichlist VARCHAR, artist VARCHAR, album VARCHAR, year VARCHAR," +
                        "catalognumber VARCHAR, deleteflag VARCHAR);");
                */

                // create query string
                String queryStrValues =
                        "'" + username + "', " +
                        "'" + item_discogsitemurl  + "', " +
                        "'" + item_imageurl + "', " +
                        "'" + item_barcode + "', " +
                        "'" + item_shortdescription + "', " +
                        "'" + item_whichlist + "', " +
                        "'" + item_artist + "', " +
                        "'" + item_album + "', " +
                        "'" + item_albumYear + "', " +
                        "'" + item_catalognumber + "'";


                if (i == 0 ) {
                    if (debug) { Toast.makeText(this, "Query String = " + queryStrValues, Toast.LENGTH_LONG).show();}
                    // insert data into user table
                    discogetDB.execSQL("INSERT INTO items (owner, discogsitemurl, imageurl, barcode, shortdescription, whichlist, artist, album, year, catalognumber )" +
                            " VALUES (" + queryStrValues + ")");

                }
                // TODO add other databases later...


            }   // end of release loop...

            //close DB
            dbHelper.close();


        } catch (JSONException e) {
            e.printStackTrace();
        }



    } // end of getUserCollection...



    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private TextView message = (TextView) findViewById(R.id.tv_createAccount_message);

        @Override
        protected String doInBackground(String... urls) {

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


        @Override
        protected void onPreExecute() {
           // message.setText("Creating your account...");
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



} // end...
