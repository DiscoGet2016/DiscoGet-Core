package com.discoget.test.discoget_core;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;


/**
 * Created by Steven on 8/26/2016.
 */
public class EditFriendsProflie extends AppCompatActivity {

// How to Load Image From URL (Internet) in Android ImageView

    private SQLiteDatabase discogetDB;
    private MySQLiteHelper dbHelper;



    TextView userName;
    TextView userFullName;
    TextView userBio;

    String username = "";  // used for info strings...
    String password = "";  // not needed...
    String token = "";     // token used to access user info...
    String userImageUrl = "";  // assigned in JSON search for now...  // TODO store value in DB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_profile);

        // create database helper object...
        dbHelper = new MySQLiteHelper(this);


        // get extras passed from calling page...
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        String friendsUserName = "";

        if(b!=null)
        {
            username =(String) b.get("username");
            token =(String) b.get("token");
            friendsUserName = (String) b.get("friendsUserName");
        }


        // for testing...
        // TextView namePassed = (TextView) findViewById(R.id.txt_profile_namePassed);
        // namePassed.setText(username);

        // new code 09/11/16 -- Asumes valid user at this point...
        // verify username in test string list
        /*
            String[] validUsers = {"djens", "mrSangha", "sawerdeman55"};


        if (!(Arrays.asList(validUsers).contains(username))) {
            // Toast.makeText(this, "DiscoGet DB not Found", Toast.LENGTH_SHORT).show();
            Toast.makeText(this,"User not found",Toast.LENGTH_LONG).show();

            //TODO  go back to login...
            finish();
            Intent goToNextScreen = new Intent (UserProfile.this,AccountAccess.class);
            startActivity(goToNextScreen);
        } else {  // do next screen
          */


            // add toolbar...
            String paneTitle = friendsUserName + "'s Profile";

            Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(my_toolbar);

            getSupportActionBar().setTitle(paneTitle);
            getSupportActionBar().setIcon(R.drawable.ic_menu);

