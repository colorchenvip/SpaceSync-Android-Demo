package com.dislab.leocai.spacesyncandroidui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        int clientsNum = dataServerMultiClient.getClientsNum();
        toolbar.setVisibility(View.GONE);
//        int clientsNum = 3;
        TrackingCallBack[] trackingCallBacks = new TrackingCallBack[clientsNum];
        for (int i = 0; i < clientsNum; i++) {
            PhoneDisplayerAndroidImpl pcImpl = new PhoneDisplayerAndroidImpl(getActivity());
            addPhoneViewToPane(i, pcImpl);
            trackingCallBacks[i] = new PhoneViewCallBack(pcImpl);
        }
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

    private void addPhoneViewToPane(int i, PhoneDisplayerAndroidImpl pcImpl) {
        LinearLayout.LayoutParams k = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 600);
        k.setMargins(20, 5, 20, 5);
        pcImpl.setLayoutParams(k);
        RelativeLayout rl = new RelativeLayout(getContext());
        LinearLayout.LayoutParams fk = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 610);
        fk.gravity = Gravity.CENTER_HORIZONTAL;
        rl.setLayoutParams(fk);
        rl.addView(pcImpl);
        TextView tv_device = new TextView(getContext());
        tv_device.setText("S" + i);
        tv_device.setTextColor(Color.WHITE);
        rl.addView(tv_device);

        RelativeLayout.LayoutParams k2 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        k2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        k2.setMargins(0, 0, 50, 0);
        TextView tv_angle = new TextView(getContext());
        tv_angle.setLayoutParams(k2);
        tv_angle.setText("Angle between North And FC: " + String.format("%.2f", pcImpl.getAngle() / Math.PI * 180) + " °" + System.getProperty("line.separator"));
        tv_angle.setTextColor(Color.WHITE);
        rl.addView(tv_angle);

        addText(rl, 0, 105, 770, 0, "Y", 14);
        addText(rl, 0, 280, 560, 0, "X", 14);

        addText(rl,0,119,654,0,"Y'", 14);
        addText(rl,0,375,565,0,"X'", 14);
        addText(rl,0,380,800,0,"Z Z'", 14);


        addText(rl,0,120,350,0,"[", 40);
        addText(rl,0,120,25,0,"]", 40);


        RelativeLayout.LayoutParams k3 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        k3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        k3.setMargins(0, 80, 50, 0);
        TextView tv_matrix = new TextView(getContext());
        tv_matrix.setLayoutParams(k3);
        String matrixStr = "Rotation Matrix:" +System.getProperty("line.separator") +getMatrixStr(pcImpl.getSpaceSyncMatrix());
        tv_matrix.setText(matrixStr);
        tv_matrix.setTextColor(Color.WHITE);
        rl.addView(tv_matrix);
        phonePane.addView(rl);
    }

    private void addText(RelativeLayout rl, int left, int top, int right, int bottom, String str, float size) {
        RelativeLayout.LayoutParams k4 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        k4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        k4.setMargins(left,top,right,bottom);
        TextView tv_x = new TextView(getContext());
        tv_x.setTextSize(size);
        tv_x.setLayoutParams(k4);
        tv_x.setText(str);
        tv_x.setTextColor(Color.WHITE);
        rl.addView(tv_x);
    }

    private String getMatrixStr(double[][] mat) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                sb.append(String.format("%.2f",mat[i][j]));
                if(j!=mat[0].length-1){
                    sb.append(" ");
                }
            }
            sb.append(System.getProperty("line.separator"));
        }
        return sb.toString();
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
