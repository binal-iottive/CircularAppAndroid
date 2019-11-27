package com.tofa.circular.customclass;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class GraphUtils {
    public static final String CHART_ACTION_ACTITY_INTENSITY = "CHART_ACTION_ACTITY_INTENSITY";
    public static final String CHART_ACTION_CALORIES_BURN = "CHART_ACTION_CALORIES_BURN";
    public static final String CHART_ACTION_HR_MAX = "CHART_ACTION_HR_MAX";
    public static final String CHART_ACTION_HRV= "CHART_ACTION_HRV";
    public static final String CHART_ACTION_ACTIVE_MINUTES= "CHART_ACTION_ACTIVE_MINUTES";
    public static final String CHART_ACTION_ENERGY_LEVEL= "CHART_ACTION_ENERGY_LEVEL";
    public static final String CHART_ACTION_BOOT_STEPS= "CHART_ACTION_BOOT_STEPS";
    public static final String CHART_ACTION_RESTING_HR= "CHART_ACTION_RESTING_HR";
    public static final String CHART_ACTION_RESTING_SPO2= "CHART_ACTION_RESTING_SPO2";
    public static final String CHART_TYPE_PAST_WEEK = "CHART_TYPE_PAST_WEEK";
    public static final String CHART_TYPE_PAST_MONTH = "CHART_TYPE_PAST_MONTH";
    public static final String CHART_TYPE_TODAYS = "CHART_TYPE_TODAYS";
    public static final String CHART_TYPE_ALL = "CHART_TYPE_ALL";
    public static final String[] weeksLabel = new String[] {"S", "M", "T", "W", "T", "F", "S"};
    public static final String[] monthsLabel = new String[] {"week1", "week2", "week3", "week4"};
    public static String[] yAxisLabelActiveMinutes= new String[]{"","30min","1h","1h 30"};
    private static float graphMinValueHr = 0;
    private static float graphMaxValueHr = 0;
    public static void loadHRGraph(LineChart mChart, Context context) {

        mChart.setNoDataText("");
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(false);
        mChart.setDescription(null);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        data.setDrawValues(false);

        mChart.setData(data);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(false);
        xAxis.setAxisMinValue(1);
        xAxis.setDrawAxisLine(false);
        xAxis.setAvoidFirstLastClipping(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinValue(40);
        yAxis.setAxisMaxValue(210);
        yAxis.setDrawLimitLinesBehindData(false);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawLabels(false);


        YAxis rightaxis = mChart.getAxisRight();
        rightaxis.setEnabled(false);
        graphMinValueHr = 40;
       graphMaxValueHr = 210;
    }

    public static void addValueToChart(LineChart mChart, float graphValue, Context context) {
        try {
            LineData data = mChart.getData();
            if (data != null) {
                ILineDataSet set = (LineDataSet) data.getDataSetByIndex(0);
                if (set == null) {
                    set = createSetAccelerometerChart(mChart, context);
                    data.addDataSet(set);
                }
                data.addEntry(new Entry(set.getEntryCount(), graphValue), 0);
                data.notifyDataChanged();

                try {
                    if (set.getEntryCount() >= 51) {
                        set.removeFirst();
                        for (int i=0; i<set.getEntryCount(); i++) {
                            Entry entryToChange = set.getEntryForIndex(i);
                            entryToChange.setX(entryToChange.getX() - 1);
                        }
                    }
                    mChart.notifyDataSetChanged();
                    mChart.setVisibleXRangeMaximum(50);
                    mChart.setVisibleXRangeMinimum(50);
                    setMinMaxtoChartHr(data, mChart,graphValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mChart.moveViewToX(data.getEntryCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LineDataSet createSetAccelerometerChart(LineChart mChart, Context context) {

        LineDataSet set = new LineDataSet(null, "");
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        //set.setCircleColor(Color.BLACK);
        set.setLineWidth(3f);
        set.setHighlightEnabled(false);
        set.setHighlightLineWidth(0f);
//        set.setFillFormatter();
        set.setDrawCircles(false);
        // set.setCircleColorHole(Color.BLACK);
//        set.setFillAlpha(65);
//        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(0f);
        set.setDrawValues(false);
        set.setColor(Color.parseColor("#E00A0A"));

        mChart.getLegend().setEnabled(false);
        return set;
    }

    public static void setMinMaxtoChartHr(LineData data, LineChart mChart, float graphValue) {
        if (data.getEntryCount() >= 51) {
            ILineDataSet entryList = data.getDataSets().get(0);
            graphMinValueHr = graphValue;
            graphMaxValueHr = graphValue;
            for (int i = 0; i <= 50; i++) {
                int entryValue = (int) entryList.getEntryForIndex(i).getY();
                if (entryValue < graphMinValueHr) {
                    graphMinValueHr = entryValue;
                }
                if (entryValue > graphMaxValueHr) {
                    graphMaxValueHr = entryValue;
                }

                if (i == data.getEntryCount()-1) {
                    mChart.getAxisLeft().setAxisMaxValue(graphMaxValueHr);
                    mChart.getAxisLeft().setAxisMinValue(graphMinValueHr);
                }
            }
        }else {
            if (graphValue < graphMinValueHr) {
                graphMinValueHr = graphValue;
            }
            if (graphValue > graphMaxValueHr) {
                graphMaxValueHr = graphValue;
            }

            mChart.getAxisLeft().setAxisMaxValue(graphMaxValueHr);
            mChart.getAxisLeft().setAxisMinValue(graphMinValueHr);
        }
    }

    public static float convertHrRawDataToChartData(String hrRawData){
        float result1 =0;
        hrRawData = hrRawData.replace("LVhr","");
        int hr = Integer.parseInt(hrRawData,16);
        result1 = hr;
        return result1;
    }

}
