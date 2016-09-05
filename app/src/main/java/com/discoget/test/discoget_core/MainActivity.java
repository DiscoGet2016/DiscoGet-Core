package com.discoget.test.discoget_core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the button that starts SearchActivity
        Button button_search = (Button) findViewById(R.id.btn_searchActivity);

        // Set a clickListener on that
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to open the {@link SearchActivity}
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                // Start the new activity
                startActivity(searchIntent);
            }
        });

        // Find the button that starts LayoutSwitcher
        Button button_layoutSwitcher = (Button) findViewById(R.id.btn_startLayoutSwitcher);

        // Set a clickListener on that
        button_layoutSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to open the {@link LayoutSwitcher activity}
                Intent layoutIntent = new Intent(MainActivity.this, LayoutSwitcher.class);
                // Start the new activity
                startActivity(layoutIntent);
            }
        });

        Button btn_webpage = (Button) findViewById(R.id.btn_web_test);

        // Set a clickListener on that
        btn_webpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent to open the {@link SearchActivity}
                Intent searchIntent = new Intent(MainActivity.this, WebAccount.class);
                // Start the new activity
                startActivity(searchIntent);
            }
        });
    }

    public void AddMessage(View view) {
        TextView txt_mytext = (TextView) findViewById(R.id.txt_mytext);
        String msgOne = "Hi Team!!!";
        txt_mytext.setText(msgOne);
    }


}