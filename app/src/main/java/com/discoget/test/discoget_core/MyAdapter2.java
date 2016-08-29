package com.discoget.test.discoget_core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


// We can create custom adapters
class MyAdapter2 extends ArrayAdapter<String> {


    public MyAdapter2(Context context, String[] values){

        super(context, R.layout.row_layout_collection, values);

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
        View theView = theInflater.inflate(R.layout.row_layout_collection, parent, false);

        String gravatarHome = "https://www.gravatar.com/avatar/";
        String defaultAvatar = "http://www.digitalqatar.qa/wp-content/uploads/sites/2/2011/12/android.jpg";

        // build friendsDataArray
        String[] friendsURLArray =  {
                "https://tse1.mm.bing.net/th?id=OIP.M2e9a86e275a3ee50f8701395e930db35H0&pid=15.1&P=0&w=300&h=300",
                "https://tse4.mm.bing.net/th?id=OIP.Ma9e6a4decc5f8e2abe8506f8fee3764aH2&pid=15.1&P=0&w=169&h=169",         // Enrique
                "https://tse3.mm.bing.net/th?id=OIP.Madeaef6b0689fbc676e8881f41ba3403o0&pid=15.1&P=0&w=170&h=170",         // Gurui
                "https://tse1.mm.bing.net/th?id=OIP.M2f4097ca931ee581b61b6823336a73dcH0&pid=15.1&P=0&w=300&h=300",         //Steve
                "https://tse4.mm.bing.net/th?id=OIP.M84e33f90af6a7f09cbdc7b73a87b41dfH0&pid=15.1&P=0&w=171&h=169",         //Test dude
                "https://tse2.mm.bing.net/th?id=OIP.Mf872a40e1326c8f8b357db79199a764fH0&pid=15.1&P=0&w=300&h=300",
                ""};


        int posNumber = position;

        // We retrieve the text from the array
        String myFriend = getItem(position);

        // Get the TextView we want to edit
        TextView theTextView = (TextView) theView.findViewById(R.id.txt_artist);

        // Put the next TV Show into the TextView
        theTextView.setText(myFriend);

        // Get the ImageView in the layout
        ImageView theImageView = (ImageView) theView.findViewById(R.id.img_albumCover);

        // We can set a ImageView like this

       // String imageURL = "http://assets.blabbermouth.net.s3.amazonaws.com/media/powerwolfblessedcd.jpg";

        // Image link from internet    //(ImageView) theView.findViewById(R.id.img_friend)
        /*new DownloadImageFromInternet(theImageView)//findViewById(R.id.image_view))
                .execute(friendsURLArray[position]); //("https://pbs.twimg.com/profile_images/630285593268752384/iD1MkFQ0.png");
*/
        //theImageView.setImageResource();

        return theView;

    }





}