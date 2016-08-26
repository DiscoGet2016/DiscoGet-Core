package com.discoget.test.discoget_core;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

public class LayoutSwitcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_switcher);


        Button changeTo2 = (Button) findViewById(R.id.btn_changeTo_2);
        Button changeTo3 = (Button) findViewById(R.id.btn_changeTo_3);
        Button changeTo1 = (Button) findViewById(R.id.btn_changeTo_1);

        changeTo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.myViewFlipper);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.secondLayout)));
            }
        });

        changeTo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.myViewFlipper);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.thirdLayout)));
            }
        });

        changeTo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.myViewFlipper);
                viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(findViewById(R.id.firstLayout)));
            }
        });
    }
};
