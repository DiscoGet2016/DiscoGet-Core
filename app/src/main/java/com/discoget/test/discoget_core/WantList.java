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
public class WantList extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_list_view2);


        // String array for menu items
        String [] myListOfFriends =  {"Beatles", "Madonna", "ABBA", "Prince", "R&B", "Rock"};;

        // create list adapter
        ListAdapter theAdapter = new MyAdapter2(this,myListOfFriends);

        // get list view in xml screen
        ListView theListView = (ListView) findViewById(R.id.list_view2);

        theListView.setAdapter(theAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int menuSelected, long l) {

                String menuItemSelected = "Menu Selected was" + String.valueOf(adapterView.getItemAtPosition(menuSelected));

                Toast.makeText(WantList.this,menuItemSelected, Toast.LENGTH_SHORT).show();

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



