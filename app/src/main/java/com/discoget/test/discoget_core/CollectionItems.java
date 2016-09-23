package com.discoget.test.discoget_core;

/**
 * Created by Steven on 8/30/2016.
 */
public class CollectionItems {

    public String searchType;
    public String itemArtist;       //Artist
    public String itemLabel;        //Label / Album
    public String itemYear;         //year
    public String itemCoverURL;     //coverURL
    public String itemResourceID;   //resource id - link to Discogs
    public String itemListType;
    public String itemTitle;        // album title
    // add more items for later use...

    public CollectionItems(String itemArtist, String itemLabel, String itemYear ,String itemCoverURL, String itemResourceID, String itemListType) {
            this.searchType = "none";
            this.itemArtist = itemArtist;
            this.itemLabel = itemLabel;
            this.itemYear = itemYear;
            this.itemCoverURL = itemCoverURL;
            this.itemResourceID = itemResourceID;
            this.itemListType = itemListType;
            this.itemTitle = "";

    }

    public CollectionItems(String itemArtist, String itemLabel, String itemYear,
                           String itemCoverURL, String itemResourceID, String itemListType, String itemTitle) {
        this.searchType = "none";
        this.itemArtist = itemArtist;
        this.itemLabel = itemLabel;
        this.itemYear = itemYear;
        this.itemCoverURL = itemCoverURL;
        this.itemResourceID = itemResourceID;
        this.itemListType = itemListType;
        this.itemTitle = itemTitle;

    }

    public CollectionItems(String searchType, String itemArtist, String itemLabel, String itemYear,
                           String itemCoverURL, String itemResourceID, String itemListType, String itemTitle) {
        this.searchType = searchType;
        this.itemArtist = itemArtist;
        this.itemLabel = itemLabel;
        this.itemYear = itemYear;
        this.itemCoverURL = itemCoverURL;
        this.itemResourceID = itemResourceID;
        this.itemListType = itemListType;
        this.itemTitle = itemTitle;

    }
}


