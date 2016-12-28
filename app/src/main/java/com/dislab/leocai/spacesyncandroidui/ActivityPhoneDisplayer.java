package com.dislab.leocai.spacesyncandroidui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.dislab.leocai.spacesyncandroidui.ui.PhoneDisplayerAndroidImpl;

public class ActivityPhoneDisplayer extends AppCompatActivity {

    private LinearLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        frame = (LinearLayout) findViewById(R.id.phonepane);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        frame.setWeightSum(2);

        FrameLayout ft = new FrameLayout(this);
        PhoneDisplayerAndroidImpl k = new PhoneDisplayerAndroidImpl(this);
        k.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));


        frame.addView(k);


        FrameLayout ft2 = new FrameLayout(this);
        PhoneDisplayerAndroidImpl k2 = new PhoneDisplayerAndroidImpl(this);
        k2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        frame.addView(k2);



    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
