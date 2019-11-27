package com.tofa.circular;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.hopenlib.flextools.FlexRadioGroup;
import com.tofa.circular.adapter.ActivityDetailGraphDescriptionAdapter;
import com.tofa.circular.customclass.BarChartUtils;
import com.tofa.circular.customclass.GraphUtils;
import com.tofa.circular.customclass.LineChartUtils;
import com.tofa.circular.customclass.NonScrollListView;
import com.tofa.circular.customclass.ProgressCardView;
import com.tofa.circular.customclass.StatusCardView;
import com.tofa.circular.customclass.TimeChartData;
import com.tofa.circular.customclass.TimeChartDataSeparatorType;
import com.tofa.circular.customclass.TimeChartView;
import com.tofa.circular.customclass.Utils;
import com.tofa.circular.model.DetailGraphDescriptionModel;
import com.tofa.circular.sqldatabase.DatabaseHelperTable;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.tofa.circular.customclass.Utils.insertDataArrayList;
import static java.lang.Thread.sleep;

public class ActivityAnalysisActivity extends AppCompatActivity implements FlexRadioGroup.OnCheckedChangeListener,RadioGroup.OnCheckedChangeListener{
    private LineChart lineChart;
    private StatusCardView scv_steps_taken, scv_walking_equivalency, scv_calories_burns, scv_active_minutes,
            scv_vo2_max, scv_hr_max;
    private TimeChartView chartViewActivityDuration;
    private LineChart mHRChart;
    private BarChart barChart;
    private NonScrollListView lv_chart_detail;
    private RadioGroup rg_activity_intensity;
    private FlexRadioGroup flexRadioGroup;
    private TextView tv_grapth_title;
    private String chartAction;
    private String chartType;

    static ActivityAnalysisActivity instance;

    public static ActivityAnalysisActivity getInstance() {
        return instance;
    }

    float[] hrList = new float[]{50, 70, 100, 120, 140, 160, 160, 140, 100, 70, 60, 50, 50, 70, 100, 120, 140, 160, 160, 140, 100, 70, 60, 50,
            50, 70, 100, 120, 140, 160, 160, 140, 100, 70, 60, 50, 50, 70, 100, 120, 140, 160, 160, 140, 100, 70, 60, 50,};


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

