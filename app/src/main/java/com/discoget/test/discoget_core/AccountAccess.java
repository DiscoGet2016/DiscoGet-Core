package com.discoget.test.discoget_core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    }

    public void goLogin(View view)  {

        // Declaring vatiables
        TextView msgText;
        // ImageView discogetIconButton = (ImageView) findViewById(R.id.imageView2);
        String  msgToPrint = "Hello World!!!";

       /* Intent goToNextScreen = new Intent (this, MainActivity.class);

        final int result = 1;

        startActivity(goToNextScreen);
        */
        //TODO
        String toastString = "go Login...";
        Toast.makeText(AccountAccess.this,toastString, Toast.LENGTH_SHORT).show();


        Intent goToNextScreen = new Intent (this, UserProfile.class);

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
