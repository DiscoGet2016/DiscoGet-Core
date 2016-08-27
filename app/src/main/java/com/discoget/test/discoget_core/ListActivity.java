package com.discoget.test.discoget_core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;

//import static com.discoget.test.outtestproject.R.id.the_list_view;

/**
 * Created by steve on 8/17/2016.
 */
public class ListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_list_view);


       // String array for menu items
        String [] myListOfFriends =  {"Enrique", "Gursewak", "Steve", "Friend1", "Friend2"};;

        // create list adapter
        ListAdapter theAdapter = new MyAdapter(this,myListOfFriends);

        // get list view in xml screen
        ListView theListView = (ListView) findViewById(R.id.list_view);

        theListView.setAdapter(theAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int menuSelected, long l) {

                String menuItemSelected = "Menu Selected was" + String.valueOf(adapterView.getItemAtPosition(menuSelected));

                Toast.makeText(ListActivity.this,menuItemSelected, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void goHome(View view) {

        //TODO need to finish
        String toastString = "go home...";
        Toast.makeText(ListActivity.this,toastString, Toast.LENGTH_SHORT).show();

        //TODO
        // go to list screen....
        Intent goToNextScreen = new Intent (this, AccountAccess.class);
        final int result = 1;
        startActivity(goToNextScreen);
    }


}



