package com.discoget.test.discoget_core;

/**
 * Created by Steven on 8/30/2016.
 */
public class FriendsItems {

    public String friendUserName;    //Artist
    public String friendFullName;     //Label / Album
    public String friendEmail;      //year
    public String friendMobileNumber;      //year
    public String friendImageURL;  // coverURL
    //public String friendDeleteFlag;   // for later use

    // add more items for later use...
    public FriendsItems(String friendUserName, String friendFullName, String friendImageURL, String friendEmail, String friendMobileNumber) {
        this.friendUserName = friendUserName;
        this.friendFullName = friendFullName;
        this.friendImageURL = friendImageURL;
        this.friendEmail = friendEmail;
        this.friendMobileNumber = friendMobileNumber;
        //this.friendDeleteFlag = friendDeleteFlag;
    }
    public FriendsItems(String friendUserName, String friendFullName, String friendImageURL) {
        this.friendUserName = friendUserName;
        this.friendFullName = friendFullName;
        this.friendImageURL = friendImageURL;
        this.friendEmail = "";
        this.friendMobileNumber = "";
        //this.friendDeleteFlag = friendDeleteFlag;

    }
}


