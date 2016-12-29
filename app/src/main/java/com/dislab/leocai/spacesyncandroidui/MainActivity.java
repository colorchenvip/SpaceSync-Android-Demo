package com.dislab.leocai.spacesyncandroidui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dislab.leocai.spacesync.connection.DataServerMultiClient;
import com.dislab.leocai.spacesync.connection.OnConnectedListener;
import com.dislab.leocai.spacesync.connection.datalistteners.ObserverSpaceSyncMultiClient;
import com.dislab.leocai.spacesync.core.ConsistentExtraction;
import com.dislab.leocai.spacesync.core.ConsistentExtractionImpl;
import com.dislab.leocai.spacesync.core.DirectionEstimator;
import com.dislab.leocai.spacesync.core.DirectionEstimatorImpl;
import com.dislab.leocai.spacesync.core.OreintationTracker;
import com.dislab.leocai.spacesync.core.OreintationTrackerImpl;
import com.dislab.leocai.spacesync.core.SpaceSync;
import com.dislab.leocai.spacesync.core.SpaceSyncConsistanceImpl;
import com.dislab.leocai.spacesync.transformation.GyrGaccMatrixTracker;
import com.dislab.leocai.spacesync.transformation.TrackingCallBack;
import com.dislab.leocai.spacesync.ui.PhoneViewCallBack;
import com.dislab.leocai.spacesync.utils.SpaceSyncConfig;
import com.dislab.leocai.spacesync.utils.SpaceSyncFactory;
import com.dislab.leocai.spacesyncandroidui.ui.PhoneDisplayerAndroidImpl;

import java.io.IOException;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnConnectedListener {

    private DataServerMultiClient dataServerMultiClient = new DataServerMultiClient();
    private LinearLayout phonePane;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        phonePane = (LinearLayout)findViewById(R.id.phonepane);
        tvInfo = (TextView)findViewById(R.id.tv_info);
        findViewById(R.id.btn_ready).setOnClickListener(this);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        try {
            dataServerMultiClient.setOnConnectionListener(this);
            dataServerMultiClient.startServer();
            String address = dataServerMultiClient.getAddress();
            log("Server Address:"+address);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void log(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvInfo.setText(s);
            }
        });
    }

    private void addPhoneViewToPane(PhoneDisplayerAndroidImpl pcImpl) {
        pcImpl.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        phonePane.addView(pcImpl);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        log("ready to receive data");
        int clientsNum = dataServerMultiClient.getClientsNum();
        TrackingCallBack[] trackingCallBacks = new TrackingCallBack[clientsNum];
        for (int i = 0; i < clientsNum; i++) {
            PhoneDisplayerAndroidImpl pcImpl = new PhoneDisplayerAndroidImpl(this);
            addPhoneViewToPane(pcImpl);
            trackingCallBacks[i] = new PhoneViewCallBack(pcImpl);
        }
        SpaceSyncConfig.BUFFER_SIZE = 50;
        SpaceSyncConfig.SELECTED_FC_THRESHOLD = 0.5;
        SpaceSyncConfig.SYNC_THRESHOLD = 3;
        SpaceSync spaceSync = SpaceSyncFactory.getDefaultSpaceSync(clientsNum, trackingCallBacks, null, null);
        Observer spaceSyncOb = new ObserverSpaceSyncMultiClient(clientsNum, spaceSync);
        dataServerMultiClient.addDataListener(spaceSyncOb);
        try {
            dataServerMultiClient.receivedData();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void newClientConnected(String s) {
        log(s+":connected");
    }
}
