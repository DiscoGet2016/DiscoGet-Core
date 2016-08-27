package com.discoget.test.discoget_core;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
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
public class UserProfile extends AppCompatActivity {

// How to Load Image From URL (Internet) in Android ImageView

    TextView userName;
    TextView userEmail;
    TextView userBio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        // add toolbar...
        String paneTitle = "User Profile";

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle(paneTitle);
        getSupportActionBar().setIcon(R.drawable.ic_menu);

        my_toolbar.findViewById(R.id.my_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                PopupMenu popup = new PopupMenu(UserProfile.this, v);

                // This activity implements OnMenuItemClickListener



                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    public boolean onMenuItemClick(MenuItem item) {


                        final int result = 1;
                        Intent goToNextScreen;

                        switch (item.getItemId()) {
                            case R.id.menu_profile:
                                goToNextScreen = new Intent (UserProfile.this,UserProfile.class);
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_search:
                                goToNextScreen = new Intent (UserProfile.this,SearchActivity.class);
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_collection:
                                goToNextScreen = new Intent (UserProfile.this,WantList.class);
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_wantlist:
                                goToNextScreen = new Intent (UserProfile.this,WantList.class);
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_friends:

                                goToNextScreen = new Intent (UserProfile.this,Friends.class);
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_logout:
                                goToNextScreen = new Intent (UserProfile.this,ActivityHome.class);
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_exit:
                                //goToNextScreen = new Intent (UserProfile.this,Friends.class);
                                //startActivity(goToNextScreen);

                                finish();

                                return true;

                            default:

                       /* Toast.makeText(UserProfile.this,
                                "Clicked popup menu item " + item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                       */
                                goToNextScreen = new Intent (UserProfile.this,ActivityHome.class);
                                startActivity(goToNextScreen);

                                return false;
                        }

                    }
                });

                popup.inflate(R.menu.actions);
                popup.show();
            }
        });



        // Get image from Internet...
        //------------------------------------------------------------------------------
        String gravatarHome = "https://www.gravatar.com/avatar/";
        String defaultAvatar = "http://www.digitalqatar.qa/wp-content/uploads/sites/2/2011/12/android.jpg";

        // build friendsDataArray
        String[] friendsURLArray = {
                gravatarHome + "d4cb23b09d74f33ef8ad43fc0e0896f9",         // Enrique
                gravatarHome + "baf9b97e84ac714acb14a65e9855538d",         // Guru
                gravatarHome + "956a76b2e3547849f3a62df862865be2",         //Steve
                gravatarHome + "205e460b479e2e5b48aec07710c08d50",         //Test dude
                defaultAvatar,
                defaultAvatar};
        //ImageView theImageView = (ImageView) theView.findViewById(R.id.img_friend);
        // We can set a ImageView like this


        try {
            getJSONdata();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Image link from internet
        new DownloadImageFromInternet((ImageView) findViewById(R.id.image_view))
                .execute(friendsURLArray[4]);
        //.execute("https://pbs.twimg.com/profile_images/630285593268752384/iD1MkFQ0.png");
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

                switch (item.getItemId()) {
                    case R.id.menu_profile:
                        goToNextScreen = new Intent (UserProfile.this,UserProfile.class);
                        startActivity(goToNextScreen);

                        return true;
                    case R.id.menu_search:
                        goToNextScreen = new Intent (UserProfile.this,SearchActivity.class);
                        startActivity(goToNextScreen);

                        return true;
                    case R.id.menu_collection:
                        goToNextScreen = new Intent (UserProfile.this,WantList.class);
                        startActivity(goToNextScreen);

                        return true;
                    case R.id.menu_wantlist:
                        goToNextScreen = new Intent (UserProfile.this,WantList.class);
                        startActivity(goToNextScreen);

                        return true;
                    case R.id.menu_friends:

                        goToNextScreen = new Intent (UserProfile.this,Friends.class);
                        startActivity(goToNextScreen);

                        return true;
                    case R.id.menu_logout:
                        goToNextScreen = new Intent (UserProfile.this,ActivityHome.class);
                        startActivity(goToNextScreen);

                        return true;
                    case R.id.menu_exit:
                        //goToNextScreen = new Intent (UserProfile.this,Friends.class);
                        //startActivity(goToNextScreen);

                        finish();

                        return true;

                    default:

                       /* Toast.makeText(UserProfile.this,
                                "Clicked popup menu item " + item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                       */
                       goToNextScreen = new Intent (UserProfile.this,ActivityHome.class);
                       startActivity(goToNextScreen);

                       return false;
                }

            }
        });

        popup.inflate(R.menu.actions);
        popup.show();
    }


  /* @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.archive:
                archive(item);
                return true;
            case R.id.delete:
                delete(item);
                return true;
            default:
                return false;
        }
    }
*/


    public void goHome(View view) {

        //TODO need to finish
        String toastString = "go home...";
        Toast.makeText(UserProfile.this, toastString, Toast.LENGTH_SHORT).show();

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent(this, AccountAccess.class);
        final int result = 1;
        startActivity(goToNextScreen);
    }

    public void goCollection(View view) {

        //TODO need to finish
        String toastString = "go Collection...";
        Toast.makeText(UserProfile.this, toastString, Toast.LENGTH_SHORT).show();

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent(this, WantList.class);
        final int result = 1;
        //startActivity(goToNextScreen);
    }

    public void goWantList(View view) {
        //TODO need to finish
        String toastString = "go Want-List...";
        Toast.makeText(UserProfile.this, toastString, Toast.LENGTH_SHORT).show();

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent(this, WantList.class);
        final int result = 1;
       // startActivity(goToNextScreen);
    }

    public void goFriends(View view) {
        //TODO need to finish
        String toastString = "go Want-List...";
        Toast.makeText(UserProfile.this, toastString, Toast.LENGTH_SHORT).show();

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent(this, Friends.class);
        final int result = 1;
        startActivity(goToNextScreen);
    }


    private void getJSONdata() throws JSONException {

        // Save the results in a String
        String djens = "{'username': 'djens', 'profile': '', 'num_collection': 242, 'collection_fields_url': 'https://api.discogs.com/users/djens/collection/fields'," +
                " 'releases_contributed': 0, 'rating_avg': 0.0, 'registered': '2007-12-20T15:27:48-08:00', 'wantlist_url': 'https://api.discogs.com/users/djens/wants'," +
                " 'seller_num_ratings': 0, 'rank': 1.0, 'releases_rated': 0, 'buyer_rating': 100.0, 'num_pending': 0, 'seller_rating_stars': 0.0," +
                " 'resource_url': 'https://api.discogs.com/users/djens', 'num_lists': 0, 'name': '', 'num_for_sale': 0, 'buyer_rating_stars': 5.0," +
                " 'home_page': '', 'num_wantlist': 53, 'inventory_url': 'https://api.discogs.com/users/djens/inventory'," +
                " 'uri': 'https://www.discogs.com/user/djens', 'id': 317613, 'buyer_num_ratings': 1," +
                " 'avatar_url': 'https://secure.gravatar.com/avatar/d4cb23b09d74f33ef8ad43fc0e0896f9?s=500&r=pg&d=mm'," +
                " 'location': 'San Diego, CA', 'collection_folders_url': 'https://api.discogs.com/users/djens/collection/folders'," +
                " 'seller_rating': 0.0,'email':'djens@gmail.com'}"; //sb.toString();

        String swerdeman55 = "{'username': 'sawerdeman55', 'profile': 'Music Listener/ app developer / avid vinyl record collector, ok really just getting started... lol'," +
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


        // Create a JSONObject by passing the JSON data
        JSONObject jObject = new JSONObject(mrSangha);

        // Get the Array named translations that contains all the translations
        // JSONArray jArray = jObject.getJSONArray("");

        // Cycles through every translation in the array
        //outputTranslations(jArray);
        //displayJsonData(jObject);


        // add data to profile info
        //ImageView userPhoto = (ImageView) findViewById(R.id.img_userPhoto);
        userName = (TextView) findViewById(R.id.txt_userName2);
        userEmail = (TextView) findViewById(R.id.txt_userEmail2);
        userBio = (TextView) findViewById(R.id.txt_userBio2);

        //String buildDisplay = "Display of JSON data\n\n";


        //String imageURL = "http://assets.blabbermouth.net.s3.amazonaws.com/media/powerwolfblessedcd.jpg";

        // Image link from internet    //(ImageView) theView.findViewById(R.id.img_friend)
        // new DownloadImageFromInternet(userPhoto)//findViewById(R.id.image_view))
        //       .execute(friendsURLArray[2]); //("https://pbs.twimg.com/profile_images/630285593268752384/iD1MkFQ0.png");

        //set text data from JSON object);
        userName.setText(jObject.optString("username"));
        userEmail.setText(jObject.optString("email"));
        userBio.setText(jObject.optString("profile"));


        //now go get user image...
        //        new LoadImage().execute(friendsURLArray[2]);


        // buildDisplay += "Proifle : " + jsonObject.optString("profile").toString()+"\n";
        // buildDisplay += "AvatarURL : " + jsonObject.optString("avatar_url").toString()+"\n";
        // buildDisplay += "Location: " + jsonObject.optString("location").toString()+"\n";

    }


    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
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
}

