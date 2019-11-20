package com.tofa.circular;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.tofa.circular.customclass.ProgressCardView;
import com.tofa.circular.customclass.StatusCardView;
import com.tofa.circular.customclass.TimeChartData;
import com.tofa.circular.customclass.TimeChartDataSeparatorType;
import com.tofa.circular.customclass.TimeChartView;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class ActivityAnalysisActivity extends AppCompatActivity {
    private LineChart sleepAnalysisChart;
    private StatusCardView scv_steps_taken, scv_walking_equivalency, scv_calories_burns, scv_active_minutes,
            scv_vo2_max, scv_hr_max;
    private TimeChartView chartViewActivityDuration;

    static ActivityAnalysisActivity instance;
    public static ActivityAnalysisActivity getInstance()
    {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_analysis);

        initUI();
        ((ProgressCardView) findViewById(R.id.crdRecovery)).setValueFormatter(staticValueFormater("Medium"));
        ((ProgressCardView) findViewById(R.id.crdWakeupScore)).setValueFormatter(percentValueFormater(100));
        ((ProgressCardView) findViewById(R.id.crdHeartRate)).setValueFormatter(staticValueFormater("68 ms"));
        ((ProgressCardView) findViewById(R.id.crdRestingHeartRate)).setValueFormatter(staticValueFormater("62 bpm"));
        ((ProgressCardView) findViewById(R.id.crdSleepQuality)).setValueFormatter(percentValueFormater(100));
        ((ProgressCardView) findViewById(R.id.crdSleepBalance)).setValueFormatter(staticValueFormater("Excelent"));
        ((ProgressCardView) findViewById(R.id.crdActivityVolume)).setValueFormatter(staticValueFormater("Excelent"));

        //new AsyncCaller().execute();
    }

    private void initUI() {
        scv_steps_taken = findViewById(R.id.scv_steps_taken);
        scv_walking_equivalency = findViewById(R.id.scv_walking_equivalency);
        scv_calories_burns = findViewById(R.id.scv_calories_burns);
        scv_active_minutes = findViewById(R.id.scv_active_minutes);
        scv_vo2_max = findViewById(R.id.scv_vo2_max);
        scv_hr_max = findViewById(R.id.scv_hr_max);

        sleepAnalysisChart = findViewById(R.id.lineSleep);
        sleepAnalysisChart.getDescription().setEnabled(false);
        sleepAnalysisChart.setDrawGridBackground(false);

        chartViewActivityDuration = findViewById(R.id.chartViewActivityDuration);
        ArrayList<TimeChartData> clockPieHelperArrayList = new ArrayList<>();
        clockPieHelperArrayList.add(createData(11,5,13,5,
                TimeChartDataSeparatorType.BOTH,
                "Activity start", "Activity end"));
        chartViewActivityDuration.setDate(clockPieHelperArrayList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;

    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    private class AsyncCaller extends AsyncTask<Void, Integer, Void>
    {
        LineChart theChart = ActivityAnalysisActivity.this.sleepAnalysisChart;

        @Override
        protected Void doInBackground(Void... params) {
            for ( int i=0; i<1000; i++ )
            {

                try {
                    Random rand = new Random();

                    int n = rand.nextInt(70000);
                    publishProgress(n);
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... n) {
            //ActivityAnalysisActivity.this.addDataSleep(n[0]);
            // send to uart
            //MainActivity.getInstance().mService.sendBroadcast();
            //String HexStr = String.format("%02X", n[0]);
            String HexStr = Integer.toHexString(n[0]);
            System.out.println(n[0]);
            int needZero = 6-HexStr.length();
            for ( int i=0; i<needZero; i++ )
            {
                HexStr = "0"+HexStr;
            }
            HexStr = "R"+HexStr;
            if ( MainActivity.getInstance() != null && MainActivity.getInstance().mService != null )
                MainActivity.getInstance().mService.forceExtra(HexStr.getBytes());
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

    public void addDataSleep(Integer n)
    {
        LineData data = sleepAnalysisChart.getData();

        if (data == null) {
            data = new LineData();
            sleepAnalysisChart.setData(data);
        }

        ILineDataSet set = data.getDataSetByIndex(0);

        if (set == null) {
            set = new LineDataSet(null, "DataSet 1");
            //set.setLineWidth(2.5f);
            //set.setCircleRadius(4.5f);
            //set.setColor(Color.rgb(240, 99, 99));
            //set.setCircleColor(Color.rgb(240, 99, 99));
            //set.setHighLightColor(Color.rgb(190, 190, 190));
            //set.setAxisDependency(AxisDependency.LEFT);
            set.setValueTextSize(10f);
            data.addDataSet(set);
        }

        // choose a random dataSet
        int randomDataSetIndex = (int) (Math.random() * data.getDataSetCount());
        ILineDataSet randomSet = data.getDataSetByIndex(randomDataSetIndex);
        float value = (float) (Math.random() * 50) + 50f * (randomDataSetIndex + 1);

        data.addEntry(new Entry(randomSet.getEntryCount(), n), randomDataSetIndex);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        sleepAnalysisChart.notifyDataSetChanged();

        sleepAnalysisChart.setVisibleXRangeMaximum(6);
        //chart.setVisibleYRangeMaximum(15, AxisDependency.LEFT);
//
//            // this automatically refreshes the chart (calls invalidate())
        sleepAnalysisChart.moveViewTo(data.getEntryCount() - 7, 50f, YAxis.AxisDependency.LEFT);

        /*
        LineChart lineChart = findViewById(R.id.lineSleep);
        System.out.println(entries.size());
        entries.add(new Entry(entries.size()-1,n));
       // lineChart.getLineData().getDataSets().get(0).addEntry(new Entry(lineChart.getLineData().getEntryCount(),n));
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
        */
    }
    private TimeChartData createData(int startHour, int startMin, int endHour, int endMin,
                                     TimeChartDataSeparatorType separatorType,
                                     String startLabel, String endLabel) {
        TimeChartData chartData = new TimeChartData(startHour, startMin, endHour, endMin, separatorType);
        chartData.setStartLabelText(startLabel);
        chartData.setEndLabelText(endLabel);
        return chartData;
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
}
