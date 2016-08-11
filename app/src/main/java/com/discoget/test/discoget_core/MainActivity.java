package com.discoget.test.discoget_core;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void AddMessage(View view) {

        //Button btn_One = (Button);
        TextView txt_msgOne;

        txt_msgOne = (TextView) findViewById(R.id.txt_msgOne);

        String msgOne = "Hi Team!!!";

        txt_msgOne.setText(msgOne);



    }
}
