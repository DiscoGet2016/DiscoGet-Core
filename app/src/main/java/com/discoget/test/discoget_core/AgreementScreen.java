package com.discoget.test.discoget_core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import java.io.File;


/**
 * Created by Steven on 8/28/2016.
 */
public class AgreementScreen extends Activity {

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
                    "ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum" +
                    " dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia " +
                    "deserunt mollit anim id est laborum.\n\n" +
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt" +
                    " ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi" +
                    "ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum" +
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

        Intent goToNextScreen = new Intent (this, AccountAccess.class);
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

            // Execute an SQL statement that isn't select

            discogetDB.execSQL("CREATE TABLE IF NOT EXISTS agreement " +
                   "(id integer primary key, approved VARCHAR, agreementdate VARCHAR);");
           // discogetDB.ex

            discogetDB.execSQL("INSERT INTO agreement (approved, agreementdate) VALUES ('yes!!!', 'today')");

         //   discogetDB.close();

            dbHelper.close();

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

            Log.e("CONTACTS ERROR", "Error Creating Database");

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
