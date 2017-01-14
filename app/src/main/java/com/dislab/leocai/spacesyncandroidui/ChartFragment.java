package com.dislab.leocai.spacesyncandroidui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dislab.leocai.spacesync.core.ConsistantAccListener;
import com.dislab.leocai.spacesync.core.LinearAccListener;
import com.dislab.leocai.spacesync.core.SpaceSync;
import com.dislab.leocai.spacesync.ui.RealTimeChart;
import com.dislab.leocai.spacesyncandroidui.ui.RealTimeChartMPChart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leocai on 17-1-5.
 */
public class ChartFragment extends Fragment implements LinearAccListener, ConsistantAccListener {
    private SpaceSync spaceSync;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        spaceSync = ((MainActivity) getActivity()).getSpaceSync();
        return inflater.inflate(R.layout.frg_chart, container, false);
    }

    List<RealTimeChart> charts = new ArrayList<>();
    RealTimeChartMPChart fcCharts;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout ly = (LinearLayout) getView().findViewById(R.id.root);
        int clientsNum;
        if(spaceSync==null) {
            clientsNum = 3;
        }
        else {
            clientsNum = spaceSync.getClientsNum();
            spaceSync.setGlobalLinearAccListener(this);
            spaceSync.setConsistantAccListener(this);
        }
//        int clientsNum = 2;

        fcCharts = new RealTimeChartMPChart(getContext(), new String[]{"PC1"});
        ly.addView(fcCharts.getView());
        setChartStyle(fcCharts);


        for (int i = 0; i < clientsNum; i++) {
            RealTimeChartMPChart realTimeChart = new RealTimeChartMPChart(getContext(), new String[]{"X", "Y", "Z"});
            setChartStyle(realTimeChart);
            ly.addView(realTimeChart.getView());
            charts.add(realTimeChart);
        }


    }

    private void setChartStyle(RealTimeChartMPChart realTimeChart) {
        View view = realTimeChart.getView();
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }


    @Override
    public void dealWithClientGlobalAcc(final int clientId, final double[][] tracked_hori_lacc) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                charts.get(clientId).showStaticData(tracked_hori_lacc);
            }
        });
    }

    @Override
    public void dealWithConsistant(final double[] fc) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fcCharts.showStaticData(fc);
            }
        });
    }
}
