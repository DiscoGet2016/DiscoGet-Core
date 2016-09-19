package com.discoget.test.discoget_core;

/**
 * Created by Steven on 8/30/2016.
 */
public class CollectionItems {

    public String itemArtist;       //Artist
    public String itemLabel;        //Label / Album
    public String itemYear;         //year
    public String itemCoverURL;     //coverURL
    public String itemResourceID;   //resource id - link to Discogs
    public String itemListType;
    // add more items for later use...

    public CollectionItems(String itemArtist, String itemLabel, String itemYear ,String itemCoverURL, String itemResourceID, String itemListType) {
            this.itemArtist = itemArtist;
            this.itemLabel = itemLabel;
            this.itemYear = itemYear;
            this.itemCoverURL = itemCoverURL;
            this.itemResourceID = itemResourceID;
            this.itemListType = itemListType;

    }


}


