package com.tofa.circular;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.tofa.circular.customclass.ProgressCardView;
import com.tofa.circular.customclass.TimeChartData;
import com.tofa.circular.customclass.TimeChartDataSeparatorType;
import com.tofa.circular.customclass.TimeChartView;

import java.util.ArrayList;
import java.util.List;

public class SleepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        TimeChartView chartView = findViewById(R.id.chartSleep);

        ArrayList<TimeChartData> clockPieHelperArrayList = new ArrayList<>();
        clockPieHelperArrayList.add(createData(0,30,7,12,
                TimeChartDataSeparatorType.END,
                "", "Wake up"));
        clockPieHelperArrayList.add(createData(14,5,16,1,
                TimeChartDataSeparatorType.BOTH,
                "Nap start", "Nap end"));
        clockPieHelperArrayList.add(createData(20,43,20,45,
                TimeChartDataSeparatorType.START,
                "Start of lying", ""));
        clockPieHelperArrayList.add(createData(23,5,0,0,
                TimeChartDataSeparatorType.START,
                "Sleep start", ""));
        chartView.setDate(clockPieHelperArrayList);

        ProgressCardView pgDisturb = findViewById(R.id.crdDisturbance);
        int max = pgDisturb.getProgressMax();
        ProgressCardView.ProgressValueFormater valueFormater = new ProgressCardView.ProgressValueFormater(){
            @Override
            public String getFormattedValue(int value) {
                @SuppressLint("DefaultLocale") String percent = String.format("%.0f",(float) (max - value) /  max * 100);
                return value + " min (" + percent + "%)";
            }
        };

        pgDisturb.setValueFormatter(valueFormater);

        ((ProgressCardView) findViewById(R.id.crdRealSleep)).setValueFormatter(staticValueFormater("8 h 18 min (92%"));

        ((ProgressCardView) findViewById(R.id.crdTranq)).setValueFormatter(staticValueFormater("Agitated"));

        ((ProgressCardView) findViewById(R.id.crdCircadian)).setValueFormatter(staticValueFormater("Excellent"));

        ((ProgressCardView) findViewById(R.id.crdREM)).setValueFormatter(percentValueFormater(100));

        ((ProgressCardView) findViewById(R.id.crdDeepSleep)).setValueFormatter(percentValueFormater(100));

        ((ProgressCardView) findViewById(R.id.crdTimeFall)).setValueFormatter(staticValueFormater("22 min"));

        ((ProgressCardView) findViewById(R.id.crdSleepDebt)).setValueFormatter(staticValueFormater("-11 min"));

        LineChart chart = findViewById(R.id.lineChart);
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 3));
        entries.add(new Entry(1, 1));
        entries.add(new Entry(2, 0));
        entries.add(new Entry(4, 1));
        entries.add(new Entry(6, 0));
        entries.add(new Entry(7, 1));
        entries.add(new Entry(8, 0));
        entries.add(new Entry(10, 1));
        entries.add(new Entry(12, 3));
        entries.add(new Entry(14, 1));
        entries.add(new Entry(15, 2));
        entries.add(new Entry(17, 1));
        entries.add(new Entry(19, 2));
        entries.add(new Entry(21, 1));
        entries.add(new Entry(22, 0));
        entries.add(new Entry(23, 1));
        entries.add(new Entry(25, 3));

        LineDataSet dataSet = new LineDataSet(entries, "");
        LineData lineData = new LineData(dataSet);

        dataSet.setDrawValues(false);
        dataSet.setLineWidth(5);
        dataSet.setHighlightEnabled(false);
        dataSet.setDrawCircles(false);
        dataSet.setMode(LineDataSet.Mode.STEPPED);
        dataSet.setDrawHighlightIndicators(false);

        chart.getLegend().setEnabled(false);
        chart.getDescription().setText("");

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setYOffset(10);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(2);
        xAxis.setLabelCount(5);

        xAxis.setValueFormatter(new ValueFormatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f AM", value);
            }
        });

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setDrawLabels(true);
        axisLeft.setGranularityEnabled(true);
        axisLeft.setAxisMinimum(0);
        axisLeft.setAxisMaximum(3);
        axisLeft.setGranularity(1);
        axisLeft.setLabelCount(4, true);
        axisLeft.setTextColor(getResources().getColor(R.color.colorBlack));
        axisLeft.setTextSize(10);

        YAxis axisRight = chart.getAxisRight();
        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);


        axisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String rst = "";
                switch ((int) value + ""){
                    case "0":
                        rst = "Deep";
                        break;
                    case "1":
                        rst = "Light";
                        break;
                    case "2":
                        rst = "REM";
                        break;
                    case "3":
                        rst = "Awake ";
                        break;
                }
                return rst;
            }
        });

        chart.setPinchZoom(false);
        chart.setExtraLeftOffset(20);
        chart.setDrawBorders(false);
        chart.setDrawGridBackground(false);

        Paint paint = chart.getRenderer().getPaintRender();
        int height = 350;
        LinearGradient lingrad = new LinearGradient(0,0,0,height,
                new int[] {
                        Color.parseColor("#19D947"),
                        Color.parseColor("#1830AE"),
                },
                new float[] {
                        0, 1
                },
                Shader.TileMode.CLAMP);
        paint.setShader(lingrad);

        chart.setHardwareAccelerationEnabled(true);
        chart.setData(lineData);
        chart.invalidate();

        chartView.setOnClickListener(view -> {
            startActivity(new Intent(this, EditLogsActivity.class));
        });
    }

    private ProgressCardView.ProgressValueFormater staticValueFormater(String text) {
        ProgressCardView.ProgressValueFormater valueFormater = new ProgressCardView.ProgressValueFormater(){
            @Override
            public String getFormattedValue(int value) {
                return text;
            }
        };
        return valueFormater;
    }

    private ProgressCardView.ProgressValueFormater percentValueFormater(int max) {
        ProgressCardView.ProgressValueFormater valueFormater = new ProgressCardView.ProgressValueFormater(){
            @Override
            public String getFormattedValue(int value) {
                @SuppressLint("DefaultLocale") String percent = String.format("%.0f",(float) value /  max * 100);
                return percent + "%";
            }
        };
        return valueFormater;
    }

    private TimeChartData createData(int startHour, int startMin, int endHour, int endMin,
                                        TimeChartDataSeparatorType separatorType,
                                        String startLabel, String endLabel) {
        TimeChartData chartData = new TimeChartData(startHour, startMin, endHour, endMin, separatorType);
        chartData.setStartLabelText(startLabel);
        chartData.setEndLabelText(endLabel);
        return chartData;
    }
}
