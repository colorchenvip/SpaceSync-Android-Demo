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
import com.dislab.leocai.spacesync.connection.OnConnectedListener;
import com.dislab.leocai.spacesync.connection.datalistteners.ObserverSpaceSyncMultiClient;
import com.dislab.leocai.spacesync.core.SpaceSync;
import com.dislab.leocai.spacesync.transformation.TrackingCallBack;
import com.dislab.leocai.spacesync.ui.PhoneViewCallBack;
import com.dislab.leocai.spacesync.utils.SpaceSyncFactory;
import com.dislab.leocai.spacesyncandroidui.ui.PhoneDisplayerAndroidImpl;

import java.io.IOException;
import java.util.Observer;

public class PhoneFragment extends Fragment  implements View.OnClickListener, OnConnectedListener{

    private LinearLayout phonePane;
    private DataServer dataServerMultiClient;
    private TextView tvInfo;
    boolean started = false;
    private LinearLayout toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(!started){
            dataServerMultiClient = ((MainActivity)getActivity()).getDataServer();
            try {
                dataServerMultiClient.setOnConnectionListener(this);
                dataServerMultiClient.startServer();
            }catch (IOException e) {
                e.printStackTrace();
            }
            started = true;
        }
        return inflater.inflate(R.layout.content_main, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        phonePane = (LinearLayout)getView().findViewById(R.id.phonepane);
        tvInfo = (TextView)getView().findViewById(R.id.tv_info);
        toolbar = (LinearLayout)getView().findViewById(R.id.phonetool);
        getView().findViewById(R.id.btn_ready).setOnClickListener(this);
        String address = dataServerMultiClient.getAddress();
        log("Server Address:" + address);
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
//        int clientsNum = dataServerMultiClient.getClientsNum();
        toolbar.setVisibility(View.GONE);
        int clientsNum = 3;
        TrackingCallBack[] trackingCallBacks = new TrackingCallBack[clientsNum];
        for (int i = 0; i < clientsNum; i++) {
            PhoneDisplayerAndroidImpl pcImpl = new PhoneDisplayerAndroidImpl(getActivity());
            addPhoneViewToPane(pcImpl);
            trackingCallBacks[i] = new PhoneViewCallBack(pcImpl);
        }
//        SpaceSync spaceSync = SpaceSyncFactory.getDefaultSpaceSync(clientsNum, trackingCallBacks, null, null);
//        ((MainActivity)getActivity()).setSpaceSync(spaceSync);
//        Observer spaceSyncOb = new ObserverSpaceSyncMultiClient(clientsNum, spaceSync);
//        dataServerMultiClient.addDataListener(spaceSyncOb);
//        try {
//            dataServerMultiClient.receivedData();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
    }

    @Override
    public void newClientConnected(String s) {
        log(s + ":connected");
    }

    private void addPhoneViewToPane(PhoneDisplayerAndroidImpl pcImpl) {
        LinearLayout.LayoutParams k = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 800);
        k.setMargins(20,5,20,5);
        pcImpl.setLayoutParams(k);
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
