package com.discoget.test.discoget_core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Steven on 8/19/2016.
 */
public class Friends extends AppCompatActivity{
    private int msgCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);


        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle("Friends");
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

                        switch (item.getItemId()) {
                            case R.id.menu_profile:
                                goToNextScreen = new Intent (Friends.this,UserProfile.class);
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_search:
                                goToNextScreen = new Intent (Friends.this,SearchActivity.class);
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_collection:
                                goToNextScreen = new Intent (Friends.this,WantList.class);
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_wantlist:
                                goToNextScreen = new Intent (Friends.this,WantList.class);
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_friends:

                                goToNextScreen = new Intent (Friends.this,Friends.class);
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_logout:
                                goToNextScreen = new Intent (Friends.this,ActivityHome.class);
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_exit:
                                //goToNextScreen = new Intent (Friends.this,Friends.class);
                                //startActivity(goToNextScreen);

                                finish();

                                return true;

                            default:

                       /* Toast.makeText(UserProfile.this,
                                "Clicked popup menu item " + item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                       */
                                goToNextScreen = new Intent (Friends.this,ActivityHome.class);
                                startActivity(goToNextScreen);

                                return false;
                        }

                    }
                });

                popup.inflate(R.menu.actions);
                popup.show();
            }
        });









        // String array for menu items
        String [] myListOfFriends =  {"Enrique", "Gursewak", "Steve", "Friend1", "Friend2"};;

        // create list adapter
        ListAdapter theAdapter = new MyAdapter(this,myListOfFriends);

        // get list view in xml screen
        ListView myFriendsList = (ListView) findViewById(R.id.my_list_of_friends);

        myFriendsList.setAdapter(theAdapter);

        myFriendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int menuSelected, long l) {

                String menuItemSelected = "Menu Selected was" + String.valueOf(adapterView.getItemAtPosition(menuSelected));

                Toast.makeText(Friends.this,menuItemSelected, Toast.LENGTH_SHORT).show();

            }
        });




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

    public void goCollection(View view) {

        //TODO need to finish
        String toastString = "go Collection...";

        Toast.makeText(Friends.this,toastString, Toast.LENGTH_SHORT).show();

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent (this, ListActivity.class);
        final int result = 1;
        startActivity(goToNextScreen);

    }

    public void goWantList(View view) {
        //TODO need to finish
        String toastString = "go Want-List...";

        Toast.makeText(Friends.this,toastString, Toast.LENGTH_SHORT).show();

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent (this, ListActivity.class);
        final int result = 1;
        startActivity(goToNextScreen);
    }

    public void deleteFriend(View view) {
        //TODO need to finish
        String toastString = "delete friend...";

        Toast.makeText(Friends.this,toastString, Toast.LENGTH_SHORT).show();

        //TODO

    }
}
