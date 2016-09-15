package com.discoget.test.discoget_core;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;



/**
 * Created by Steven on 8/19/2016.
 */
public class AccountAccess extends AppCompatActivity {

   private int msgCounter = 0;
   private Boolean debugFlag = false;

    private SQLiteDatabase discogetDB;
    private MySQLiteHelper dbHelper;

    String uid; // = resultSet.getString(0);
    String pw; //  = resultSet.getString(1);
    String token; //  = resultSet.getString(2);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        dbHelper = new MySQLiteHelper(this);

        discogetDB = dbHelper.getWritableDatabase();

        // get user data...
        /*
        Cursor resultSet = mydatbase.rawQuery("Select * from TutorialsPoint",null);
        resultSet.moveToFirst();
        String username = resultSet.getString(1);
        String password = resultSet.getString(2);
         */
        Cursor resultSet = discogetDB.rawQuery("SELECT uid, password, discogstoken FROM user", null);
        resultSet.moveToFirst();
        uid = resultSet.getString(0);
        pw  = resultSet.getString(1);
        token  = resultSet.getString(2);



       if (debugFlag) { Toast.makeText(this,"USER ID = " + uid + "  " + pw + "  " + token,Toast.LENGTH_LONG).show(); }




        //ToolBar...
        String paneTitle = " ";

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);

        getSupportActionBar().setTitle(paneTitle);
        getSupportActionBar().setIcon(R.drawable.ic_exit_icon30x);

        my_toolbar.findViewById(R.id.my_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            finish();

               // Intent goToNextScreen = new Intent(AccountCreation.this, AccountAccess.class);
               // startActivity(goToNextScreen);
            }
        });


        EditText tvUserName = (EditText) findViewById(R.id.et_login_username);

        tvUserName.setText(uid.toString());


       }

    public void goLogin(View view) {

        // Declaring variables
        //TextView msgText;
        // ImageView discogetIconButton = (ImageView) findViewById(R.id.imageView2);
        //String  msgToPrint = "Hello World!!!";
        EditText userpw = (EditText) findViewById(R.id.et_login_password);


        if (pw.toString().equals(userpw.getText().toString())) {


       /* Intent goToNextScreen = new Intent (this, MainActivity.class);

        final int result = 1;

        startActivity(goToNextScreen);
        */
            //TODO
            if (debugFlag) {
                String toastString = "go Login...";
                Toast.makeText(AccountAccess.this, toastString, Toast.LENGTH_SHORT).show();
            }
            // Get username and password from screen
            TextView username = (TextView) findViewById(R.id.et_login_username);
            TextView password = (TextView) findViewById(R.id.et_login_password);

            Intent goToNextScreen = new Intent(this, UserProfile.class);

            goToNextScreen.putExtra("username", username.getText().toString());
            goToNextScreen.putExtra("password", password.getText().toString());

            final int result = 1;
            startActivity(goToNextScreen);

        } else {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
        }

    }


    public void goRegister(View view) {

        //TODO
        String toastString = "go Register...";
        Toast.makeText(AccountAccess.this,toastString, Toast.LENGTH_SHORT).show();

        Intent goToNextScreen = new Intent (this, WebAccount.class);

        final int result = 1;

        startActivity(goToNextScreen);
    }

    public void goLogin2(View view) {

        //TODO
        String toastString = "go Forgot Password..";
        Toast.makeText(AccountAccess.this,toastString, Toast.LENGTH_SHORT).show();
    }
}
