package com.discoget.test.discoget_core;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

/**
 * Created by Steven on 8/30/2016.
 */
public class ItemScreen extends AppCompatActivity {

// How to Load Image From URL (Internet) in Android ImageView

    String username = "";
    String password = "";

    String searchType = "";
    String itemArtist = "";
    String itemLabel = "";
    String itemYear = "";
    String itemURL = "";
    String itemReleaseID = "";
    String itemList = "";
    String itemTitle = "";

    String tracksStr[] = {"TRack 1   3:09","TRack 2   3:09","TRack 3   3:09","TRack 4   3:09","TRack 5   3:09","TRack 6   3:09" };

    private String discogsJSONString;
    private boolean debug = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_screen);
       //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
       // getSupportActionBar().setCustomView(R.layout.abs_layout);

        // add tool bar
        //ToolBar...
        String paneTitle = "";

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle(paneTitle);
        //getSupportActionBar().setIcon(R.drawable.back_30x);

        my_toolbar.findViewById(R.id.my_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Intent goToNextScreen = new Intent(AccountCreation.this, AccountAccess.class);
                // startActivity(goToNextScreen);
            }
        });

        // end of tool bar

        TextView tvRowOne = (TextView) findViewById(R.id.tv_item_artist);
        TextView tvRowTwo = (TextView) findViewById(R.id.tv_item_label);
        TextView tvRowThree = (TextView) findViewById(R.id.tv_item_year);

        Button btnCollection = (Button) findViewById(R.id.btn_itemscreen_collection);
        Button btnWantList = (Button) findViewById(R.id.btn_itemscreen_wantlist);

        ImageView ivAlbumCover = (ImageView) findViewById(R.id.iv_item_cover);

        ListView tracksListView = (ListView) findViewById(R.id.trackslistView);


        // get extras passed from calling page...
        Intent iin = getIntent();
        Bundle b = iin.getExtras();


        if (b != null) {
            // username =(String) b.get("username");
            // password =(String) b.get("password");
            itemArtist = (String) b.get("artist");
            itemLabel = (String) b.get("label");
            itemYear = (String) b.get("year");
            itemURL = (String) b.get("URL");
            itemReleaseID = (String) b.get("release_id");
            itemList = (String) b.get("listtype");
            itemTitle  = (String) b.get("albumtitle");
            searchType = (String) b.get("searchType");



            //getSupportActionBar().setTitle(itemTitle);   // put title in menu bar...
            TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
            toolbarTitle.setText(itemTitle);



            if ((!(searchType == null)) && searchType.equals("title")) {

                    tvRowOne.setText(itemTitle);    // title
                    tvRowTwo.setText(itemLabel);    // label
                    tvRowThree.setText(itemYear + "  [" + itemReleaseID + "]");     // year  [release_id]


            } else {

                tvRowOne.setText(itemArtist);
                tvRowTwo.setText(itemLabel);
                tvRowThree.setText(itemYear + "  [" + itemReleaseID + "]");
            }



            btnCollection.setTag(itemReleaseID);
            btnWantList.setTag(itemReleaseID);





            // set status of buttons...
            if (itemList.toLowerCase().equals("collection")) {
                btnCollection.setText("In Collection");
                btnCollection.setClickable(false);
                btnCollection.getBackground().setColorFilter(Color.parseColor("#b3e6ff"), PorterDuff.Mode.DARKEN);   // was ffff66
            }

            if (itemList.toLowerCase().equals("want-list")) {
                btnWantList.setText("In Want-List");
                btnWantList.setClickable(false);
                btnWantList.getBackground().setColorFilter(Color.parseColor("#b3e6ff"), PorterDuff.Mode.DARKEN);   // was ffff66
             }


            new ImageLoadTask(itemURL, ivAlbumCover).execute();


            // -- Get and Display Tracks info...
            ArrayList<Tracks> listOfTracks = new ArrayList<Tracks>();
            TracksAdapter trackAdapter = new TracksAdapter(this, listOfTracks);
            // add items to track

            getItemTracks(trackAdapter, itemReleaseID);

           /* Tracks newTrack;
            for (int i = 1; i < 16; i++) {
                newTrack = new Tracks (Integer.toString(i), " Track Title" , "time");
                trackAdapter.add(newTrack);
            }

            */

            ListView trackListView = (ListView)findViewById(R.id.trackslistView);
            trackListView.setAdapter(trackAdapter);


            trackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final String item = (String) adapterView.getItemAtPosition(i);
                    Log.e("Error", "-----------------------------"+item);
                    Toast.makeText(getApplicationContext(),"Position "+item, Toast.LENGTH_LONG).show();
                }
            });


        }


    }




    private void getItemTracks(TracksAdapter trackAdapter, String releaseid) {

         // Tracks newTrack;
         /*   for (int i = 1; i < 16; i++) {
                newTrack = new Tracks ("Track" + Integer.toString(i), " Track Title" , "time");
                trackAdapter.add(newTrack);
            }
         */

          if(isConnected()){
            // tvIsConnected.setBackgroundColor(0xFF00CC00);
            // tvIsConnected.setText("You are connected");

            // String to call...
            String trackURL = "https://api.discogs.com/releases/" + releaseid; // "213199";  // TODO token???

            // call AsynTask to perform network operation on separate thread
            String jsonTestString;



            try {
                //Toast.makeText(this,"Sync Started: Please wait",Toast.LENGTH_LONG).show();

                jsonTestString = new HttpAsyncTask().execute(trackURL).get();
                splitJSONString(trackAdapter, releaseid, jsonTestString);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            // TODO need an edit option ... of what???

            //close DB


            // goto next screen...
          /*
            finish();  // close this activity

            Intent goToNextScreen = new Intent (this, AccountAccess.class);
            // put username as pass to screen

            startActivity(goToNextScreen);

           // Toast.makeText(this,discogsJSONString,Toast.LENGTH_LONG).show();
           */
          }  else{
            // tvIsConnected.setText("You are NOT connected");
            /*
                Toast.makeText(this,"Connection not available",Toast.LENGTH_LONG).show();
            */
          }


    } // end build tracks...



    private void splitJSONString(TracksAdapter trackAdapter, String releaseid, String jsonData) {


        // parse Release Data to get tracks..
        String userListToProcess = jsonData;


        JSONObject releaseItem;   // generic name...

        try {

            String track_position = "A";
            String track_title = "no Title found";
            String track_time = "time";

            // releaseItem wil be the JSON primary object
            releaseItem = new JSONObject(userListToProcess);

            // array of tracks objects...
            JSONArray listOfTRacks = releaseItem.getJSONArray("tracklist");

            // set release array length
            String arrayLength = Integer.toString(listOfTRacks.length());


            // loop thru releases (for this page...  // TODO need to process multiple pages...
            Tracks newTrack;


            int count = 0;
            for (int i=0;i < listOfTRacks.length(); i++) {  // loop through array

                // get current release array object
                JSONObject track = listOfTRacks.getJSONObject(i);

                //Toast.makeText(I, track, Toast.LENGTH_SHORT).show();

                // break it down...
                if (track.has("position")) {
                    track_position = track.getString("position");
                }

                if (track.has("title")) {
                    track_title = track.getString("title");
                }
                if (track.has("duration")) {
                    track_time = track.getString("duration");
                }

                // add to track to adapter...
                newTrack = new Tracks (track_position, track_title, track_time);
                trackAdapter.add(newTrack);


            }   // end of if...release loop...


            // Toast.makeText(this, "item count = " + Integer.toString(count) , Toast.LENGTH_SHORT).show();
            //close DB
            // dbHelper.close();

        } catch (JSONException e) {
            e.printStackTrace();

            Toast.makeText(this,"Track list - JSON Error...",Toast.LENGTH_LONG).show();
        }
    }







    public void goAddToCollection(View view) {
        // add item to Collection
        Toast.makeText(this, itemArtist + "Added to Collection", Toast.LENGTH_SHORT).show();
        Button thisButton = (Button) findViewById(R.id.btn_itemscreen_collection);
        addThisItemTo("collection",thisButton.getTag().toString());
        //finish();
    }

    private void addThisItemTo(String listType, String s) {
        // add item base on releasee data...
        Toast.makeText(this,"Item " + s + " Added to :" +listType, Toast.LENGTH_SHORT).show();
    }


    public void goAddToWantList(View view) {
        // add item to WantList
        Toast.makeText(this, itemArtist + "Added to Want-List", Toast.LENGTH_SHORT).show();
        Button thisButton = (Button) findViewById(R.id.btn_itemscreen_collection);
        addThisItemTo("collection",thisButton.getTag().toString());
        //finish();


    }




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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
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