        writeLiveOnOff("LV");
        Utils.startInsertDataService(ActivityAnalysisActivity.this);
    }

    private void initUI() {
        scv_steps_taken = findViewById(R.id.scv_steps_taken);
        scv_walking_equivalency = findViewById(R.id.scv_walking_equivalency);
        scv_calories_burns = findViewById(R.id.scv_calories_burns);
        scv_active_minutes = findViewById(R.id.scv_active_minutes);
        scv_vo2_max = findViewById(R.id.scv_vo2_max);
        scv_hr_max = findViewById(R.id.scv_hr_max);
        lv_chart_detail = findViewById(R.id.lv_chart_detail);
        mHRChart = findViewById(R.id.mHRChart);
        barChart = findViewById(R.id.barChart);
        rg_activity_intensity = findViewById(R.id.rg_activity_intensity);
        flexRadioGroup = findViewById(R.id.flexRadioGroup);
        tv_grapth_title = findViewById(R.id.tv_grapth_title);
        GraphUtils.loadHRGraph(mHRChart, ActivityAnalysisActivity.this);

        lineChart = findViewById(R.id.lineChart);

        chartViewActivityDuration = findViewById(R.id.chartViewActivityDuration);
        ArrayList<TimeChartData> clockPieHelperArrayList = new ArrayList<>();
        clockPieHelperArrayList.add(createData(11, 5, 13, 5,
                TimeChartDataSeparatorType.BOTH,
                "Activity start", "Activity end"));
        chartViewActivityDuration.setDate(clockPieHelperArrayList);

        rg_activity_intensity.setOnCheckedChangeListener(this);
        flexRadioGroup.setOnCheckedChangeListener(this);
        chartAction = GraphUtils.CHART_ACTION_ACTITY_INTENSITY;
        chartType = GraphUtils.CHART_TYPE_PAST_WEEK;
        addActivityInyensityGraph(chartType);
    }

    public void addLiveData(String notifyValue) {
        if (notifyValue.contains("LVhr")) {
            addDataToChart(notifyValue);
        } else {
            String filtered = notifyValue.substring(notifyValue.lastIndexOf("/") + 1);
            int value = Integer.parseInt(filtered,16);
            String tableName = "";
            if (notifyValue.contains("Dstp")) {
                scv_steps_taken.setValue(value+"");
                tableName = DatabaseHelperTable.TABLE_NAME_STEPS;
            } else if (notifyValue.contains("Dwlk")) {
                value = value/100;
                scv_walking_equivalency.setValue(value+"");
                tableName = DatabaseHelperTable.TABLE_NAME_WALINKG;
            } else if (notifyValue.contains("Dcal")) {
                scv_calories_burns.setValue(value+"");
                tableName = DatabaseHelperTable.TABLE_NAME_CALORIES;
            } else if (notifyValue.contains("Dact")) {
                scv_active_minutes.setValue(value+"");
                tableName = DatabaseHelperTable.TABLE_NAME_ACTIVE;
            } else if (notifyValue.contains("Dvoz")) {
                scv_vo2_max.setValue(value+"");
                tableName = DatabaseHelperTable.TABLE_NAME_VO2;
            } else if (notifyValue.contains("Dhrl")) {
                scv_hr_max.setValue(value+"");
                tableName = DatabaseHelperTable.TABLE_NAME_HR;
            }
            insertDataArrayList.add(new DatabaseHelperTable(tableName,Utils.getCurrentDate(),Utils.getCurrentTime(),value+""));
        }
    }


    private void addCaloriesBurnGraph(String clickedtype){

    }

    public void writeLiveOnOff(String command){
        if ( MainActivity.mService != null && MainActivity.mDevice != null ) {
            byte[] value = new byte[0];
            try {
                value = command.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            MainActivity.mService.writeRXCharacteristic(value);
        }
    }

    public void addDataToChart(String hrValue) {
        GraphUtils.addValueToChart(mHRChart, GraphUtils.convertHrRawDataToChartData(hrValue), ActivityAnalysisActivity.this);
 /*       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (i<hrList.length) {
                    Log.d("addDataToChart==>",hrList[i]+"");
                    BarChartUtils.addValueToChart(barChart, hrList[i], ActivityAnalysisActivity.this);
                    addDataToChart(i + 1);
                }
            }
        }, 200);*/
    }

    @Override
    protected void onDestroy() {
        instance = null;
        writeLiveOnOff("lv");
        Utils.stopInsertDataService(ActivityAnalysisActivity.this);
        super.onDestroy();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.rbActivityToday:
                chartType = GraphUtils.CHART_TYPE_PAST_WEEK;
                break;

            case R.id.rbActivityWeek:
                chartType = GraphUtils.CHART_TYPE_PAST_MONTH;
                break;

            case R.id.rbActivityMonth:
                chartType = GraphUtils.CHART_TYPE_ALL;
                break;
        }

        if (chartAction.equals(GraphUtils.CHART_ACTION_ACTITY_INTENSITY)){
            addActivityInyensityGraph(chartType);
        }else  if (chartAction.equals(GraphUtils.CHART_ACTION_CALORIES_BURN)){
            addCaloriesBurnedGraph(chartType);
        }else  if (chartAction.equals(GraphUtils.CHART_ACTION_HR_MAX)){
            addHeartRateMaxGraph(chartType);
        }else  if (chartAction.equals(GraphUtils.CHART_ACTION_HRV)){
            addHRVGraph(chartType);
        }else  if (chartAction.equals(GraphUtils.CHART_ACTION_ACTIVE_MINUTES)){
            addActiveMinutesGraph(chartType);
        }
    }

    @Override
    public void onCheckedChanged(FlexRadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()){
            case R.id.rbActivityStages:
                tv_grapth_title.setText(R.string.activity_intensity);
                chartAction = GraphUtils.CHART_ACTION_ACTITY_INTENSITY;
                setToadyWeekMonthRadioText();
                addActivityInyensityGraph(chartType);
                break;

            case R.id.rbHeartRate:
                tv_grapth_title.setText(R.string.calories_burned);
                chartAction = GraphUtils.CHART_ACTION_CALORIES_BURN;
                setWeekMonthAllRadiotext();
                addCaloriesBurnedGraph(chartType);
                break;

            case R.id.rbHRV:
                tv_grapth_title.setText(R.string.heart_rate);
                chartAction = GraphUtils.CHART_ACTION_HR_MAX;
                setToadyWeekMonthRadioText();
                addHeartRateMaxGraph(chartType);
                break;

            case R.id.rbActivityQualityScore:
                tv_grapth_title.setText(R.string.heart_rate_variability);
                chartAction = GraphUtils.CHART_ACTION_HRV;
                setWeekMonthAllRadiotext();
                addHRVGraph(chartType);
                break;

            case R.id.rbHRS:
                tv_grapth_title.setText(R.string.active_monutes);
                chartAction = GraphUtils.CHART_ACTION_ACTIVE_MINUTES;
                setWeekMonthAllRadiotext();
                addActiveMinutesGraph(chartType);
                break;

            case R.id.rbO2:
                tv_grapth_title.setText(R.string.energy_levels);
                chartAction = GraphUtils.CHART_ACTION_ENERGY_LEVEL;
                setWeekMonthAllRadiotext();
                break;

            case R.id.rbO3:
                tv_grapth_title.setText(R.string.number_of_steps);
                chartAction = GraphUtils.CHART_ACTION_BOOT_STEPS;
                setWeekMonthAllRadiotext();
                break;

            case R.id.rbO4:
                tv_grapth_title.setText(R.string.resting_heart_rate);
                chartAction = GraphUtils.CHART_ACTION_RESTING_HR;
                setWeekMonthAllRadiotext();
                break;

            case R.id.rb45:
                tv_grapth_title.setText(R.string.blood_oxygen_level);
                chartAction = GraphUtils.CHART_ACTION_RESTING_SPO2;
                setToadyWeekMonthRadioText();
                break;
        }
    }

    private void setToadyWeekMonthRadioText(){
        ((RadioButton) rg_activity_intensity.getChildAt(0)).setText(R.string.today);
        ((RadioButton) rg_activity_intensity.getChildAt(1)).setText(R.string.past_week);
        ((RadioButton) rg_activity_intensity.getChildAt(2)).setText(R.string.past_month);
    }

    private void setWeekMonthAllRadiotext(){
        ((RadioButton) rg_activity_intensity.getChildAt(0)).setText(R.string.past_week);
        ((RadioButton) rg_activity_intensity.getChildAt(1)).setText(R.string.past_month);
        ((RadioButton) rg_activity_intensity.getChildAt(2)).setText(R.string.all);
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
        ProgressCardView.ProgressValueFormater valueFormater = new ProgressCardView.ProgressValueFormater() {
            @Override
            public String getFormattedValue(int value) {
                return text;
            }
        };
        return valueFormater;
    }

    private ProgressCardView.ProgressValueFormater percentValueFormater(int max) {
        ProgressCardView.ProgressValueFormater valueFormater = new ProgressCardView.ProgressValueFormater() {
            @Override
            public String getFormattedValue(int value) {
                @SuppressLint("DefaultLocale") String percent = String.format("%.0f", (float) value / max * 100);
                return percent + "%";
            }
        };
        return valueFormater;
    }

    private void addActivityInyensityGraph(String clickedtype){
        if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_WEEK)){
            lineChart.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);
            BarChartUtils.loadBarChart(barChart,GraphUtils.CHART_TYPE_TODAYS,chartAction);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Low","1 hrs 07 min",0));
            modelArrayList.add(new DetailGraphDescriptionModel("Medium","37 min",0));
            modelArrayList.add(new DetailGraphDescriptionModel("High","57 min",0));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }else if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_MONTH)){
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            LineChartUtils.loadLineChart(lineChart,GraphUtils.CHART_ACTION_ACTITY_INTENSITY,GraphUtils.CHART_TYPE_PAST_WEEK);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Low","1 hrs 07 min",R.color.colorYellow));
            modelArrayList.add(new DetailGraphDescriptionModel("Medium","37 min",R.color.colorOrange));
            modelArrayList.add(new DetailGraphDescriptionModel("High","57 min",R.color.colorDarkRed));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
        else if (clickedtype.equals(GraphUtils.CHART_TYPE_ALL)){
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            LineChartUtils.loadLineChart(lineChart,GraphUtils.CHART_ACTION_ACTITY_INTENSITY,GraphUtils.CHART_TYPE_PAST_MONTH);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Low","1 hrs 07 min",R.color.colorYellow));
            modelArrayList.add(new DetailGraphDescriptionModel("Medium","37 min",R.color.colorOrange));
            modelArrayList.add(new DetailGraphDescriptionModel("High","57 min",R.color.colorDarkRed));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
    }

    private void addHeartRateMaxGraph(String clickedtype){
        if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_WEEK)){
            lineChart.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);
            BarChartUtils.loadBarChart(barChart,GraphUtils.CHART_TYPE_TODAYS,chartAction);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("day average","70bpm",R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline","70bpm",R.color.colorAlphaBlue));
            modelArrayList.add(new DetailGraphDescriptionModel("HR max","70bpm",0));
            modelArrayList.add(new DetailGraphDescriptionModel("HR min","70bpm",0));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }else if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_MONTH)){
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            LineChartUtils.loadLineChart(lineChart,chartAction,GraphUtils.CHART_TYPE_PAST_WEEK);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average","70bpm",R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline","70bpm",R.color.colorAlphaBlue));
            modelArrayList.add(new DetailGraphDescriptionModel("HR max","70bpm",0));
            modelArrayList.add(new DetailGraphDescriptionModel("HR min","70bpm",0));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
        else if (clickedtype.equals(GraphUtils.CHART_TYPE_ALL)){
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            LineChartUtils.loadLineChart(lineChart,chartAction,GraphUtils.CHART_TYPE_PAST_MONTH);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Weekly average","70bpm",R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline","70bpm",R.color.colorAlphaBlue));
            modelArrayList.add(new DetailGraphDescriptionModel("HR max","70bpm",0));
            modelArrayList.add(new DetailGraphDescriptionModel("HR min","70bpm",0));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
    }

    private void addCaloriesBurnedGraph(String clickedtype){
        if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_WEEK)){
            lineChart.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);
            BarChartUtils.loadBarChart(barChart,clickedtype,chartAction);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average","1570 kcal",R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline","1570 kcal",R.color.colorAlphaBlue));
            modelArrayList.add(new DetailGraphDescriptionModel("Week total","1570 kcal",0));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }else if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_MONTH)){
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            LineChartUtils.loadLineChart(lineChart,chartAction,clickedtype);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average","1570 kcal",R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline","1570 kcal",R.color.colorAlphaBlue));
            modelArrayList.add(new DetailGraphDescriptionModel("Week total","1570 kcal",0));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
        else if (clickedtype.equals(GraphUtils.CHART_TYPE_ALL)){
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            LineChartUtils.loadLineChart(lineChart,chartAction,clickedtype);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Monthaly average","1570 kcal",R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("All time total","1570 kcal",0));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
    }
    private void addHRVGraph(String clickedtype){
        if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_WEEK)){
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            LineChartUtils.loadLineChart(lineChart,chartAction,clickedtype);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average","1570 bpm",R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline","1570 bpm",R.color.colorAlphaBlue));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }else if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_MONTH)){
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            LineChartUtils.loadLineChart(lineChart,chartAction,clickedtype);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average","1570 bpm",R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline","1570 bpm",R.color.colorAlphaBlue));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
        else if (clickedtype.equals(GraphUtils.CHART_TYPE_ALL)){
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            LineChartUtils.loadLineChart(lineChart,chartAction,clickedtype);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Monthaly average","1570 bpm",R.color.colorAlphaRed));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
    }

    private void addActiveMinutesGraph(String clickedtype){
        if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_WEEK)){
            lineChart.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);
            BarChartUtils.loadBarChart(barChart,clickedtype,chartAction);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average","110",R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline","121",R.color.colorAlphaBlue));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }else if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_MONTH)){
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            LineChartUtils.loadLineChart(lineChart,chartAction,clickedtype);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average","110",R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline","121",R.color.colorAlphaBlue));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
        else if (clickedtype.equals(GraphUtils.CHART_TYPE_ALL)){
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            LineChartUtils.loadLineChart(lineChart,chartAction,clickedtype);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average","110",R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline","121",R.color.colorAlphaBlue));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this,modelArrayList,chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
    }
}
