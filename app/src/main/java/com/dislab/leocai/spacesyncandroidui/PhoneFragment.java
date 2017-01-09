package com.dislab.leocai.spacesyncandroidui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dislab.leocai.spacesync.connection.DataServer;
import com.dislab.leocai.spacesync.connection.DataServerMultiClient;
import com.dislab.leocai.spacesync.connection.OnConnectedListener;
import com.dislab.leocai.spacesync.connection.datalistteners.ObserverSpaceSyncMultiClient;
import com.dislab.leocai.spacesync.core.SpaceSync;
import com.dislab.leocai.spacesync.transformation.TrackingCallBack;
import com.dislab.leocai.spacesync.ui.PhoneViewCallBack;
import com.dislab.leocai.spacesync.utils.SpaceSyncConfig;
import com.dislab.leocai.spacesync.utils.SpaceSyncFactory;
import com.dislab.leocai.spacesyncandroidui.test.MainActivity;
import com.dislab.leocai.spacesyncandroidui.ui.PhoneDisplayerAndroidImpl;

import java.io.IOException;
import java.util.Observer;

public class PhoneFragment extends Fragment  implements View.OnClickListener, OnConnectedListener{

    private LinearLayout phonePane;
    private DataServer dataServerMultiClient;
    private TextView tvInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataServerMultiClient = ((MainActivity)getActivity()).getDataServer();
        return inflater.inflate(R.layout.content_main, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        phonePane = (LinearLayout)getView().findViewById(R.id.phonepane);
        tvInfo = (TextView)getView().findViewById(R.id.tv_info);
        getView().findViewById(R.id.btn_ready).setOnClickListener(this);
        try {
            dataServerMultiClient.setOnConnectionListener(this);
            dataServerMultiClient.startServer();
            String address = dataServerMultiClient.getAddress();
            log("Server Address:" + address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        tvInfo.setText(outState.getString("tvInfo",""));
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
//        if(savedInstanceState!=null&&tvInfo!=null)
//            savedInstanceState.putString("tvInfo", tvInfo.getText().toString());
    }

    private void log(final String s) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvInfo.setText(s);
            }
        });
    }

    @Override
    public void onClick(View v) {
        log("ready to receive data");
        int clientsNum = dataServerMultiClient.getClientsNum();
        TrackingCallBack[] trackingCallBacks = new TrackingCallBack[clientsNum];
        for (int i = 0; i < clientsNum; i++) {
            PhoneDisplayerAndroidImpl pcImpl = new PhoneDisplayerAndroidImpl(getActivity());
            addPhoneViewToPane(pcImpl);
            trackingCallBacks[i] = new PhoneViewCallBack(pcImpl);
        }
        SpaceSyncConfig.BUFFER_SIZE = 50;
        SpaceSyncConfig.SELECTED_FC_THRESHOLD = 0.5;
        SpaceSyncConfig.SYNC_THRESHOLD = 3;
        SpaceSync spaceSync = SpaceSyncFactory.getDefaultSpaceSync(clientsNum, trackingCallBacks, null, null);
        ((MainActivity)getActivity()).setSpaceSync(spaceSync);
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
        log(s + ":connected");
    }

    private void addPhoneViewToPane(PhoneDisplayerAndroidImpl pcImpl) {
        pcImpl.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        phonePane.addView(pcImpl);
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


}
