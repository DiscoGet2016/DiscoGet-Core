package com.discoget.test.discoget_core;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class SearchActivity extends AppCompatActivity {

    String username = "";
    String password = "";
    String listType = "";

    static final int SCAN_BARCODE = 1;

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
            //listType =(String) b.get("listType");

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
                        finish();

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

                            //----- changed - SAW  09/16/16 ---
                            case R.id.menu_collection:

                                goCollectionList();

                                return true;
                            case R.id.menu_wantlist:

                                goWantList();

                                return true;
                            // ---- end of chagne -------------

                            case R.id.menu_friends:
                                goToNextScreen = new Intent (SearchActivity.this,Friends.class);
                                goToNextScreen.putExtra("username",username);
                                goToNextScreen.putExtra("password",password);
                                // goToNextScreen.putExtra("listType","Collection");
                                startActivity(goToNextScreen);

                                return true;
                            case R.id.menu_logout:
                                finish();
                                goToNextScreen = new Intent (SearchActivity.this,ActivityHome.class);
                                startActivity(goToNextScreen);

                                return true;


                            default:

                               goToNextScreen = new Intent (SearchActivity.this,UserProfile.class);
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
                startActivityForResult(scanIntent, SCAN_BARCODE);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SCAN_BARCODE) {
            if(resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("scanned_barcode");
                Toast.makeText(SearchActivity.this, "Barcode: " + result, Toast.LENGTH_LONG).show();
                if (!(result.equals("Nothing to Read")) || result.length() > 1) {
                    /// do stuff here...
                    //finish();

                    Intent goToNextScreen = new Intent(this, DisplaySearchResults.class);

                    //goToNextScreen.putExtra("username", username.getText().toString());
                    //goToNextScreen.putExtra("password", password.getText().toString());
                    goToNextScreen.putExtra("searchType", "barcode");
                    goToNextScreen.putExtra("searchValue", result);

                    final int a_result = 1;
                    startActivity(goToNextScreen);
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                String result = "Nothing scanned";
                Toast.makeText(SearchActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }
    }//onActivityResult

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

                TextView searched = (TextView) findViewById(R.id.searchbar_query);
                searched.setText(searchView.getQuery());

                Intent goToNextScreen = new Intent(SearchActivity.this, DisplaySearchResults.class);

                goToNextScreen.putExtra("username", "test"); //username.getText().toString());
                goToNextScreen.putExtra("password",  "test"); //password.getText().toString());
                goToNextScreen.putExtra("searchType", "title");
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

    public void gotest(View view) {

        // FOR TESTING...
        Intent goToNextScreen = new Intent(SearchActivity.this, DisplaySearchResults.class);

        goToNextScreen.putExtra("username", "test"); //username.getText().toString());
        goToNextScreen.putExtra("password",  "test"); //password.getText().toString());
        goToNextScreen.putExtra("searchType", "barcode");
        goToNextScreen.putExtra("searchValue", "5413356693324");

        final int a_result = 1;
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