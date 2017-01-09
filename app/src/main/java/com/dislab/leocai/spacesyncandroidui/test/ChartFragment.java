package com.dislab.leocai.spacesyncandroidui.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dislab.leocai.spacesync.connection.DataServerMultiClient;
import com.dislab.leocai.spacesync.core.ConsistantAccListener;
import com.dislab.leocai.spacesync.core.LinearAccListener;
import com.dislab.leocai.spacesync.core.SpaceSync;
import com.dislab.leocai.spacesync.ui.RealTimeChart;
import com.dislab.leocai.spacesyncandroidui.R;
import com.dislab.leocai.spacesyncandroidui.ui.RealTimeChartMPChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        int clientsNum = 2;
        ly.setWeightSum(clientsNum+1);

//        spaceSync.setGlobalLinearAccListener(this);
//        spaceSync.setConsistantAccListener(this);

        fcCharts = new RealTimeChartMPChart(getContext(), new String[]{"PC1"});
        double[] ks = new double[]{1, 2, 3, 2, 1, 10, 1, 2, 3, 2, 1, 10, 1, 2, 3, 2, 1, 10, 1, 2, 3, 2, 1, 10};
        ly.addView(fcCharts.getView());
        setChartStyle(fcCharts);
        fcCharts.showStaticData(ks);

        for (int i = 0; i < clientsNum; i++) {
            RealTimeChartMPChart realTimeChart = new RealTimeChartMPChart(getContext(), new String[]{"X", "Y", "Z"});
            setChartStyle(realTimeChart);
            ly.addView(realTimeChart.getView());
            charts.add(realTimeChart);
            double[][] ds = new double[][]{
                    {1,2,3},
                    {2,2,2},
                    {3,3,4},
                    {1,2,3},
                    {2,2,2},
                    {3,3,4},
                    {1,2,3},
                    {2,2,2},
                    {3,3,4}
            };
            realTimeChart.showStaticData(ds);
        }


    }

    private void setChartStyle(RealTimeChartMPChart realTimeChart) {
        View view = realTimeChart.getView();
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
    }


    @Override
    public void dealWithClientGlobalAcc(int clientId, double[][] tracked_hori_lacc) {
        charts.get(clientId).showStaticData(tracked_hori_lacc);
    }

    @Override
    public void dealWithConsistant(double[] fc) {
        fcCharts.showStaticData(fc);
    }
}
