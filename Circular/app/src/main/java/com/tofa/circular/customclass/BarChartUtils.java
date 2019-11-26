package com.tofa.circular.customclass;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import java.util.ArrayList;

public class BarChartUtils {

    public static void loadBarChart(BarChart mChart, String type) {
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
        if (type.equals("Todays")){
//            setAxisLabelsToday(mChart);
            mChart.setVisibleXRangeMinimum(90);
            addValueToChart(mChart,90,100, "Todays");
        }
        if (type.equals("week")){
            mChart.setVisibleXRangeMinimum(7);
            setAxisLabelsWeeks(mChart);
            addValueToChart(mChart,7,2, "week");
        }
        mChart.notifyDataSetChanged();
        mChart.invalidate();

    }

    public static void setAxisLabelsToday(BarChart mChart){
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("10h");
        xAxisLabel.add("12h");
        xAxisLabel.add("14h");
        xAxisLabel.add("16h");
        xAxisLabel.add("18h");
        xAxisLabel.add("20h");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setLabelCount(6,true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return  xAxisLabel.get((int) value / (90/5));
            }
        });

        final ArrayList<String> yAxisLabel = new ArrayList<>();
        yAxisLabel.add("");
        yAxisLabel.add("Low");
        yAxisLabel.add("Medium");
        yAxisLabel.add("High");
        yAxisLabel.add("HR max");
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaximum(0f);
        leftAxis.setAxisMaximum(120f);
        leftAxis.setLabelCount(5, true);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return  yAxisLabel.get((int) value / (120/4));
            }
        });
    }

    public static void setAxisLabelsWeeks(BarChart mChart){
       String[] xAxisLabel = new String[] {"S", "M", "T", "W", "T", "F", "S"};

        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisMaximum(7f);
        xAxis.setLabelCount(7,true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return  xAxisLabel[(int) value];
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaximum(0.5f);
        leftAxis.setAxisMaximum(2f);
        leftAxis.setGranularity(0.5f);
        leftAxis.setStartAtZero(false);
        leftAxis.setLabelCount(4, true);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return  value+"k";
            }
        });
    }

    public static void addValueToChart(BarChart mChart, int length, int range, String graphType) {
        mChart.setVisibleXRangeMaximum(length);
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int index = 0; index < length; index++)
            yVals1.add(new BarEntry(index + 0.3f, getRandom(range, 0)));

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
        }
        mChart.setData(data);
//        RoundedBarChartRenderer roundedBarChartRenderer= new RoundedBarChartRenderer(mChart,mChart.getAnimator(),mChart.getViewPortHandler());
//        roundedBarChartRenderer.setmRadius(20f);
//        mChart.setRenderer(roundedBarChartRenderer);
    }

    private static float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }
}
