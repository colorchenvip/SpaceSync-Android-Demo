package com.dislab.leocai.spacesyncandroidui.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import com.dislab.leocai.spacesync.ui.RealTimeChart;
import com.dislab.leocai.spacesync.utils.SpaceSyncConfig;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leocai on 17-1-5.
 */
public class RealTimeChartMPChart implements RealTimeChart {

    private final LineChart chart;
    private final LineData lineData;
    private int cuKey;
    protected static final int MAX_X = 100;

    private final int[] colors = new int[]{Color.BLACK, Color.RED, Color.GREEN, Color.GRAY};

    public RealTimeChartMPChart(Context context, String[]columns, String title){
        chart = new LineChart(context);
        lineData = new LineData();
        int i=0;
        for(String column:columns){
            ArrayList<Entry> entries;
            entries = new ArrayList<>();
            entries.add(new Entry(0, 0));
            LineDataSet dataSet = new LineDataSet(entries, column); // add entries to dataset
            dataSet.setColor(colors[i++%colors.length]);
            dataSet.setDrawCircles(false);
            dataSet.setValueTextSize(0);
            lineData.addDataSet(dataSet);
        }
        Description ds= new Description();
        ds.setText(title);
        ds.setTextSize(16);
        chart.setDescription(ds);
        chart.setData(lineData);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setMinimumHeight(450);
        chart.getLegend().setTextSize(14);
        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMaximum(SpaceSyncConfig.BUFFER_SIZE);
        xAxis.setAxisMinimum(0);
        chart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        setRange(-10,10);
    }

    @Override
    public void addData(double[] data) {
        for (int i = 0; i < data.length; i++) {
            lineData.addEntry(new Entry(cuKey, (float)data[i]), i);
        }
        cuKey++;
//        if (cuKey >= MAX_X) {
//            for (int i = 0; i < data.length; i++) {
//                lineData.removeEntry(0,i);
//            }
//        }
    }

    @Override
    public void setRange(int min, int max) {
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(min);
        leftAxis.setAxisMaximum(max);
    }

    @Override
    public void showStaticData(double[] data) {
        clearData();
        for (double d : data) {
            addData(new double[] { d });
        }
        chart.getLineData().notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.postInvalidate();

    }

    @Override
    public void clearData() {
        cuKey = 0;
        List<ILineDataSet> sets = lineData.getDataSets();
        for(ILineDataSet set:sets){
            set.clear();
        }
    }

    @Override
    public void showStaticData(double[][] data) {
        clearData();
        for (double[] d : data) {
            addData(d);
        }
        chart.getLineData().notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.postInvalidate();
    }

    public View getView(){
        return chart;
    }
}
