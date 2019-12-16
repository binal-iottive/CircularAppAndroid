package com.tofa.circular;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChartView;
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
import com.tofa.circular.nrfUARTv2.UartService;
import com.tofa.circular.sqldatabase.DatabaseHelper;
import com.tofa.circular.sqldatabase.DatabaseHelperTable;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.tofa.circular.customclass.Utils.insertDataArrayList;
import static java.lang.Thread.sleep;

public class ActivityAnalysisActivity extends AppCompatActivity implements FlexRadioGroup.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private LineChart lineChart;
    private StatusCardView scv_steps_taken, scv_walking_equivalency, scv_calories_burns, scv_active_minutes,
            scv_vo2_max, scv_hr_max;
    private TimeChartView chartViewActivityDuration;
    private LineChart mHRChart;
    private BarChart barChart;
    private AnyChartView waterfallChart;
    private NonScrollListView lv_chart_detail;
    private RadioGroup rg_activity_intensity;
    private FlexRadioGroup flexRadioGroup;
    private TextView tv_grapth_title;
    private String chartAction;
    private String chartType;
    private ImageView iv_hr_onoff;
    private TextView tv_live_hr;

    static ActivityAnalysisActivity instance;
    private DatabaseHelper db;

    public static ActivityAnalysisActivity getInstance() {
        return instance;
    }

    float[] hrList = new float[]{50, 70, 200, 120, 300, 160, 10, 140, 100, 70, 60, 50, 50, 70, 200, 120, 140, 160, 250, 140, 20, 70, 60, 50,
            50, 70, 100, 120, 140, 160, 250, 140, 100, 70, 60, 150, 50, 70, 100, 220, 140, 160, 160, 140, 100, 70, 60, 500,};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_analysis);

        db = new DatabaseHelper(this);
        initUI();
        ((ProgressCardView) findViewById(R.id.crdRecovery)).setValueFormatter(staticValueFormater("Medium"));
        ((ProgressCardView) findViewById(R.id.crdWakeupScore)).setValueFormatter(percentValueFormater(100));
        ((ProgressCardView) findViewById(R.id.crdHeartRate)).setValueFormatter(staticValueFormater("68 ms"));
        ((ProgressCardView) findViewById(R.id.crdRestingHeartRate)).setValueFormatter(staticValueFormater("62 bpm"));
        ((ProgressCardView) findViewById(R.id.crdSleepQuality)).setValueFormatter(percentValueFormater(100));
        ((ProgressCardView) findViewById(R.id.crdSleepBalance)).setValueFormatter(staticValueFormater("Excelent"));
        ((ProgressCardView) findViewById(R.id.crdActivityVolume)).setValueFormatter(staticValueFormater("Excelent"));

        Utils.startInsertDataService(ActivityAnalysisActivity.this);
        setLvhrOff();

//        insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_SPO2, "2019-12-01", Utils.getCurrentTime(),100));
//        insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_SPO2, "2019-12-02",Utils.getCurrentTime(),20));
//        insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_SPO2, "2019-12-03",Utils.getCurrentTime(),60));
//        insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_SPO2, "2019-12-04",Utils.getCurrentTime(),55));
//        insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_SPO2, "2019-12-05",Utils.getCurrentTime(),76));
//        insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_SPO2, "2019-12-06",Utils.getCurrentTime(),29));
//        insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_SPO2, "2019-12-07",Utils.getCurrentTime(),92));

//        insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_HR, "2019-12-13", Utils.getCurrentTime(),210));
//        insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_HR, "2019-12-13",Utils.getCurrentTime(),120));
//        insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_HR, "2019-12-13",Utils.getCurrentTime(),110));
//        insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_HR, "2019-12-13",Utils.getCurrentTime(),0));
//        insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_HR, "2019-12-13",Utils.getCurrentTime(),20));
//        insertDataArrayList.add(new DatabaseHelperTable(DatabaseHelperTable.TABLE_NAME_HR, "2019-12-13",Utils.getCurrentTime(),150));


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
        waterfallChart = findViewById(R.id.waterfallChart);
        rg_activity_intensity = findViewById(R.id.rg_activity_intensity);
        flexRadioGroup = findViewById(R.id.flexRadioGroup);
        tv_grapth_title = findViewById(R.id.tv_grapth_title);
        iv_hr_onoff = findViewById(R.id.iv_hr_onoff);
        lineChart = findViewById(R.id.lineChart);
        chartViewActivityDuration = findViewById(R.id.chartViewActivityDuration);
        tv_live_hr = findViewById(R.id.tv_live_hr);

        GraphUtils.loadHRGraph(mHRChart, ActivityAnalysisActivity.this);

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

        iv_hr_onoff.setOnClickListener(this);
