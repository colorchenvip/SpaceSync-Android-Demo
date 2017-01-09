package com.dislab.leocai.spacesyncandroidui.ui;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.dislab.leocai.spacesync.ui.RealTimeChart;
import com.github.mikephil.charting.charts.LineChart;
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

    public RealTimeChartMPChart(Context context, String[]columns){
        chart = new LineChart(context);
        lineData = new LineData();
        for(String column:columns){
            ArrayList<Entry> entries;
            entries = new ArrayList<>();
            entries.add(new Entry(0, 0));
            LineDataSet dataSet = new LineDataSet(entries, column); // add entries to dataset
            lineData.addDataSet(dataSet);
        }
        chart.setData(lineData);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMaximum(MAX_X);
        xAxis.setAxisMinimum(0);
        chart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        setRange(-10,10);
    }

    @Override
    public void addData(double[] data) {
        cuKey++;
        for (int i = 0; i < data.length; i++) {
            lineData.addEntry(new Entry(cuKey, (float)data[i]), i);
        }
        if (cuKey >= MAX_X) {
            for (int i = 0; i < data.length; i++) {
                lineData.removeEntry(cuKey - MAX_X,i);
            }
        }
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
        chart.postInvalidate();
    }

    @Override
    public void clearData() {
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
        chart.postInvalidate();
    }

    public View getView(){
        return chart;
    }
}