            my_toolbar.findViewById(R.id.my_toolbar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(EditFriendsProflie.this, v);

                    // This activity implements OnMenuItemClickListener

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {


                            final int result = 1;
                            Intent goToNextScreen;

                            switch (item.getItemId()) {
                                case R.id.menu_profile:
                                    goToNextScreen = new Intent(EditFriendsProflie.this, EditFriendsProflie.class);
                                    goToNextScreen.putExtra("username", username);
                                    goToNextScreen.putExtra("password", password);

                                    startActivity(goToNextScreen);

                                    return true;
                                case R.id.menu_search:
                                    goToNextScreen = new Intent(EditFriendsProflie.this, SearchActivity.class);
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
                                    goToNextScreen = new Intent(EditFriendsProflie.this, Friends.class);
                                    goToNextScreen.putExtra("username", username);
                                    goToNextScreen.putExtra("password", password);

                                    startActivity(goToNextScreen);

                                    return true;
                                case R.id.menu_logout:
                                    finish();
                                    goToNextScreen = new Intent(EditFriendsProflie.this, AccountAccess.class);
                                    startActivity(goToNextScreen);

                                    return true;

                                default:


                                    goToNextScreen = new Intent(EditFriendsProflie.this, UserProfile.class);
                                    startActivity(goToNextScreen);

                                    return false;
                            }

                        }
                    });

                    popup.inflate(R.menu.actions);
                    popup.show();
                }
            });


        // get data from database
        getProfileDataFromDB (friendsUserName);


        // Image link from internet
        new DownloadImageFromInternet((ImageView) findViewById(R.id.iv_profile_userPhoto))
                   .execute(userImageUrl);
            // .execute(friendsURLArray[3]);
            //.execute("https://pbs.twimg.com/profile_images/630285593268752384/iD1MkFQ0.png");


            // finish();
        //}  // else...


    }


    /*public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.actions, popup.getMenu());
        popup.show();
    }
 */

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener



        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            public boolean onMenuItemClick(MenuItem item) {

                final int result = 1;
                Intent goToNextScreen;
                finish();

                switch (item.getItemId()) {
                    case R.id.menu_profile:
                        goToNextScreen = new Intent (EditFriendsProflie.this,EditFriendsProflie.class);
                        startActivity(goToNextScreen);

                        return true;
                    case R.id.menu_search:
                        goToNextScreen = new Intent (EditFriendsProflie.this,SearchActivity.class);
                        startActivity(goToNextScreen);

                        return true;
                    case R.id.menu_collection:
                        goToNextScreen = new Intent (EditFriendsProflie.this,WantList.class);
                        startActivity(goToNextScreen);

                        return true;
                    case R.id.menu_wantlist:
                        goToNextScreen = new Intent (EditFriendsProflie.this,WantList.class);
                        startActivity(goToNextScreen);

                        return true;
                    case R.id.menu_friends:

                        goToNextScreen = new Intent (EditFriendsProflie.this,Friends.class);
                        startActivity(goToNextScreen);

                        return true;
                    case R.id.menu_logout:
                        finish();
                        goToNextScreen = new Intent (EditFriendsProflie.this,ActivityHome.class);
                        startActivity(goToNextScreen);

                        return true;


                    default:


                       goToNextScreen = new Intent (EditFriendsProflie.this,UserProfile.class);
                       startActivity(goToNextScreen);

                       return false;
                }

            }
        });

        popup.inflate(R.menu.actions);
        popup.show();
    }



    public void goHome(View view) {

        //TODO need to finish
        String toastString = "go home...";
        Toast.makeText(EditFriendsProflie.this, toastString, Toast.LENGTH_SHORT).show();

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent(this, AccountAccess.class);
        final int result = 1;
        startActivity(goToNextScreen);
    }

    public void goCollection(View view) {

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

    public void goWantList(View view) {

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

    public void goSearch(View view) {
        //TODO need to finish
        //String toastString = "go to search screen";
        // Toast.makeText(UserProfile.this, toastString, Toast.LENGTH_SHORT).show();

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent(this, SearchActivity.class);
        final int result = 1;
        startActivity(goToNextScreen);
    }


    private void getProfileDataFromDB (String friendsUserName) {


       // Toast.makeText(this, "friends username: " + friendsUserName,Toast.LENGTH_LONG).show();

        // add data to profile info
        //ImageView userPhoto = (ImageView) findViewById(R.id.img_userPhoto);
        userName = (TextView) findViewById(R.id.txt_profile_userName);
        userFullName = (TextView) findViewById(R.id.txt_profile_userFullName);
        userBio = (TextView) findViewById(R.id.txt_profile_userBio);


        // add data to text fields...
        discogetDB = dbHelper.getWritableDatabase();

        // get user data...
        /*
        Cursor resultSet = mydatbase.rawQuery("Select * from TutorialsPoint",null);
        resultSet.moveToFirst();
        String username = resultSet.getString(1);
        String password = resultSet.getString(2);
         */
        Cursor resultSet = discogetDB.rawQuery("SELECT userid, password, fullname, profile, photourl, discogstoken" +
                " FROM user WHERE uid = '" + friendsUserName + "'", null);
        resultSet.moveToFirst();

        //uid = resultSet.getString(0);
        //pw  = resultSet.getString(1);
        //token  = resultSet.getString(2);

        if (!(resultSet.isNull(0))) {
            userName.setText(checkForText(resultSet.getString(0)));
            userFullName.setText(checkForText(resultSet.getString(2)));
            userBio.setText(checkForText(resultSet.getString(3)));

            userImageUrl = resultSet.getString(4);

        }

    } // end of getProfileDataFromDB


    private void getJSONdata(String username) throws JSONException {

        // Save the results in a String
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

        String sawerdeman55 = "{'username': 'sawerdeman55', 'profile': 'Music Listener/ app developer / avid vinyl record collector, ok really just getting started... lol'," +
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


        JSONObject jObject = new JSONObject();

        if (username.equals("sawerdeman55")) {
            jObject = new JSONObject(sawerdeman55);
        }
        if (username.equals("djens")) {
            jObject = new JSONObject(djens);
        }
        if (username.equals("mrSangha")) {
            jObject = new JSONObject(mrSangha);
        }

        if (username.equals("swerdeman")) {
            jObject = new JSONObject(swerdeman);
        }


        //JSONObject jObject = new JSONObject(sawerdeman55);

        // Create a JSONObject by passing the JSON data
        //JSONObject jObject = new JSONObject();

        // Get the Array named translations that contains all the translations
        // JSONArray jArray = jObject.getJSONArray("");

        // Cycles through every translation in the array
        //outputTranslations(jArray);
        //displayJsonData(jObject);


        // add data to profile info
        //ImageView userPhoto = (ImageView) findViewById(R.id.img_userPhoto);
        userName = (TextView) findViewById(R.id.txt_profile_userName);
        userFullName = (TextView) findViewById(R.id.txt_profile_userFullName);
        userBio = (TextView) findViewById(R.id.txt_profile_userBio);

        //String buildDisplay = "Display of JSON data\n\n";


        //String imageURL = "http://assets.blabbermouth.net.s3.amazonaws.com/media/powerwolfblessedcd.jpg";

        // Image link from internet    //(ImageView) theView.findViewById(R.id.img_friend)
        // new DownloadImageFromInternet(userPhoto)//findViewById(R.id.image_view))
        //       .execute(friendsURLArray[2]); //("https://pbs.twimg.com/profile_images/630285593268752384/iD1MkFQ0.png");

        //set text data from JSON object);

        //if (jObject.optString("username").length() > 0 ) {
        //    userName.setText(jObject.optString("username"));
        //} else

        userName.setText(checkForText(jObject.optString("username")));
        userFullName.setText(checkForText(jObject.optString("name")));
        userBio.setText(checkForText(jObject.optString("profile")));

        userImageUrl = jObject.optString("avatar_url");



        //now go get user image...
        //        new LoadImage().execute(friendsURLArray[2]);


        // buildDisplay += "Profile : " + jsonObject.optString("profile").toString()+"\n";
        // buildDisplay += "AvatarURL : " + jsonObject.optString("avatar_url").toString()+"\n";
        // buildDisplay += "Location: " + jsonObject.optString("location").toString()+"\n";

    }

    private String checkForText(String textValue) {

        String valueToReturnIfBlank = "";


        if (textValue.length() > 0) {
            return textValue;
        } else {
            return valueToReturnIfBlank;
        }
    }


    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            //Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
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

