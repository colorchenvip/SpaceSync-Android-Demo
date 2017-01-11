package com.dislab.leocai.spacesyncandroidui.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dislab.leocai.spacesync.connection.DataServerMultiClient;
import com.dislab.leocai.spacesync.core.DirectionEstimateResults;
import com.dislab.leocai.spacesync.core.DirectionListener;
import com.dislab.leocai.spacesync.core.SpaceSync;
import com.dislab.leocai.spacesyncandroidui.R;
import com.dislab.leocai.spacesyncandroidui.test.MainActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by leocai on 17-1-9.
 */
public class DirectionFragment extends Fragment implements DirectionListener {


    private SpaceSync spaceSync;
    private List<DirectionUI> directionUIList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        spaceSync = ((MainActivity)getActivity()).getSpaceSync();
        spaceSync.setDataListener(new DataWriterListener());
        return inflater.inflate(R.layout.frg_chart, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout ly = (LinearLayout) getView().findViewById(R.id.root);
        int clientsNum = spaceSync.getClientsNum();
        ly.setWeightSum(clientsNum+1);
        spaceSync.setDirectionListener(this);
        directionUIList = new ArrayList<>(clientsNum);
        for (int i = 0; i < clientsNum; i++) {
            DirectionUI directionUI = new DirectionUI(getContext());
            directionUI.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            directionUIList.add(directionUI);
            ly.addView(directionUI);
        }

    }

    @Override
    public void dealWithDirection(DirectionEstimateResults directions, boolean isSyncTime) {
        double[][] axises = directions.getHoriFcDirection();
        double[][] magDirections = directions.getClientsMagDirections();
        double [] angle = new double[axises.length];
        for (int i = 0; i < axises.length; i++) {
            directionUIList.get(i).setV1(new float[]{(float) magDirections[i][0], (float) magDirections[i][2]});
            if(isSyncTime){
                directionUIList.get(i).setV2(new float[]{(float) axises[i][0], (float) axises[i][2]});
                angle[i] = directionUIList.get(i).computAngle();
            }
        }
        if(isSyncTime)
            write(angle);
    }

    FileWriter  fileWriter;
    {
        try {
            fileWriter = new FileWriter(new File(Environment.getExternalStorageDirectory(), "angle.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(double []angle) {
        try {
            String line="";
            for (int i = 0; i < angle.length; i++) {
                line += angle[i];
                if(i!=angle.length-1) line+=",";
            }
            fileWriter.write(line+"\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
