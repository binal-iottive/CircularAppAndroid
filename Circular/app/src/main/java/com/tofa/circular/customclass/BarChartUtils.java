package com.tofa.circular.customclass;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.tofa.circular.renderer.BarChartCustomRenderer;

import java.util.ArrayList;

import static com.tofa.circular.customclass.GraphUtils.weeksLabel;

public class BarChartUtils {
    public static void loadBarChart(BarChart mChart, String type, String chartAction, ArrayList<Float> entries, float averageValue, float baselinevalue) {
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setFocusable(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("10h");
        xAxisLabel.add("12h");
        xAxisLabel.add("14h");
        xAxisLabel.add("16h");
        xAxisLabel.add("18h");
        xAxisLabel.add("20h");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setAxisMaximum(90f);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(6,true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return  xAxisLabel.get((int) value / (90/5));
            }
        });

        mChart.getAxisLeft().setDrawAxisLine(false);
        mChart.getAxisRight().setDrawAxisLine(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getAxisRight().setDrawLabels(false);

        final ArrayList<String> yAxisLabel = new ArrayList<>();
        yAxisLabel.add("");
        yAxisLabel.add("Low");
        yAxisLabel.add("Medium");
        yAxisLabel.add("High");
        yAxisLabel.add("HR max");
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisMaximum(0f);
        leftAxis.setGranularity(1f);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setStartAtZero(true);
        leftAxis.setLabelCount(5, true);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return  yAxisLabel.get((int) value / (100/4));
            }
        });

        mChart.getLegend().setEnabled(false);
        mChart.setDescription(null);
        if (type.equals(GraphUtils.CHART_TYPE_TODAYS)){
//            setAxisLabelsToday(mChart);
            mChart.setVisibleXRangeMinimum(90);
            addValueToChart(mChart,90,100, "Todays",chartAction,entries);
        }
        if (type.equals(GraphUtils.CHART_TYPE_PAST_WEEK)){
            mChart.setVisibleXRangeMinimum(7);
            setAxisLabelsWeeks(mChart,chartAction,type,entries,averageValue, baselinevalue);
            addValueToChart(mChart,7,2, "week",chartAction,entries);
        }
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    public static void setAxisLabelsWeeks(BarChart mChart, String chartAction, String type, ArrayList<Float> entries, float averageValue, float baselinevalue){
        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisMaximum(7f);
        xAxis.setLabelCount(7,true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return  weeksLabel[(int) value];
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        if (chartAction.equals(GraphUtils.CHART_ACTION_ACTIVE_MINUTES)){
            leftAxis.setAxisMinimum(0f);
            leftAxis.setGranularity(50f);
            leftAxis.setAxisMaximum(150f);
            leftAxis.setLabelCount(4, true);
            leftAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    if ((int) value==0){
                        return "";
                    }
                    return  (int) value+"";
                }
            });
        }else if (chartAction.equals(GraphUtils.CHART_ACTION_BOOT_STEPS)){
            leftAxis.setAxisMinimum(0f);
            leftAxis.setGranularity(4f);
            leftAxis.setAxisMaximum(12f);
            leftAxis.setLabelCount(4, true);
            leftAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    if ((int) value==0){
                        return "";
                    }
                    return  (int) value+"k";
                }
            });
        }else {
            leftAxis.setAxisMinimum(0.5f);
            leftAxis.setGranularity(0.5f);
            leftAxis.setAxisMaximum(2f);
            leftAxis.setLabelCount(4, true);
            leftAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    if (value == 0.5){
                        return "";
                    }
                    return  value+"k";
                }
            });
        }

        leftAxis.removeAllLimitLines();
        if (chartAction.equals(GraphUtils.CHART_ACTION_CALORIES_BURN)
                || chartAction.equals(GraphUtils.CHART_ACTION_ACTIVE_MINUTES)
                || chartAction.equals(GraphUtils.CHART_ACTION_BOOT_STEPS)
                || chartAction.equals(GraphUtils.CHART_ACTION_RESTING_HR)
                || chartAction.equals(GraphUtils.CHART_ACTION_RESTING_SPO2)){
            LimitLine ll1 = new LimitLine(baselinevalue, "");
            ll1.setLineWidth(3f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(5f);
            ll1.setLineColor(Color.parseColor("#593996F7"));//baseLine

            LimitLine ll2 = new LimitLine(averageValue, "");
            ll2.setLineWidth(3f);
            ll2.enableDashedLine(10f, 10f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll2.setTextSize(5f);
            ll2.setLineColor(Color.parseColor("#59E00A0A"));

            leftAxis.addLimitLine(ll1);
            leftAxis.addLimitLine(ll2);
        }
    }

    public static void addValueToChart(BarChart mChart, int length, int range, String graphType, String chartAction, ArrayList<Float> entries) {
        mChart.setVisibleXRangeMaximum(length);
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int index = 0; index < entries.size(); index++) {
//            yVals1.add(new BarEntry(index + 0.3f, getRandom(range, 1)));

            if (chartAction.equals(GraphUtils.CHART_ACTION_BOOT_STEPS)){
                yVals1.add(new BarEntry(index + 0.3f, (entries.get(index))/1000));
            }else {
                yVals1.add(new BarEntry(index + 0.5f, (entries.get(index))));
            }
        }

        BarDataSet set1;
        set1 = new BarDataSet(yVals1, "");
        set1.setColor(Color.parseColor("#E00A0A"));
        set1.setDrawValues(false);
        set1.setDrawIcons(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        if (graphType.equals("week")){
            data.setBarWidth(0.3f);
            data.setDrawValues(true);
            data.setValueTextColor(Color.parseColor("#E00A0A"));
            if (chartAction.equals(GraphUtils.CHART_ACTION_CALORIES_BURN)) {
                data.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.format("%.1f", value) + " k";
                    }
                });
            }
        }
        mChart.setData(data);
        BarChartCustomRenderer barChartCustomRenderer = new BarChartCustomRenderer(mChart, mChart.getAnimator(), mChart.getViewPortHandler());
        barChartCustomRenderer.setRadius(15);
        mChart.setRenderer(barChartCustomRenderer);
    }

    public static float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }
}
