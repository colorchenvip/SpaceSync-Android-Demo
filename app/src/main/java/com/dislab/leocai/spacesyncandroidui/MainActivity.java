package com.dislab.leocai.spacesyncandroidui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.dislab.leocai.spacesync.connection.DataServerMultiClient;
import com.dislab.leocai.spacesync.core.SpaceSync;
import com.dislab.leocai.spacesyncandroidui.ChartFragment;
import com.dislab.leocai.spacesyncandroidui.PhoneFragment;
import com.dislab.leocai.spacesyncandroidui.R;
import com.dislab.leocai.spacesyncandroidui.ui.DirectionFragment;

public class MainActivity extends FragmentActivity  {

    private DataServerMultiClient dataServerMultiClient = new DataServerMultiClient();

    private SpaceSync spaceSync;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main);
        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //1
        tabHost.addTab(tabHost.newTabSpec("3D Illustration")
                        .setIndicator("3D Illustration"),
                PhoneFragment.class,
                null);
        tabHost.addTab(tabHost.newTabSpec("Inertial Reading")
                        .setIndicator("Inertial Reading"),
                ChartFragment.class,
                null);
//        tabHost.addTab(tabHost.newTabSpec("Direction")
//                        .setIndicator("Direction"),
//                DirectionFragment.class,
//                null);


    }

    public DataServerMultiClient getDataServer(){
        return dataServerMultiClient;
    }



    /**************************
     *
     * 給子頁籤呼叫用
     *
     **************************/
    public String getAppleData(){
        return "Apple 123";
    }

    public void setSpaceSync(SpaceSync spaceSync) {
        this.spaceSync = spaceSync;
    }

    public SpaceSync getSpaceSync() {
        return spaceSync;
    }
}
