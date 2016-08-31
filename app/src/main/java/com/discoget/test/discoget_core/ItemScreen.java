package com.discoget.test.discoget_core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

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

}