//        addDataToChart(0);
    }

    public void addLiveData(String notifyValue) {
        if (notifyValue.contains("LVhr")) {
            float LVhr = GraphUtils.convertHrRawDataToChartData(notifyValue);
            addDataToChart(LVhr);
            tv_live_hr.setText(LVhr+"");
        } else {
            String filtered = notifyValue.substring(notifyValue.lastIndexOf("/") + 1);
            int value = Integer.parseInt(filtered, 16);
            String tableName = "";
            if (notifyValue.contains("Dstp")) {
                scv_steps_taken.setValue(value + "");
                tableName = DatabaseHelperTable.TABLE_NAME_STEPS;
            } else if (notifyValue.contains("Dwlk")) {
                float value1 = ((float) value) / 1000;
                scv_walking_equivalency.setValue(String.format("%.02f", value1));
                tableName = DatabaseHelperTable.TABLE_NAME_WALINKG;
            } else if (notifyValue.contains("Dca")) {
                scv_calories_burns.setValue(value + "");
                tableName = DatabaseHelperTable.TABLE_NAME_CALORIES;
            } else if (notifyValue.contains("Dact")) {
                scv_active_minutes.setValue(value + "");
                tableName = DatabaseHelperTable.TABLE_NAME_ACTIVE;
            } else if (notifyValue.contains("Dvoz")) {
                scv_vo2_max.setValue(value + "");
                tableName = DatabaseHelperTable.TABLE_NAME_VO2;
            } else if (notifyValue.contains("Dhrv")) {
//                scv_hr_max.setValue(value + "");
                tableName = DatabaseHelperTable.TABLE_NAME_HRV;
            } else if (notifyValue.contains("Dhr")) {
                scv_hr_max.setValue(value + "");
                tableName = DatabaseHelperTable.TABLE_NAME_HR;
            } else if (notifyValue.contains("Deng")) {
//                scv_hr_max.setValue(value + "");
                tableName = DatabaseHelperTable.TABLE_NAME_ENERGY_LEVEL;
            } else if (notifyValue.contains("Drhr")) {
//                scv_hr_max.setValue(value + "");
                tableName = DatabaseHelperTable.TABLE_NAME_RESTING_HR;
            } else if (notifyValue.contains("Dspo")) {
//                scv_hr_max.setValue(value + "");
                tableName = DatabaseHelperTable.TABLE_NAME_SPO2;
            }
            if (tableName.equals(DatabaseHelperTable.TABLE_NAME_SPO2)) {
                float value1 = ((float) value) / 100;
                insertDataArrayList.add(new DatabaseHelperTable(tableName, Utils.getCurrentDate(), Utils.getCurrentTime(), value1));
            }else if (tableName.equals(DatabaseHelperTable.TABLE_NAME_CALORIES)) {
                float value1 = ((float) value) / 1000;
                insertDataArrayList.add(new DatabaseHelperTable(tableName, Utils.getCurrentDate(), Utils.getCurrentTime(), value1));
            } else {
                insertDataArrayList.add(new DatabaseHelperTable(tableName, Utils.getCurrentDate(), Utils.getCurrentTime(), value));
            }
        }
    }

    public boolean writeLiveOnOff(String command) {
        if (MainActivity.mService != null && MainActivity.mDevice != null) {
            byte[] value = new byte[0];
            try {
                value = command.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            MainActivity.mService.writeRXCharacteristic(value);
            return true;
        }else {
            Toast.makeText(instance, getString(R.string.please_connect_device), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void addDataToChart(float hrValue) {
        GraphUtils.addValueToChart(mHRChart, hrValue, ActivityAnalysisActivity.this);
      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((int) hrValue<hrList.length) {
                    Log.d("addDataToChart==>",hrList[(int) hrValue]+"");
                    GraphUtils.addValueToChart(mHRChart, hrList[(int) hrValue], ActivityAnalysisActivity.this);
                    addDataToChart((int) hrValue + 1);
                }else {
                    addDataToChart(0);
                }
            }
        }, 200);*/
    }

    @Override
    protected void onDestroy() {
        instance = null;
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
        switch (radioGroup.getCheckedRadioButtonId()) {
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

        if (chartAction.equals(GraphUtils.CHART_ACTION_ACTITY_INTENSITY)) {
            addActivityInyensityGraph(chartType);
        } else if (chartAction.equals(GraphUtils.CHART_ACTION_CALORIES_BURN)) {
            addCaloriesBurnedGraph(chartType);
        } else if (chartAction.equals(GraphUtils.CHART_ACTION_HR_MAX)) {
            addHeartRateMaxGraph(chartType);
        } else if (chartAction.equals(GraphUtils.CHART_ACTION_HRV)) {
            addHRVGraph(chartType);
        } else if (chartAction.equals(GraphUtils.CHART_ACTION_ACTIVE_MINUTES)) {
            addActiveMinutesGraph(chartType);
        } else if (chartAction.equals(GraphUtils.CHART_ACTION_BOOT_STEPS)) {
            addNumberOfStpesGraph(chartType);
        } else if (chartAction.equals(GraphUtils.CHART_ACTION_ENERGY_LEVEL)) {
            addEnergyLevelGraph(chartType);
        } else if (chartAction.equals(GraphUtils.CHART_ACTION_RESTING_HR)) {
            addRestingHeartRateGraph(chartType);
        } else if (chartAction.equals(GraphUtils.CHART_ACTION_RESTING_SPO2)) {
            addRestingSPO2Graph(chartType);
        }
    }

    @Override
    public void onCheckedChanged(FlexRadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()) {
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
                addEnergyLevelGraph(chartType);
                break;

            case R.id.rbO3:
                tv_grapth_title.setText(R.string.number_of_steps);
                chartAction = GraphUtils.CHART_ACTION_BOOT_STEPS;
                setWeekMonthAllRadiotext();
                addNumberOfStpesGraph(chartType);
                break;

            case R.id.rbO4:
                tv_grapth_title.setText(R.string.resting_heart_rate);
                chartAction = GraphUtils.CHART_ACTION_RESTING_HR;
                setWeekMonthAllRadiotext();
                addRestingHeartRateGraph(chartType);
                break;

            case R.id.rb45:
                tv_grapth_title.setText(R.string.blood_oxygen_level);
                chartAction = GraphUtils.CHART_ACTION_RESTING_SPO2;
                setToadyWeekMonthRadioText();
                addRestingSPO2Graph(chartType);
                break;
        }
    }

    private void setToadyWeekMonthRadioText() {
        ((RadioButton) rg_activity_intensity.getChildAt(0)).setText(R.string.today);
        ((RadioButton) rg_activity_intensity.getChildAt(1)).setText(R.string.past_week);
        ((RadioButton) rg_activity_intensity.getChildAt(2)).setText(R.string.past_month);
    }

    private void setWeekMonthAllRadiotext() {
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

    private void addActivityInyensityGraph(String clickedtype) {
        if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_WEEK)) {
            lineChart.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(new ArrayList<Float>());
            BarChartUtils.loadBarChart(barChart, GraphUtils.CHART_TYPE_TODAYS, chartAction, new ArrayList<Float>(), averageValue, -1f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Low", "1 hrs 07 min", 0));
            modelArrayList.add(new DetailGraphDescriptionModel("Medium", "37 min", 0));
            modelArrayList.add(new DetailGraphDescriptionModel("High", "57 min", 0));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_MONTH)) {
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(new ArrayList<Float>());
            LineChartUtils.loadLineChart(lineChart, GraphUtils.CHART_ACTION_ACTITY_INTENSITY, GraphUtils.CHART_TYPE_PAST_WEEK, new ArrayList<Float>(), averageValue, -1f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Low", "1 hrs 07 min", R.color.colorYellow));
            modelArrayList.add(new DetailGraphDescriptionModel("Medium", "37 min", R.color.colorOrange));
            modelArrayList.add(new DetailGraphDescriptionModel("High", "57 min", R.color.colorDarkRed));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_ALL)) {
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(new ArrayList<Float>());
            LineChartUtils.loadLineChart(lineChart, GraphUtils.CHART_ACTION_ACTITY_INTENSITY, GraphUtils.CHART_TYPE_PAST_MONTH, new ArrayList<Float>(), averageValue, -1f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Low", "1 hrs 07 min", R.color.colorYellow));
            modelArrayList.add(new DetailGraphDescriptionModel("Medium", "37 min", R.color.colorOrange));
            modelArrayList.add(new DetailGraphDescriptionModel("High", "57 min", R.color.colorDarkRed));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
    }

    private void addHeartRateMaxGraph(String clickedtype) {
        if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_WEEK)) {
            ArrayList<Float> todayData = db.getTodaysData(DatabaseHelperTable.TABLE_NAME_HR);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(todayData);
            LineChartUtils.loadLineChart(lineChart, chartAction, GraphUtils.CHART_TYPE_TODAYS, todayData, averageValue, 76);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Day average", (int) Math.round(averageValue) + " bpm", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "76 bpm", R.color.colorAlphaBlue));
            modelArrayList.add(new DetailGraphDescriptionModel("HR max", (int) Math.round(getMaximumValue(todayData)) + " bpm", 0));
            modelArrayList.add(new DetailGraphDescriptionModel("HR min", (int) Math.round(getMinimumValue(todayData)) + " bpm", 0));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_MONTH)) {
            ArrayList<Float> weekData = db.getLastWeekData(DatabaseHelperTable.TABLE_NAME_HR);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(weekData);
            LineChartUtils.loadLineChart(lineChart, chartAction, GraphUtils.CHART_TYPE_PAST_WEEK, weekData, averageValue, 75);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average", (int) Math.round(averageValue) + " bpm", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "75 bpm", R.color.colorAlphaBlue));
            modelArrayList.add(new DetailGraphDescriptionModel("HR max", (int) Math.round(getMaximumValue(weekData)) + " bpm", 0));
            modelArrayList.add(new DetailGraphDescriptionModel("HR min", (int) Math.round(getMinimumValue(weekData)) + " bpm", 0));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_ALL)) {
            ArrayList<Float> monthData = db.getLastMonthData(DatabaseHelperTable.TABLE_NAME_HR);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(monthData);
            LineChartUtils.loadLineChart(lineChart, chartAction, GraphUtils.CHART_TYPE_PAST_MONTH, monthData, averageValue, 74);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Weekly average", (int) Math.round(averageValue) + " bpm", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "74 bpm", R.color.colorAlphaBlue));
            modelArrayList.add(new DetailGraphDescriptionModel("HR max", (int) Math.round(getMaximumValue(monthData)) + " bpm", 0));
            modelArrayList.add(new DetailGraphDescriptionModel("HR min", (int) Math.round(getMinimumValue(monthData)) + " bpm", 0));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
    }

    private void addCaloriesBurnedGraph(String clickedtype) {
        if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_WEEK)) {
            ArrayList<Float> weekData = db.getLastWeekDataSteps(DatabaseHelperTable.TABLE_NAME_CALORIES);
            lineChart.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(weekData);
            BarChartUtils.loadBarChart(barChart, clickedtype, chartAction, weekData, averageValue, 1.650f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average", String.format("%.01f", averageValue) + " kcal", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "1.650 kcal", R.color.colorAlphaBlue));
            modelArrayList.add(new DetailGraphDescriptionModel("Week total", getAllDataTotal(weekData) + " kcal", 0));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_MONTH)) {
            ArrayList<Float> monthData = db.getLastMonthDataSteps(DatabaseHelperTable.TABLE_NAME_CALORIES);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(monthData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, monthData, averageValue, 9.244f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Weekly average", String.format("%.01f", averageValue) + " kcal", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "9.244 kcal", R.color.colorAlphaBlue));
            modelArrayList.add(new DetailGraphDescriptionModel("Month total", getAllDataTotal(monthData) + " kcal", 0));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_ALL)) {
            ArrayList<Float> allData = db.getAllDailyData(DatabaseHelperTable.TABLE_NAME_CALORIES);
            float averageValue = getAverageValue(allData);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, allData, averageValue, -1f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Monthly average", String.format("%.01f", averageValue) + " kcal", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("All time total", getAllDataTotal(allData) + " kcal", 0));

            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
    }

    private void addHRVGraph(String clickedtype) {
        if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_WEEK)) {
            ArrayList<Float> weekData = db.getLastWeekData(DatabaseHelperTable.TABLE_NAME_HRV);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(weekData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, weekData, averageValue, 78f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average", (int) Math.round(averageValue) + " bpm", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "78 bpm", R.color.colorAlphaBlue));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_MONTH)) {
            ArrayList<Float> monthData = db.getLastMonthData(DatabaseHelperTable.TABLE_NAME_HRV);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(monthData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, monthData, averageValue, 78f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Weekly average", (int) Math.round(averageValue) + " bpm", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "78 bpm", R.color.colorAlphaBlue));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_ALL)) {
            ArrayList<Float> allData = db.getAllDailyData(DatabaseHelperTable.TABLE_NAME_HRV);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(allData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, allData, averageValue, -1f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Monthly average", (int) Math.round(averageValue) + " bpm", R.color.colorAlphaRed));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
    }

    private void addEnergyLevelGraph(String clickedtype) {
        if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_WEEK)) {
            ArrayList<Float> weekData = db.getLastWeekData(DatabaseHelperTable.TABLE_NAME_ENERGY_LEVEL);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(weekData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, weekData, averageValue, -1f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average", (int) Math.round(averageValue) + " %", R.color.colorAlphaRed));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_MONTH)) {
            ArrayList<Float> monthData = db.getLastMonthData(DatabaseHelperTable.TABLE_NAME_ENERGY_LEVEL);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(monthData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, monthData, averageValue, -1f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Weekly average", (int) Math.round(averageValue) + " %", R.color.colorAlphaRed));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_ALL)) {
            ArrayList<Float> allData = db.getAllDailyData(DatabaseHelperTable.TABLE_NAME_ENERGY_LEVEL);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(allData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, allData, averageValue, -1f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Monthly average", (int) Math.round(averageValue) + " %", R.color.colorAlphaRed));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
    }

    private void addActiveMinutesGraph(String clickedtype) {
        if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_WEEK)) {
            ArrayList<Float> weekData = db.getLastWeekData(DatabaseHelperTable.TABLE_NAME_ACTIVE);
            lineChart.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(weekData);
            BarChartUtils.loadBarChart(barChart, clickedtype, chartAction, weekData, averageValue, 121f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average", (int) Math.round(averageValue) + "", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "121", R.color.colorAlphaBlue));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_MONTH)) {
            ArrayList<Float> monthData = db.getLastMonthData(DatabaseHelperTable.TABLE_NAME_ACTIVE);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(monthData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, monthData, averageValue, 121f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Weekly average", (int) Math.round(averageValue) + "", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "121", R.color.colorAlphaBlue));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_ALL)) {
            ArrayList<Float> allData = db.getAllTableData(DatabaseHelperTable.TABLE_NAME_ACTIVE);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(allData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, allData, averageValue, 121f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Monthly average", (int) Math.round(averageValue) + "", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "121", R.color.colorAlphaBlue));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
    }

    private void addNumberOfStpesGraph(String clickedtype) {
        if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_WEEK)) {
            ArrayList<Float> weekData = db.getLastWeekDataSteps(DatabaseHelperTable.TABLE_NAME_STEPS);
            lineChart.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(weekData);
            BarChartUtils.loadBarChart(barChart, clickedtype, chartAction, weekData, averageValue, 10f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average", (int) Math.round(averageValue) + "", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "10000", R.color.colorAlphaBlue));
            modelArrayList.add(new DetailGraphDescriptionModel("Week Total", getAllDataTotal(weekData) + "", 0));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_MONTH)) {
            ArrayList<Float> monthData = db.getLastMonthDataSteps(DatabaseHelperTable.TABLE_NAME_STEPS);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(monthData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, monthData, averageValue, 70f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Weekly average", (int) Math.round(averageValue) + "", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "70000", R.color.colorAlphaBlue));
            modelArrayList.add(new DetailGraphDescriptionModel("Month Total", getAllDataTotal(monthData) + "", 0));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_ALL)) {
            ArrayList<Float> allData = db.getAllTableData(DatabaseHelperTable.TABLE_NAME_STEPS);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(allData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, allData, averageValue, -1f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("monthly average", (int) Math.round(averageValue) + "", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("All time total", getAllDataTotal(allData) + "", 0));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
    }

    private void addRestingHeartRateGraph(String clickedtype) {
        if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_WEEK)) {
            ArrayList<Float> weekData = db.getLastWeekData(DatabaseHelperTable.TABLE_NAME_RESTING_HR);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(weekData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, weekData, averageValue, 64f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average", (int) Math.round(averageValue) + " bpm", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "64 bpm", R.color.colorAlphaBlue));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_MONTH)) {
            ArrayList<Float> monthData = db.getLastMonthData(DatabaseHelperTable.TABLE_NAME_RESTING_HR);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(monthData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, monthData, averageValue, 64f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Weekly average", (int) Math.round(averageValue) + " bpm", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "64 bpm", R.color.colorAlphaBlue));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_ALL)) {
            ArrayList<Float> allData = db.getAllDailyData(DatabaseHelperTable.TABLE_NAME_RESTING_HR);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(allData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, allData, averageValue, -1f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Monthly average", (int) Math.round(averageValue) + " bpm", R.color.colorAlphaRed));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
    }


    private void addRestingSPO2Graph(String clickedtype) {
//        GraphUtils.loadWaterFallChart(waterfallChart);
//        barChart.setVisibility(View.GONE);
//        lineChart.setVisibility(View.GONE);
//        waterfallChart.setVisibility(View.VISIBLE);
        if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_WEEK)) {
            ArrayList<Float> weekData = db.getLastWeekData(DatabaseHelperTable.TABLE_NAME_SPO2);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(weekData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, weekData, averageValue, 92f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Day average", (int) Math.round(averageValue)+" %", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "92 %", R.color.colorAlphaBlue));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_PAST_MONTH)) {
            ArrayList<Float> monthData = db.getLastMonthData(DatabaseHelperTable.TABLE_NAME_SPO2);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(monthData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, monthData, averageValue, 92f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Daily average", (int) Math.round(averageValue)+" %", R.color.colorAlphaRed));
            modelArrayList.add(new DetailGraphDescriptionModel("Baseline", "92 %", R.color.colorAlphaBlue));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        } else if (clickedtype.equals(GraphUtils.CHART_TYPE_ALL)) {
            ArrayList<Float> allData = db.getAllDailyData(DatabaseHelperTable.TABLE_NAME_SPO2);
            barChart.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
            float averageValue = getAverageValue(allData);
            LineChartUtils.loadLineChart(lineChart, chartAction, clickedtype, allData, averageValue, -1f);
            ArrayList<DetailGraphDescriptionModel> modelArrayList = new ArrayList<>();
            modelArrayList.add(new DetailGraphDescriptionModel("Weekly average", (int) Math.round(averageValue)+" %", R.color.colorAlphaRed));
            ActivityDetailGraphDescriptionAdapter mAdapter = new ActivityDetailGraphDescriptionAdapter(ActivityAnalysisActivity.this, modelArrayList, chartAction);
            lv_chart_detail.setAdapter(mAdapter);
        }
    }

    private float getAverageValue(ArrayList<Float> entrylist) {
        float averageValue = 0;
        float sum = 0;
        for (int index = 0; index < entrylist.size(); index++) {
            sum = sum + entrylist.get(index);
        }
        averageValue = sum / entrylist.size();
        return averageValue;
    }

    private String getAllDataTotal(ArrayList<Float> entrylist) {
        float sum = 0;
        for (float item : entrylist) {
            sum = sum + item;
        }
        return String.format("%.01f", sum);
    }

    private float getMaximumValue(ArrayList<Float> entrylist) {
        float maximumValue = 0;
        for (int index = 0; index < entrylist.size(); index++) {
            if (entrylist.get(index) > maximumValue) {
                maximumValue = entrylist.get(index);
            }
        }
        return maximumValue;
    }

    private float getMinimumValue(ArrayList<Float> entrylist) {
        float minimumValue = 0;
        for (int index = 0; index < entrylist.size(); index++) {
            if (index == 0) {
                minimumValue = entrylist.get(index);
            }
            if (entrylist.get(index) < minimumValue) {
                minimumValue = entrylist.get(index);
            }
        }
        return minimumValue;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_hr_onoff:
                if (iv_hr_onoff.getTag().equals("Off")){
                    if (writeLiveOnOff("LV"))   setLvhrOn();
                }else {
                    if (writeLiveOnOff("lv"))  setLvhrOff();
                }
                break;
        }
    }

    private void setLvhrOn(){
        iv_hr_onoff.setImageResource(R.drawable.pause_button);
        iv_hr_onoff.setTag("On");
    }

    private void setLvhrOff(){
        iv_hr_onoff.setImageResource(R.drawable.play_button);
        iv_hr_onoff.setTag("Off");
    }
}
