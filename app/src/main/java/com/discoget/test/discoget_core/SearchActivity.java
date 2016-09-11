package com.discoget.test.discoget_core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    String username = "";
    String password = "";
    String listType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // get Extra info passed from calling screen
        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            username =(String) b.get("username");
            password = (String) b.get("password");
           // listType =(String) b.get("listType");

        }


        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle(R.string.toolbar_search_title);
        getSupportActionBar().setIcon(R.drawable.ic_menu);

        my_toolbar.findViewById(R.id.my_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                PopupMenu popup = new PopupMenu(SearchActivity.this, v);

                // This activity implements OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    public boolean onMenuItemClick(MenuItem item) {


                        final int result = 1;
                        Intent goToNextScreen;

                        switch (item.getItemId()) {
                            case R.id.menu_profile:
                                goToNextScreen = new Intent (SearchActivity.this,UserProfile.class);
                                goToNextScreen.putExtra("username",username);
                                goToNextScreen.putExtra("password",password);
                                //goToNextScreen.putExtra("listType","Collection");

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_search:
                                goToNextScreen = new Intent (SearchActivity.this,SearchActivity.class);
                                goToNextScreen.putExtra("username",username);
                                goToNextScreen.putExtra("password",password);
                               // goToNextScreen.putExtra("listType","Collection");
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_collection:
                                goToNextScreen = new Intent (SearchActivity.this,WantList.class);
                                goToNextScreen.putExtra("username",username);
                                goToNextScreen.putExtra("password",password);
                                goToNextScreen.putExtra("listType","Collection");

                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_wantlist:
                                goToNextScreen = new Intent (SearchActivity.this,WantList.class);
                                goToNextScreen.putExtra("username",username);
                                goToNextScreen.putExtra("password",password);
                                goToNextScreen.putExtra("listType","Want-List");
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_friends:
                                goToNextScreen = new Intent (SearchActivity.this,Friends.class);
                                goToNextScreen.putExtra("username",username);
                                goToNextScreen.putExtra("password",password);
                                // goToNextScreen.putExtra("listType","Collection");
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_logout:
                                goToNextScreen = new Intent (SearchActivity.this,ActivityHome.class);
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_exit:
                                //goToNextScreen = new Intent (SearchActivity.this,Friends.class);
                                //startActivity(goToNextScreen);

                                finish();

                                return true;

                            default:

                       /* Toast.makeText(UserProfile.this,
                                "Clicked popup menu item " + item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                       */
                                goToNextScreen = new Intent (SearchActivity.this,ActivityHome.class);
                                startActivity(goToNextScreen);

                                return false;
                        }

                    }
                });

                popup.inflate(R.menu.actions);
                popup.show();
            }
        });




        // Find the button that starts SearchActivity
        Button button_scan = (Button) findViewById(R.id.btn_scan);

        // Set a clickListener on that
        button_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a new intent to open the {@link SearchActivity}
                Intent scanIntent = new Intent(SearchActivity.this, ScanActivity.class);

                // Start the new activity
                startActivityForResult(scanIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("scanned_barcode");
                Toast.makeText(SearchActivity.this, result, Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
}