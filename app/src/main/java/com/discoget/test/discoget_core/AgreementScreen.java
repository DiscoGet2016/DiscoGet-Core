package com.discoget.test.discoget_core;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


/**
 * Created by Steven on 8/28/2016.
 */
public class AgreementScreen extends Activity {

    private static final String DISCOGET_CONSUMER_KEY = "zdpSaKQxYWoJxgEdyxGI";
    private static final String DISCOGET_CONSUMER_SECRET = "qDNFIuGPiidyrRqopTyIcgGzOuuGHGhU";

    //SQLiteDatabase discogetDB = null;

    private SQLiteDatabase discogetDB;
    private MySQLiteHelper dbHelper;
    private Boolean debugFlag = true;   // true turns of debug skips and sets...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new MySQLiteHelper(this);


       if (debugFlag) {
           // clear database for testing
           this.deleteDatabase("DiscoGetDB.db");  // DiscoGetDB.db
           Toast.makeText(this, "DiscoGet Database Deleted", Toast.LENGTH_SHORT).show();
        }



        if (!db_Exists()) {

            setContentView(R.layout.agreement_screen);


            String agreementText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt" +
                    " ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi" +
                    "ut aliquip ex ea commodo consequat.\n\n" +
                    "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum" +
                    " dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia " +
                    "deserunt mollit anim id est laborum.\n";

            TextView agreement = (TextView) findViewById(R.id.tv_agreement_agreement);

            agreement.setText(agreementText);

        } else {

            finish();

            Intent goToNextScreen = new Intent (this, AccountAccess.class);
            startActivity(goToNextScreen);

        }

    }

    public void goSubmit (View view) {

        // Create DB file
        createDatabase();
        // add agreement acceptance date...

        // upload to cloud  ???


        // goto Account Access Screen
        finish();  // close this activity

        Intent goToNextScreen = new Intent (this, CreateUserAccount.class);
        startActivity(goToNextScreen);

    }

    public void goCancel (View view) {
        // exit app do nothing...
        finish();
    }


    public void createDatabase() {

        try{

           // dbHelper = new MySQLiteHelper(this);


           /*====================================================
            //Log.e("DataBase","trying");
             //

            // Opens a current database or creates it
            // Pass the database name, designate that only this app can use it
            // and a DatabaseErrorHandler in the case of database corruption
            */ //=====================================================================

         //   discogetDB = this.openOrCreateDatabase("DiscoGetDB", MODE_PRIVATE, null);
            discogetDB = dbHelper.getWritableDatabase();

            // Create DB tables
            // Agreement table  -- store agreement info and ...?
            discogetDB.execSQL("CREATE TABLE IF NOT EXISTS agreement " +
                   "(id integer primary key, approved VARCHAR, agreementdate VARCHAR);");

                // add data to this table
                discogetDB.execSQL("INSERT INTO agreement (approved, agreementdate) VALUES ('yes!!!', 'today')");


           // DiscoGet App table  -- stores secret vendor key
            discogetDB.execSQL("CREATE TABLE IF NOT EXISTS discogetapp " +
                    "(id integer primary key, consumerkey VARCHAR, consumersecret VARCHAR);");

                // add data to this table
                discogetDB.execSQL("INSERT INTO discogetapp (consumerkey, consumersecret ) VALUES ('" +
                        DISCOGET_CONSUMER_KEY + "', '" + DISCOGET_CONSUMER_SECRET + "')");

           // User table  -- store user data and user authication token with Discogs
            discogetDB.execSQL("CREATE TABLE IF NOT EXISTS user " +
                    "(id integer primary key, uid VARCHAR, usertype VARCHAR, username VARCHAR," +
                    "password VARCHAR, firstname VARCHAR, lastname VARCHAR, fullname VARCHAR, userbio, VARCHAR, emailaddress VARCHAR," +
                    "mobilenumber VARCHAR, imageurl VARCHAR, discogstoken VARCHAR, discogskey VARCHAR);");

           // Items -- store all items for user and friends
            discogetDB.execSQL("CREATE TABLE IF NOT EXISTS items " +
                    "(id integer primary key, owner VARCHAR, itemurl VARCHAR, " +
                    "imageurl VARCHAR, barcode VARCHAR, shortdescription VARCHAR, " +
                    "whichlist VARCHAR, artist VARCHAR, album VARCHAR, albumyear VARCHAR, " +
                    "catalogid VARCHAR, deleteflag VARCHAR);");

         //   discogetDB.close();

           Toast.makeText(this,"Items DB created...",Toast.LENGTH_LONG).show();



            // The database on the file system
            File database = this.getDatabasePath("DiscoGetDB.db");

            if (debugFlag) {
                // Check if the database exists
                if (database.exists()) {
                    Toast.makeText(this, "DiscoGet Database Created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "DiscoGet Database Missing", Toast.LENGTH_SHORT).show();
                }
            }

        }

        catch(Exception e){

            Log.e("DiscoGet Database ERROR", "Error Creating Database");

        } finally {

            dbHelper.close();

        }
    } // end of database creation

    private boolean db_Exists() {

        /*
        File extStore = Environment.getExternalStorageDirectory();
        File myFile = new File(extStore.getAbsolutePath() + "DiscoGetDB.db");

        if(myFile.exists()) {

            return false;
        } else {
            return false;

        }
        */

        File database = this.getDatabasePath("DiscoGetDB.db");

        // Check if the database exists
        if (database.exists()) {
            if (debugFlag) {
                Toast.makeText(this, "DiscoGet DB Found", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            if (debugFlag) {
                Toast.makeText(this, "DiscoGet DB not Found", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        //File discoGetDB = new File("DiscoGetDB.db");

        //return discoGetDB.exists();

    }
}
