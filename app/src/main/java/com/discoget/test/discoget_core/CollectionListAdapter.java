package com.discoget.test.discoget_core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Steven on 8/30/2016.
 */
public class CollectionListAdapter extends ArrayAdapter<CollectionItems> {
    public CollectionListAdapter(Context context, ArrayList<CollectionItems> collectionList) {
        super(context, 0, collectionList);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap", "returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CollectionItems collectionItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout_collection, parent, false);
        }
        // Lookup view for data population
        TextView txtArtist = (TextView) convertView.findViewById(R.id.txt_artist);
        TextView txtAlbumTitle = (TextView) convertView.findViewById(R.id.txt_album_title);
        TextView txtYear = (TextView) convertView.findViewById(R.id.txt_year);

        //TextView  = (TextView) convertView.findViewById(R.id.tvHome);
        ImageView imgAlbumCover = (ImageView) convertView.findViewById(R.id.img_albumCover);

        // Populate the data into the template view using the data object
        txtArtist.setText(collectionItem.itemArtist);
        txtAlbumTitle.setText(collectionItem.itemTitle);
        txtYear.setText(collectionItem.itemYear);
        //tvHome.setText(collectionItem.itemCoverURL);

        String tempURL="http://www.aptitude.co.uk/img/album-covers/The-Beatles-Abbey-road.png";

        String tempURL2 = collectionItem.itemCoverURL;  //"https://s.yimg.com/fz/api/res/1.2/7GzBaZrJQJMNhFfKJmMY8A--/YXBwaWQ9c3JjaGRkO2g9NTAwO3E9OTU7dz01MDA-/http://www.amiright.com/album-covers/images/album-Kiss-Destroyer.jpg";

        //imgAlbumCover.setImageBitmap(getBitmapFromURL(tempURL));

       new ImageLoadTask(tempURL2, imgAlbumCover).execute();

       // imgAlbumCover.setImageURI(new ImageLoadTask(url, imageView).execute();;
        //imageView.setImageBitmap(getBitmapFromURL(url));

        // Return the completed view to render on screen
        return convertView;
    }
}
