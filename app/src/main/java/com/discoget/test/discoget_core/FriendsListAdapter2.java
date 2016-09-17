package com.discoget.test.discoget_core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
public class FriendsListAdapter2 extends ArrayAdapter<FriendsItems> {
    public FriendsListAdapter2(Context context, ArrayList<FriendsItems> friendsList) {
        super(context, 0, friendsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FriendsItems friendsInfo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout_select_friend, parent, false);
        }


        // Lookup view for data population
        TextView txtFriendID = (TextView) convertView.findViewById(R.id.txt_friendUserName);
        TextView txtFriendFullName = (TextView) convertView.findViewById(R.id.txt_friendFullName);
        //TextView txtYear = (TextView) convertView.findViewById(R.id.txt_year);
        //TextView  = (TextView) convertView.findViewById(R.id.tvHome);
        ImageView imgFriend = (ImageView) convertView.findViewById(R.id.img_friend);

        // Populate the data into the template view using the data object

        txtFriendID.setText(friendsInfo.friendUserName);
        txtFriendFullName.setText(friendsInfo.friendFullName);
        //txtYear.setText(collectionItem.itemYear);
        //tvHome.setText(collectionItem.itemCoverURL);

        // add button options
        Button addButton = (Button) convertView.findViewById(R.id.btn_addNewFriend);

        //if (collectionItem.itemLabel.equals("No username match...")) {
        //    addButton.setClickable(false);
        //    addButton.setBackgroundColor(Color.GRAY);

        //    String tempURL="http://www.aptitude.co.uk/img/album-covers/The-Beatles-Abbey-road.png";

            String tempURL2 = friendsInfo.friendImageURL;  //"https://s.yimg.com/fz/api/res/1.2/7GzBaZrJQJMNhFfKJmMY8A--/YXBwaWQ9c3JjaGRkO2g9NTAwO3E9OTU7dz01MDA-/http://www.amiright.com/album-covers/images/album-Kiss-Destroyer.jpg";

        //imgAlbumCover.setImageBitmap(getBitmapFromURL(tempURL));

       new ImageLoadTask(tempURL2, imgFriend).execute();

       // imgAlbumCover.setImageURI(new ImageLoadTask(url, imageView).execute();;
        //imageView.setImageBitmap(getBitmapFromURL(url));

        // Return the completed view to render on screen
        return convertView;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
}
