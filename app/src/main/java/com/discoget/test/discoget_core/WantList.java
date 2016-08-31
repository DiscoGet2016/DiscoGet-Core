package com.discoget.test.discoget_core;


import android.content.Intent;
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
public class WantList extends AppCompatActivity{

    String username = "";
    String password = "";
    String listType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_list_view2);


        // get Extra info passed from calling screen
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            username =(String) b.get("username");
            password = (String) b.get("password");
            listType =(String) b.get("listType");

        }


        //TextView tvUsername = (TextView) findViewById(R.id.txt_wantlist_username);
        //TextView tvListtype = (TextView) findViewById(R.id.txt_wantlist_listtype);

        //tvUsername.setText(username+"'s");
        //tvListtype.setText(listtype);



        // add toolbar... --------------------------------------------------------------

        String paneTitle = username+"'s" + " " + listType;
        //String paneTitle = "My collections";

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle(paneTitle);
        getSupportActionBar().setIcon(R.drawable.ic_menu);

        my_toolbar.findViewById(R.id.my_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                PopupMenu popup = new PopupMenu(WantList.this, v);

                // This activity implements OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    public boolean onMenuItemClick(MenuItem item) {

                        final int result = 1;
                        Intent goToNextScreen;

                        switch (item.getItemId()) {
                            case R.id.menu_profile:
                                goToNextScreen = new Intent (WantList.this,UserProfile.class);
                                goToNextScreen.putExtra("username",username);
                                goToNextScreen.putExtra("password",password);

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_search:
                                goToNextScreen = new Intent (WantList.this,SearchActivity.class);
                                goToNextScreen.putExtra("username",username);
                                goToNextScreen.putExtra("password",password);

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_collection:
                                goToNextScreen = new Intent (WantList.this,WantList.class);
                                goToNextScreen.putExtra("username",username);
                                goToNextScreen.putExtra("password",password);
                                goToNextScreen.putExtra("listType","Collection");

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_wantlist:
                                goToNextScreen = new Intent (WantList.this,WantList.class);
                                goToNextScreen.putExtra("username",username);
                                goToNextScreen.putExtra("password",password);
                                goToNextScreen.putExtra("listType","Want-List");
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_friends:
                                goToNextScreen = new Intent (WantList.this,Friends.class);
                                goToNextScreen.putExtra("username",username);
                                goToNextScreen.putExtra("password",password);
                                //goToNextScreen.putExtra("listType","Collection");

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_logout:
                                goToNextScreen = new Intent (WantList.this,AccountAccess.class);
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_exit:
                                //goToNextScreen = new Intent (WantList.this,Friends.class);
                                //startActivity(goToNextScreen);

                                finish();

                                return true;

                            default:

                       /* Toast.makeText(WantList.this,
                                "Clicked popup menu item " + item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                       */
                                goToNextScreen = new Intent (WantList.this,ActivityHome.class);
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

        String itemURL = "";
        // String array for menu items
        //SAW//String [] myListOfItems =  {"Beatles", "Madonna", "ABBA", "Prince", "R&B", "Rock"};;
        // Construct the data source
        ArrayList<CollectionItems> arrayOfItems = new ArrayList<CollectionItems>();
        // create list adapter


        //SAW//ListAdapter theAdapter = new MyAdapter2(this,myListOfItems);
        // Create the adapter to convert the array to views
        final CollectionListAdapter adapter = new CollectionListAdapter(this, arrayOfItems);

        // Add item to adapter
        CollectionItems newItem;

        if ( listType.equals("Collection")) {
            itemURL = "http://1.bp.blogspot.com/-7k2Jnvoaigw/T9kzBww-rXI/AAAAAAAAC3M/c0xk-sgU7wM/s1600/The+Beatles+-+Beatles+for+Sale.jpg";
            newItem = new CollectionItems("Beatles", "Columbia", "1962", itemURL);
            adapter.add(newItem);

            itemURL = "https://s.yimg.com/fz/api/res/1.2/7GzBaZrJQJMNhFfKJmMY8A--/YXBwaWQ9c3JjaGRkO2g9NTAwO3E9OTU7dz01MDA-/http://www.amiright.com/album-covers/images/album-Kiss-Destroyer.jpg";
            newItem = new CollectionItems("ABBA", "Apple Records", "1968", itemURL);
            adapter.add(newItem);

        }

        if ( listType.equals("Want-List")) {
            itemURL = "http://cps-static.rovicorp.com/3/JPG_400/MI0002/285/MI0002285831.jpg?partner=allrovi.com";
            newItem = new CollectionItems("R&B", "Alantic Records", "1972", itemURL);
            adapter.add(newItem);
        }

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
                goToNextScreen = new Intent (WantList.this,ItemScreen.class);
                goToNextScreen.putExtra("artist",adapter.getItem(menuSelected).itemArtist);
                goToNextScreen.putExtra("label",adapter.getItem(menuSelected).itemLabel);
                goToNextScreen.putExtra("year",adapter.getItem(menuSelected).itemYear);
                goToNextScreen.putExtra("URL",adapter.getItem(menuSelected).itemCoverURL);
                startActivity(goToNextScreen);

               // Toast.makeText(WantList.this,menuItemSelected, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void goHome(View view) {

        //TODO need to finish
        String toastString = "go home...";
        Toast.makeText(WantList.this,toastString, Toast.LENGTH_SHORT).show();

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent (this, AccountAccess.class);
        final int result = 1;
        startActivity(goToNextScreen);
    }


}



