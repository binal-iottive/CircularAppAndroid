package com.tofa.circular.customclass;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

import static com.tofa.circular.customclass.BarChartUtils.getRandom;
import static com.tofa.circular.customclass.GraphUtils.monthsLabel;
import static com.tofa.circular.customclass.GraphUtils.weeksLabel;

public class LineChartUtils {

    public static void loadLineChart(LineChart mChart, String actionType, String chartType) {
        LimitLine ll1 = new LimitLine(11f, "");
        ll1.setLineWidth(3f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(5f);
        ll1.setLineColor(Color.parseColor("#593996F7"));

        LimitLine ll2 = new LimitLine(13f, "");
        ll2.setLineWidth(3f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(5f);
        ll2.setLineColor(Color.parseColor("#59E00A0A"));

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawAxisLine(false);
        if (actionType.equals(GraphUtils.CHART_ACTION_CALORIES_BURN) || actionType.equals(GraphUtils.CHART_ACTION_HR_MAX)|| actionType.equals(GraphUtils.CHART_ACTION_HR_MAX)) {
            leftAxis.addLimitLine(ll1);
            leftAxis.addLimitLine(ll2);
        }else {
            leftAxis.removeAllLimitLines();
        }

        switch (actionType) {
            case GraphUtils.CHART_ACTION_ACTITY_INTENSITY:
                leftAxis.setAxisMinimum(0f);
                leftAxis.setGranularity(30f);
                leftAxis.setAxisMaximum(90f);// this replaces setStartAtZero(true)
                leftAxis.setLabelCount(4, true);
                break;

            case GraphUtils.CHART_ACTION_CALORIES_BURN:
                leftAxis.setAxisMinimum(0f);
                leftAxis.setGranularity(5f);
                leftAxis.setAxisMaximum(15f);// this replaces setStartAtZero(true)
                leftAxis.setLabelCount(4, true);
                break;

            case GraphUtils.CHART_ACTION_HR_MAX:
                leftAxis.setAxisMinimum(40f);
                leftAxis.setGranularity(30f);
                leftAxis.setAxisMaximum(130f);// this replaces setStartAtZero(true)
                leftAxis.setLabelCount(4, true);
                break;

            case GraphUtils.CHART_ACTION_HRV:
                leftAxis.setAxisMinimum(25f);
                leftAxis.setGranularity(25f);
                leftAxis.setAxisMaximum(100f);// this replaces setStartAtZero(true)
                leftAxis.setLabelCount(4, true);
                break;
        }

        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (actionType.equals(GraphUtils.CHART_ACTION_ACTITY_INTENSITY)){
                    return GraphUtils.yAxisLabelActiveMinutes[(int) value / 30];
                }else  if (actionType.equals(GraphUtils.CHART_ACTION_CALORIES_BURN)){
                    return (int) value+" k";
                }else if (actionType.equals(GraphUtils.CHART_ACTION_HR_MAX)){
                    if ((int) value == 40){
                        return "";
                    }
                    return (int) value+"";
                }else {
                    if ((int) value == 25){
                        return "";
                    }
                    return (int) value+"";
                }
            }
        });

        XAxis xAxis = mChart.getXAxis();
        if (chartType.equals(GraphUtils.CHART_TYPE_PAST_WEEK)) {
            xAxis.setLabelCount(7, true);
        } else if (chartType.equals(GraphUtils.CHART_TYPE_PAST_MONTH)) {
            xAxis.setLabelCount(4, true);
        }
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (chartType.equals(GraphUtils.CHART_TYPE_PAST_WEEK) && (int) value < weeksLabel.length) {
                    return weeksLabel[(int) value];
                } else if (chartType.equals(GraphUtils.CHART_TYPE_PAST_MONTH) && (int) value < monthsLabel.length) {
                    return monthsLabel[(int) value];
                } else {
                    return "";
                }
            }
        });

        mChart.setData(generateLineData(chartType,actionType));
        mChart.getLegend().setEnabled(false);
        mChart.setDescription(null);
        mChart.invalidate();
    }

    public static LineData generateLineData(String chartType, String actionType) {
        int itemcount = 7;
        if (chartType.equals(GraphUtils.CHART_TYPE_PAST_WEEK)) {
            itemcount = 7;
        } else if (chartType.equals(GraphUtils.CHART_TYPE_PAST_MONTH)) {
            itemcount = 4;
        }

        LineData d = new LineData();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        for (int index = 0; index < itemcount; index++)
            entries.add(new Entry(index + 0.5f, getRandom(15, 40)));

        LineDataSet set = new LineDataSet(entries, "");
        set.setColor(Color.parseColor("#E00A0A"));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.parseColor("#E00A0A"));
        set.setCircleRadius(5f);
        set.setCircleHoleColor(Color.parseColor("#E00A0A"));
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setDrawValues(false);
        set.setDrawIcons(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        if (actionType.equals(GraphUtils.CHART_ACTION_CALORIES_BURN)|| actionType.equals(GraphUtils.CHART_ACTION_HR_MAX)|| actionType.equals(GraphUtils.CHART_ACTION_HRV)) {
            return d;
        }

        ArrayList<Entry> entries1 = new ArrayList<Entry>();
        for (int index = 0; index < itemcount; index++)
            entries1.add(new Entry(index + 0.5f, getRandom(30, 30)));
        LineDataSet set1 = new LineDataSet(entries1, "");
        set1.setColor(Color.parseColor("#FC551A"));
        set1.setLineWidth(2.5f);
        set1.setCircleColor(Color.parseColor("#FC551A"));
        set1.setCircleRadius(5f);
        set1.setCircleHoleColor(Color.parseColor("#FC551A"));
        set1.setMode(LineDataSet.Mode.LINEAR);
        set1.setDrawValues(false);
        set1.setDrawIcons(false);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set1);

        ArrayList<Entry> entries2 = new ArrayList<Entry>();
        for (int index = 0; index < itemcount; index++)
            entries2.add(new Entry(index + 0.5f, getRandom(30, 60)));
        LineDataSet set2 = new LineDataSet(entries2, "");
        set2.setColor(Color.parseColor("#FAD505"));
        set2.setLineWidth(2.5f);
        set2.setCircleColor(Color.parseColor("#FAD505"));
        set2.setCircleRadius(5f);
        set2.setCircleHoleColor(Color.parseColor("#FAD505"));
        set2.setMode(LineDataSet.Mode.LINEAR);
        set2.setDrawValues(false);
        set2.setDrawIcons(false);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set2);
        return d;
    }

}
