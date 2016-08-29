package com.discoget.test.discoget_core;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created by Steven on 8/19/2016.
 */
public class AccountAccess extends AppCompatActivity {

   private int msgCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);


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

       }

    public void goLogin(View view)  {

        // Declaring variables
        //TextView msgText;
        // ImageView discogetIconButton = (ImageView) findViewById(R.id.imageView2);
        //String  msgToPrint = "Hello World!!!";

       /* Intent goToNextScreen = new Intent (this, MainActivity.class);

        final int result = 1;

        startActivity(goToNextScreen);
        */
        //TODO
        String toastString = "go Login...";
        Toast.makeText(AccountAccess.this,toastString, Toast.LENGTH_SHORT).show();

        // Get username and password from screen
        TextView username = (TextView) findViewById(R.id.etxt_username);
        TextView password = (TextView) findViewById(R.id.etxt_password);

        Intent goToNextScreen = new Intent (this, UserProfile.class);

        goToNextScreen.putExtra("username", username.getText().toString());
        goToNextScreen.putExtra("password", password.getText().toString());

        final int result = 1;
        startActivity(goToNextScreen);

    }


    public void goRegister(View view) {

        //TODO
        String toastString = "go Register...";
        Toast.makeText(AccountAccess.this,toastString, Toast.LENGTH_SHORT).show();

        Intent goToNextScreen = new Intent (this, AccountCreation.class);

        final int result = 1;

        startActivity(goToNextScreen);
    }

    public void goLogin2(View view) {

        //TODO
        String toastString = "go Forgot Password..";
        Toast.makeText(AccountAccess.this,toastString, Toast.LENGTH_SHORT).show();
    }
}
