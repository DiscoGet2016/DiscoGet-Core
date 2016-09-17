package com.discoget.test.discoget_core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Steven on 8/30/2016.
 */
public class ItemScreen extends AppCompatActivity {

// How to Load Image From URL (Internet) in Android ImageView

    String username = "";
    String password = "";

    String itemArtist = "";
    String itemLabel = "";
    String itemYear = "";
    String itemURL = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_screen);

        // add tool bar
        //ToolBar...
        String paneTitle = "  Item Screen";

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle(paneTitle);
        getSupportActionBar().setIcon(R.drawable.back_30x);

        my_toolbar.findViewById(R.id.my_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Intent goToNextScreen = new Intent(AccountCreation.this, AccountAccess.class);
                // startActivity(goToNextScreen);
            }
        });

        // end of tool bar



        TextView tvArtist = (TextView) findViewById(R.id.tv_item_artist);
        TextView tvLabel = (TextView) findViewById(R.id.tv_item_label);;
        TextView tvYear = (TextView) findViewById(R.id.tv_item_year);;
        ImageView ivAlbumCover = (ImageView) findViewById(R.id.iv_item_cover);


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


              tvArtist.setText(itemArtist);
              tvLabel.setText(itemLabel);
              tvYear.setText(itemYear);

              new ImageLoadTask(itemURL, ivAlbumCover).execute();
        }

    }


    public void goAddToCollection(View view) {
        // add item to Collection
        Toast.makeText(this, itemArtist + "Added to Collection", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void goAddToWantList(View view) {
        // add item to WantList
        Toast.makeText(this, itemArtist + "Added to Want-List", Toast.LENGTH_SHORT).show();
        finish();


    }
}

