package com.discoget.test.discoget_core;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;

import java.util.ArrayList;

/**
 * Created by Steven on 8/19/2016.
 */
public class Friends extends AppCompatActivity{
    private int msgCounter = 0;


    // build database componets
    SQLiteDatabase discogetDB;
    MySQLiteHelper dbHelper;


    String username = "";
    String password = "";
    //String listType = "";

    String  itemSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);

        // create dbhelper
        dbHelper = new MySQLiteHelper(this);


        // get Extra info passed from calling screen
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            username =(String) b.get("username");
            password = (String) b.get("password");
            //listType =(String) b.get("listType");

        }

        // add toolbar... --------------------------------------------------------------
        String paneTitle = "Friends";

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle(paneTitle);
        getSupportActionBar().setIcon(R.drawable.ic_menu);

        my_toolbar.findViewById(R.id.my_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                PopupMenu popup = new PopupMenu(Friends.this, v);

                // This activity implements OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    public boolean onMenuItemClick(MenuItem item) {

                        final int result = 1;
                        Intent goToNextScreen;
                        finish();
                        switch (item.getItemId()) {
                            case R.id.menu_profile:
                                goToNextScreen = new Intent (Friends.this,UserProfile.class);
                                goToNextScreen.putExtra("username",username);
                                goToNextScreen.putExtra("password",password);
                                // goToNextScreen.putExtra("listType","Collection");

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_search:
                                goToNextScreen = new Intent (Friends.this,SearchActivity.class);
                                goToNextScreen.putExtra("username",username);
                                goToNextScreen.putExtra("password",password);
                                // goToNextScreen.putExtra("listType","Collection");

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
                                goToNextScreen = new Intent (Friends.this,Friends.class);
                                goToNextScreen.putExtra("username",username);
                                goToNextScreen.putExtra("password",password);
                                // goToNextScreen.putExtra("listType","Collection");

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_logout:
                                finish();
                                goToNextScreen = new Intent (Friends.this,AccountAccess.class);
                                startActivity(goToNextScreen);

                                return true;

                            default:

                                 goToNextScreen = new Intent (Friends.this,UserProfile.class);
                                 startActivity(goToNextScreen);

                                return false;
                        }

                    }
                });

                popup.inflate(R.menu.actions);
                popup.show();
            }
        });


    //---------- end of popup ---------------------------------------------------------------


       /* ArrayList<FriendsItems> arrayOfItems = new ArrayList<FriendsItems>();
        final FriendsListAdapter adapter = new FriendsListAdapter(this, arrayOfItems);

        // String array for menu items
        FriendsItems myListOfFriends; // =  {"Enrique", "Gursewak", "Steve", "Friend1", "Friend2"};;
        String [] userID = {username,password};
        // get list of friends...

        myListOfFriends = getListOfFriends();

        // create list adapter
        ListAdapter theAdapter = new MyAdapter(this,myListOfFriends,userID);
        */



        ArrayList<FriendsItems> arrayOfItems = new ArrayList<FriendsItems>();
        final FriendsListAdapter adapter = new FriendsListAdapter(this, arrayOfItems);

        readFriendsFromDB(adapter);

        // get list view in xml screen
        ListView myFriendsListView = (ListView) findViewById(R.id.my_list_of_friends);
        myFriendsListView.setAdapter(adapter);

        myFriendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String friendsUserName = adapter.getItem(position).friendUserName;

               // Toast.makeText(Friends.this," ??? " + adapter.getItem(position).friendUserName, Toast.LENGTH_SHORT).show();

                Intent goToNextScreen = new Intent (Friends.this, EditFriendsProflie.class);
                goToNextScreen.putExtra("friendsUserName",friendsUserName);
                startActivity(goToNextScreen);
            }
        });



    }



    public void readFriendsFromDB(FriendsListAdapter adapter) {
        String friendUserName = "";
        String friendFullName = "";
        String friendImageURL = "";



        FriendsItems myFriend;



        // get database
        discogetDB = dbHelper.getWritableDatabase();

       // get start of data stream
        Cursor resultSet = discogetDB.rawQuery("SELECT uid, fullname, imageURL FROM user WHERE usertype = 'friend'", null);
        resultSet.moveToFirst();

        // loop through database...
        while (!resultSet.isAfterLast()) {

            friendUserName = resultSet.getString(0);
            friendFullName = resultSet.getString(1);
            friendImageURL = resultSet.getString(2);

           // Toast.makeText(this,"Friend found: "+ resultSet.getString(0),Toast.LENGTH_SHORT).show();

            // add friend to array
            myFriend = new FriendsItems(friendUserName, friendFullName, friendImageURL);
            adapter.add(myFriend);


            // go to next record
            resultSet.moveToNext();
        } // end of whileloop...


        // closeDB
        dbHelper.close();
     }

    private String[] getListOfFriends() {

        String[] myFriends = {"djens", "mrSangha", "sawerdeman55"};

        SQLiteDatabase discogetDB;
        MySQLiteHelper dbHelper;

        dbHelper = new MySQLiteHelper(this);

        // add data to text fields...
        discogetDB = dbHelper.getWritableDatabase();

            // get user data...
        /*
        Cursor resultSet = mydatbase.rawQuery("Select * from TutorialsPoint",null);
        resultSet.moveToFirst();
        String username = resultSet.getString(1);
        String password = resultSet.getString(2);
         */
            Cursor resultSet = discogetDB.rawQuery("SELECT uid, fullname, imageURL FROM user WHERE usertype = 'friend'", null);
            resultSet.moveToFirst();

        // loop through database...
        while (!resultSet.isAfterLast()) {

            Toast.makeText(this,"Friend found: "+ resultSet.getString(0),Toast.LENGTH_SHORT).show();

            // get files

            // add to array
            //itemURL = "http://1.bp.blogspot.com/-7k2Jnvoaigw/T9kzBww-rXI/AAAAAAAAC3M/c0xk-sgU7wM/s1600/The+Beatles+-+Beatles+for+Sale.jpg";
            //newItem = new CollectionItems("Beatles", "Columbia", "1962", itemURL);
            //newItem = new CollectionItems(itemArtist, itemAlbumLabel, itemAlbumYear, itemAlbumCoverURL);
            //adapter.add(newItem);


            // go to next record
            resultSet.moveToNext();
        } // end of whileloop...


        // closeDB
        dbHelper.close();

        return myFriends;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // Do not iconify the widget; expand it by default
        searchView.setIconifiedByDefault(false);

        //searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //TextView searched = (TextView) findViewById(R.id.searchbar_query);
                //searched.setText(searchView.getQuery());

                Intent goToNextScreen = new Intent(Friends.this, DisplayFriendsSearchResults.class);

                goToNextScreen.putExtra("username", "test"); //username.getText().toString());
                goToNextScreen.putExtra("password",  "test"); //password.getText().toString());
                goToNextScreen.putExtra("searchType", "Friends");
                goToNextScreen.putExtra("searchValue", query);

                final int a_result = 1;
                startActivity(goToNextScreen);
                //Toast.makeText(SearchActivity.this,"search text: " + query ,Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });


        return true;
    }



    public void goHome(View view) {

        //TODO need to finish
        String toastString = "go home...";
        Toast.makeText(Friends.this,toastString, Toast.LENGTH_SHORT).show();

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent (this, AccountAccess.class);
        final int result = 1;
        startActivity(goToNextScreen);
    }


    public void num_pressed(View view) {
        String a = (String) view.getTag(); // here will be 8 for button.

       // Toast.makeText(Friends.this,a, Toast.LENGTH_SHORT).show();
    }

    public void goCollectionBtnCall(View view) {

        //String a = (String) view.getTag(); // here will be 8 for button.

        //TODO need to finish
        String pressed = (String) (view).getTag(); //should do the job.

        String toastString = "go Collection..."+ pressed;

        //Toast.makeText(Friends.this,toastString, Toast.LENGTH_SHORT).show();

        //TODO
        // go to list screen....

       //Button theBtn = (Button) findViewById(R.id.tag_collection);

        Intent goToNextScreen = new Intent (this, WantList.class);
        goToNextScreen.putExtra("username",username);
        goToNextScreen.putExtra("password",password);
        goToNextScreen.putExtra("friendsUserName",pressed);
        goToNextScreen.putExtra("listType","collection");

        final int result = 1;

        startActivity(goToNextScreen);

    }

    public void goWantListBtnCall(View view) {
        //TODO need to finish


        String pressed= (String) (view).getTag(); //should do the job.

        String toastString = "go Want-List..."+ pressed;

        //Toast.makeText(Friends.this,toastString, Toast.LENGTH_SHORT).show();

        //TODO
        Intent goToNextScreen = new Intent (this, WantList.class);
        goToNextScreen.putExtra("username",username);
        goToNextScreen.putExtra("password",password);
        goToNextScreen.putExtra("friendsUserName",pressed);
        goToNextScreen.putExtra("listType","want-list");

        final int result = 1;

        startActivity(goToNextScreen);

        // go to list screen....
        /*Intent goToNextScreen = new Intent (this, ListActivity.class);
        final int result = 1;
        startActivity(goToNextScreen);
        */
    }

    public void goDeleteFriend(View view) {
        //TODO need to finish

        String pressed= (String) (view).getTag(); //should do the job.
        String friendToDelete = pressed;

        //TODO  -- now safty here to prevent accidential deletions...


        // delete friend
        // get database
        discogetDB = dbHelper.getWritableDatabase();

        // get start of data stream
        discogetDB.execSQL("DELETE FROM user WHERE uid = '"+ friendToDelete + "'"); //, fullname, imageURL FROM user WHERE usertype = 'friend'", null);

        // closeDB
        dbHelper.close();

        // goback to here to refresh screen...


        Intent goToNextScreen = new Intent (this, Friends.class);
        // put username as pass to screenqaz!1234

        startActivity(goToNextScreen);


    }


    public void goEditFriend(View view) {

        // get friends to edits username
        String pressed = (String) (view).getTag(); //should do the job.

        Intent goToNextScreen = new Intent (this, EditFriendsProflie.class);
        goToNextScreen.putExtra("friendsusername",pressed);
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
