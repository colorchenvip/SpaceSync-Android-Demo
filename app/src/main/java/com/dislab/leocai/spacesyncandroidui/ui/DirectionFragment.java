package com.dislab.leocai.spacesyncandroidui.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dislab.leocai.spacesync.core.DirectionEstimateResults;
import com.dislab.leocai.spacesync.core.DirectionListener;
import com.dislab.leocai.spacesync.core.SpaceSync;
import com.dislab.leocai.spacesyncandroidui.R;
import com.dislab.leocai.spacesyncandroidui.MainActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leocai on 17-1-9.
 */
public class DirectionFragment extends Fragment implements DirectionListener {


    private SpaceSync spaceSync;
    private List<DirectionUI> directionUIList;
    private EditText tv_expName;

    FileWriter fileWriterAngle;
    FileWriter fileWriterNorth;
    FileWriter fileWriterXAxis;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        spaceSync = ((MainActivity)getActivity()).getSpaceSync();

        return inflater.inflate(R.layout.frg_direction, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout ly = (LinearLayout) getView().findViewById(R.id.ly_ui);
        tv_expName = (EditText) getView().findViewById(R.id.tv_expName);
        getView().findViewById(R.id.btn_ready).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String file = tv_expName.getText().toString();
                if(file.equals("")) return;
                tv_expName.setEnabled(false);
                v.setEnabled(false);
                try {
                    File parent = new File(Environment.getExternalStorageDirectory() + "/" + file);
                    parent.deleteOnExit();
                    parent.mkdirs();
                    fileWriterAngle = new FileWriter(new File(Environment.getExternalStorageDirectory()+"/"+file, "angle.csv"));
                    fileWriterXAxis = new FileWriter(new File(Environment.getExternalStorageDirectory()+"/"+file, "xAxis.csv"));
                    fileWriterNorth = new FileWriter(new File(Environment.getExternalStorageDirectory()+"/"+file, "north.csv"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(spaceSync!=null){
                    spaceSync.setDataListener(new DataWriterListener(file));
                }
            }
        });
        int clientsNum;
        if(spaceSync==null){
            clientsNum = 3;
        }else{
            clientsNum = spaceSync.getClientsNum();
            spaceSync.setDirectionListener(this);
        }
        directionUIList = new ArrayList<>(clientsNum);
        for (int i = 0; i < clientsNum; i++) {
            DirectionUI directionUI = new DirectionUI(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 800, 1);
            lp.setMargins(20,20,5,5);
            directionUI.setLayoutParams(lp);
            directionUIList.add(directionUI);
            ly.addView(directionUI);
        }

    }

    @Override
    public void dealWithDirection(DirectionEstimateResults directions, boolean isSyncTime) {
        double[][] axises = directions.getHoriFcDirection();
        double[][] north = directions.getNorth();
        double [] angle = new double[axises.length];
        for (int i = 0; i < axises.length; i++) {
            directionUIList.get(i).setV1(north[i]);
            if(isSyncTime){
                directionUIList.get(i).setV2(axises[i]);
                angle[i] = directionUIList.get(i).computAngle();
            }
        }
        if(isSyncTime){
            write(fileWriterAngle,angle);
            write(fileWriterNorth,north);
            write(fileWriterXAxis,directions.getClientsInitXAxis());
        }
    }


    private void write(FileWriter fw,double[][] vectors) {
        if(fw==null){
            return;
        }
        try {
            String line="";
            for (int i = 0; i < vectors.length; i++) {
                for (int j = 0; j < 3; j++) {
                    line += vectors[i][j];
                    if(i!=(vectors.length-1)||(j!=2)) line+=",";
                }
            }
            fw.write(line+"\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void write(FileWriter fw, double[] angle) {
        if(fw ==null){
            return;
        }
        try {
            String line="";
            for (int i = 0; i < angle.length; i++) {
                line += angle[i];
                if(i!=angle.length-1) line+=",";
            }
            fw.write(line + "\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
