package com.discoget.test.discoget_core;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import java.io.InputStream;



// We can create custom adapters
class MyAdapter extends ArrayAdapter<String> {


    public MyAdapter(Context context, String[] values){

        super(context, R.layout.row_layout_2, values);

    }

    // Override getView which is responsible for creating the rows for our list
    // position represents the index we are in for the array.

    // convertView is a reference to the previous view that is available for reuse. As
    // the user scrolls the information is populated as needed to conserve memory.

    // A ViewGroup are invisible containers that hold a bunch of views and
    // define their layout properties.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // The LayoutInflator puts a layout into the right View
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        // inflate takes the resource to load, the parent that the resource may be
        // loaded into and true or false if we are loading into a parent view.
        View theView = theInflater.inflate(R.layout.row_layout_2, parent, false);

        String gravatarHome = "https://www.gravatar.com/avatar/";
        String defaultAvatar = "http://www.digitalqatar.qa/wp-content/uploads/sites/2/2011/12/android.jpg";

        // build friendsDataArray
        String[] friendsURLArray =  {
                gravatarHome + "d4cb23b09d74f33ef8ad43fc0e0896f9",         // Enrique
                gravatarHome + "baf9b97e84ac714acb14a65e9855538d",         // Gurui
                gravatarHome + "956a76b2e3547849f3a62df862865be2",         //Steve
                gravatarHome + "205e460b479e2e5b48aec07710c08d50",         //Test dude
                defaultAvatar,
                defaultAvatar};


int posNumber = position;

        // We retrieve the text from the array
        String myFriend = getItem(position);

        // Get the TextView we want to edit
        TextView theTextView = (TextView) theView.findViewById(R.id.txt_friendID);

        // Put the next TV Show into the TextView
        theTextView.setText(myFriend);

        // Get the ImageView in the layout
        ImageView theImageView = (ImageView) theView.findViewById(R.id.img_friend);

        // We can set a ImageView like this

        String imageURL = "http://assets.blabbermouth.net.s3.amazonaws.com/media/powerwolfblessedcd.jpg";

        // Image link from internet    //(ImageView) theView.findViewById(R.id.img_friend)
        new DownloadImageFromInternet(theImageView)//findViewById(R.id.image_view))
           .execute(friendsURLArray[position]); //("https://pbs.twimg.com/profile_images/630285593268752384/iD1MkFQ0.png");

        //theImageView.setImageResource(imageURL);

        return theView;

    }





}