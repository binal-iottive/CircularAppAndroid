package com.tofa.circular;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.elconfidencial.bubbleshowcase.BubbleShowCase;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseListener;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.navigation.NavigationView;
import com.ramijemli.percentagechartview.PercentageChartView;
import com.tofa.circular.customclass.SaveSharedPreference;
import com.tofa.circular.customclass.SharedPref;
import com.tofa.circular.nrfUARTv2.UARTActivity;
import com.tofa.circular.nrfUARTv2.UartService;
import com.tofa.circular.renderer.BarChartCustomRenderer;
import com.tofa.circular.renderer.ColoredLabelXAxisRenderer;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static androidx.core.view.GravityCompat.START;
import static com.tofa.circular.nrfUARTv2.UARTActivity.UART_PROFILE_CONNECTED;
import static com.tofa.circular.nrfUARTv2.UARTActivity.UART_PROFILE_DISCONNECTED;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "nRFUARTMain";
    LinearLayout btnCircle, btnAlarm, btnSleep, btnActivity, btnAlert;
    ImageView imgCircle, imgAlarm, imgSleep, imgActivity, imgAlert;
    private CardView crdMainSleep, crdMainActivity;
    private RelativeLayout rl_activity_analysis_details, rl_sleep_analysis_detail;
    private LinearLayout ll_yesterday, llMainAlarm;
    private Button btn_yes_activity_recommand, btn_no_activity_recommand;
    private TextView tv_alarmTime, tv_alrm_toggle;

    public static UartService mService = null;
    public static BluetoothDevice mDevice = null;
    public static BluetoothAdapter mBtAdapter = null;

    private static MainActivity instance;

    public static MainActivity getInstance()
    {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        instance = this;
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorGrayBG));
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView lblClose = headerView.findViewById(R.id.lblHeaderMainClose);
        ImageView imgClose = headerView.findViewById(R.id.imgHeaderMainClose);

        lblClose.setOnClickListener(view -> drawer.closeDrawer(START));
        imgClose.setOnClickListener(view -> drawer.closeDrawer(START));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING) {
                    if (!drawer.isDrawerOpen(START)) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, android.R.color.transparent));
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= 23) {
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.colorGrayBG));
                        }
                    }
                }
                super.onDrawerStateChanged(newState);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        if(!SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            showIntro();
        }

        SaveSharedPreference.setLoggedIn(getApplicationContext(), true);

        BarChart barChart = findViewById(R.id.chartMainSleep);
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 20));
        entries.add(new BarEntry(1, 70));
        entries.add(new BarEntry(2, 90));
        entries.add(new BarEntry(3, 90));

        BarDataSet barDataSet = new BarDataSet(entries, "Disturbances");
        int[] barColors = new int[]{
                getResources().getColor(R.color.colorBlack),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimary)
        };
        barDataSet.setColors(barColors);
        barDataSet.setValueTextSize(12);
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int) value + " min";
            }
        });
        barDataSet.setHighlightEnabled(false);
        List<Integer> integerList = new ArrayList<>();
        integerList.add(getResources().getColor(R.color.colorBlack));
        integerList.add(getResources().getColor(R.color.colorPrimary));
        integerList.add(getResources().getColor(R.color.colorPrimary));
        integerList.add(getResources().getColor(R.color.colorPrimary));
        barDataSet.setValueTextColors(integerList);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.1f);
        barChart.setData(barData);

        final ArrayList<String> xLabel = new ArrayList<>();
        xLabel.add("Your Avg");
        xLabel.add("Mon");
        xLabel.add("Tues");
        xLabel.add("Wed");

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        xAxis.setGranularity(1);
        ValueFormatter valueFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xLabel.get((int) value);
            }
        };
        xAxis.setValueFormatter(valueFormatter);
        xAxis.setTextSize(12);

        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setDrawAxisLine(false);
        axisLeft.setAxisMaximum(100);
        axisLeft.setGranularityEnabled(true);
        axisLeft.setGranularity(20);
        axisLeft.setDrawLabels(false);

        YAxis axisRight = barChart.getAxisRight();
        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);

        ColoredLabelXAxisRenderer coloredLabelXAxisRenderer = new ColoredLabelXAxisRenderer(barChart.getViewPortHandler(), barChart.getXAxis(), barChart.getTransformer(YAxis.AxisDependency.LEFT), integerList);
        barChart.setXAxisRenderer(coloredLabelXAxisRenderer);

        Legend legend = barChart.getLegend();
        List<LegendEntry> legendEntries = new ArrayList<>();
        LegendEntry legendEntry = new LegendEntry();
        legendEntry.label = "Disturbances";
        legendEntry.formColor = ContextCompat.getColor(this, R.color.colorPrimary);
        legendEntries.add(legendEntry);
        legend.setCustom(legendEntries);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setDrawInside(false);
        legend.setFormSize(12);
        legend.setTextSize(12);
        legend.setFormToTextSpace(10);

        BarChartCustomRenderer barChartCustomRenderer = new BarChartCustomRenderer(barChart, barChart.getAnimator(), barChart.getViewPortHandler());
        barChartCustomRenderer.setRadius(20);
        barChart.setRenderer(barChartCustomRenderer);

        barChart.invalidate();
        barChart.getDescription().setText("");

        btnCircle = findViewById(R.id.btnMainAddCircle);
        btnAlarm = findViewById(R.id.btnMainAlarm);
        btnSleep = findViewById(R.id.btnMainSleepAnalysis);
        btnActivity = findViewById(R.id.btnMainActivityAnalysis);
        btnAlert = findViewById(R.id.btnMainAlert);

        imgCircle = findViewById(R.id.imgMainAddCircle);
        imgAlarm = findViewById(R.id.imgMainAlarm);
        imgSleep = findViewById(R.id.imgMainSleepAnalysis);
        imgActivity = findViewById(R.id.imgMainActivityAnalysis);
        imgAlert = findViewById(R.id.imgMainAlert);
        crdMainSleep = findViewById(R.id.crdMainSleep);
        crdMainActivity = findViewById(R.id.crdMainActivity);
        rl_activity_analysis_details = findViewById(R.id.rl_activity_analysis_details);
        rl_sleep_analysis_detail = findViewById(R.id.rl_sleep_analysis_detail);
        ll_yesterday = findViewById(R.id.ll_yesterday);
        btn_yes_activity_recommand = findViewById(R.id.btn_yes_activity_recommand);
        btn_no_activity_recommand = findViewById(R.id.btn_no_activity_recommand);

        final LinearLayout lytWeather = findViewById(R.id.lytMainWeather);

        LinearLayout llMainSleep = findViewById(R.id.llMainSleep);
        TextView tv_sleepmode = findViewById(R.id.tv_sleepmode);
        TextView tv_sleepmode_toggle = findViewById(R.id.tv_sleepmode_toggle);
        llMainSleep.setOnClickListener(view -> {
            int tag = Integer.parseInt(llMainSleep.getTag()+"") ;
            Drawable dwCircle = getResources().getDrawable(R.mipmap.ic_gear);
            Drawable dwAlarm = getResources().getDrawable(R.mipmap.ic_alarm);
            Drawable dwSleep = getResources().getDrawable(R.mipmap.ic_sleep);
            Drawable dwActivity = getResources().getDrawable(R.mipmap.ic_activity);
            Drawable dwAlert = getResources().getDrawable(R.mipmap.ic_alert);
            if (tag == 1){
                imgCircle.setImageDrawable(setSaturation(dwCircle, 1));
//                imgAlarm.setImageDrawable(setSaturation(dwAlarm, 1));
                imgSleep.setImageDrawable(setSaturation(dwSleep, 1));
                imgActivity.setImageDrawable(setSaturation(dwActivity, 1));
                imgAlert.setImageDrawable(setSaturation(dwAlert, 1));
                lytWeather.setBackgroundResource(R.drawable.bg_orange_gd_rounded);
                llMainSleep.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                tv_sleepmode_toggle.setTextColor(getResources().getColor(R.color.colorBlack));
                tv_sleepmode.setTextColor(getResources().getColor(R.color.colorBlack));
                tv_sleepmode_toggle.setText("off");
                llMainSleep.setTag(0);
            }else {
                imgCircle.setImageDrawable(setSaturation(dwCircle, 0));
//                imgAlarm.setImageDrawable(setSaturation(dwAlarm, 0));
                imgSleep.setImageDrawable(setSaturation(dwSleep, 0));
                imgActivity.setImageDrawable(setSaturation(dwActivity, 0));
                imgAlert.setImageDrawable(setSaturation(dwAlert, 0));
                lytWeather.setBackgroundResource(R.drawable.bg_ble_gd_rounded);
                llMainSleep.setBackgroundColor(getResources().getColor(R.color.colorDark));
                tv_sleepmode_toggle.setTextColor(getResources().getColor(R.color.white));
                tv_sleepmode.setTextColor(getResources().getColor(R.color.white));
                tv_sleepmode_toggle.setText("on");
                llMainSleep.setTag(1);
            }
        });
      /*  btnSleepToggle.setOnCheckedChangeListener((compoundButton, b) -> {
            Drawable dwCircle = getResources().getDrawable(R.mipmap.ic_gear);
            Drawable dwAlarm = getResources().getDrawable(R.mipmap.ic_alarm);
            Drawable dwSleep = getResources().getDrawable(R.mipmap.ic_sleep);
            Drawable dwActivity = getResources().getDrawable(R.mipmap.ic_activity);
            Drawable dwAlert = getResources().getDrawable(R.mipmap.ic_alert);
            if(b) {
                imgCircle.setImageDrawable(setSaturation(dwCircle, 0));
                imgAlarm.setImageDrawable(setSaturation(dwAlarm, 0));
                imgSleep.setImageDrawable(setSaturation(dwSleep, 0));
                imgActivity.setImageDrawable(setSaturation(dwActivity, 0));
                imgAlert.setImageDrawable(setSaturation(dwAlert, 0));
                lytWeather.setBackgroundResource(R.drawable.bg_ble_gd_rounded);
            } else {
                imgCircle.setImageDrawable(setSaturation(dwCircle, 1));
                imgAlarm.setImageDrawable(setSaturation(dwAlarm, 1));
                imgSleep.setImageDrawable(setSaturation(dwSleep, 1));
                imgActivity.setImageDrawable(setSaturation(dwActivity, 1));
                imgAlert.setImageDrawable(setSaturation(dwAlert, 1));
                lytWeather.setBackgroundResource(R.drawable.bg_orange_gd_rounded);
            }
        });*/

        llMainAlarm = findViewById(R.id.llMainAlarm);
        tv_alarmTime = findViewById(R.id.tv_alarmTime);
        tv_alrm_toggle = findViewById(R.id.tv_alrm_toggle);
        llMainAlarm.setOnClickListener(view -> {
            AlarmClockDialog(tv_alarmTime.getText().toString().trim()+"\n"+tv_alrm_toggle.getText().toString().trim());
        });

        btnCircle.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AddCircleActivity.class)));

        btnAlarm.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AlarmActivity.class)));

        btnSleep.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SleepActivity.class)));

        btnActivity.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ActivityAnalysisActivity.class)));

        btnAlert.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AlertActivity.class)));

        btn_no_activity_recommand.setOnClickListener(view -> {
            int tag = Integer.parseInt(btn_no_activity_recommand.getTag()+"") ;
            if (tag == 1){
              /*  btn_no_activity_recommand.setTextColor(getResources().getColor(R.color.colorOrange));
                btn_no_activity_recommand.setBackground(getResources().getDrawable(R.drawable.btn_small_orange_border));
                btn_no_activity_recommand.setTag(0);

                btn_yes_activity_recommand.setTextColor(getResources().getColor(R.color.white));
                btn_yes_activity_recommand.setBackground(getResources().getDrawable(R.drawable.btn_small_orange));
                btn_yes_activity_recommand.setTag(1);*/
            }else {
                btn_no_activity_recommand.setTextColor(getResources().getColor(R.color.white));
                btn_no_activity_recommand.setBackground(getResources().getDrawable(R.drawable.btn_small_orange));
                btn_no_activity_recommand.setTag(1);

                btn_yes_activity_recommand.setTextColor(getResources().getColor(R.color.colorOrange));
                btn_yes_activity_recommand.setBackground(getResources().getDrawable(R.drawable.btn_small_orange_border));
                btn_yes_activity_recommand.setTag(0);
            }
        });

        btn_yes_activity_recommand.setOnClickListener(view -> {
            int tag = Integer.parseInt(btn_yes_activity_recommand.getTag()+"") ;
            if (tag == 1){
               /* btn_yes_activity_recommand.setTextColor(getResources().getColor(R.color.colorOrange));
                btn_yes_activity_recommand.setBackground(getResources().getDrawable(R.drawable.btn_small_orange_border));
                btn_yes_activity_recommand.setTag(0);

                btn_no_activity_recommand.setTextColor(getResources().getColor(R.color.white));
                btn_no_activity_recommand.setBackground(getResources().getDrawable(R.drawable.btn_small_orange));
                btn_no_activity_recommand.setTag(1);*/
            }else {
                btn_yes_activity_recommand.setTextColor(getResources().getColor(R.color.white));
                btn_yes_activity_recommand.setBackground(getResources().getDrawable(R.drawable.btn_small_orange));
                btn_yes_activity_recommand.setTag(1);

                btn_no_activity_recommand.setTextColor(getResources().getColor(R.color.colorOrange));
                btn_no_activity_recommand.setBackground(getResources().getDrawable(R.drawable.btn_small_orange_border));
                btn_no_activity_recommand.setTag(0);
            }
        });
        Button btnCloseWeather = findViewById(R.id.btnMainCloseWeather);
        btnCloseWeather.setOnClickListener(view -> lytWeather.setVisibility(View.GONE));

        service_init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCircleOnOFf();
    }

    public void setCircleOnOFf(){
        if(!SharedPref.getValue(MainActivity.this,SharedPref.PREF_IS_ALARM_CLOCK_CIRCLE)){
            btnAlarm.setVisibility(View.GONE);
        }else {
            btnAlarm.setVisibility(View.VISIBLE);
        }
        if(!SharedPref.getValue(MainActivity.this,SharedPref.PREF_IS_ALERT_CIRCLE)){
            btnAlert.setVisibility(View.GONE);
        }else {
            btnAlert.setVisibility(View.VISIBLE);
        }
        if(!SharedPref.getValue(MainActivity.this,SharedPref.PREF_IS_SLEEP_ANALYSIS_CIRCLE)){
            btnSleep.setVisibility(View.GONE);
            rl_sleep_analysis_detail.setVisibility(View.GONE);
        }else {
            btnSleep.setVisibility(View.VISIBLE);
            rl_sleep_analysis_detail.setVisibility(View.VISIBLE);
        }
        if(!SharedPref.getValue(MainActivity.this,SharedPref.PREF_IS_ACTIVITY_ANALYSIS_CIRCLE)){
            btnActivity.setVisibility(View.GONE);
            rl_activity_analysis_details.setVisibility(View.GONE);
            ll_yesterday.setVisibility(View.GONE);
        }else {
            btnActivity.setVisibility(View.VISIBLE);
            rl_activity_analysis_details.setVisibility(View.VISIBLE);
            ll_yesterday.setVisibility(View.VISIBLE);
        }
    }

    public void AlarmClockDialog(String time) {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_alarm);

        TimePicker timePick = (TimePicker) dialog.findViewById(R.id.timePickerNewAlarm);
        ToggleButton tgOnOff = (ToggleButton) dialog.findViewById(R.id.tgOnOff);
        TextView tv_back = (TextView) dialog.findViewById(R.id.tv_back);
        TextView tv_ok = (TextView) dialog.findViewById(R.id.tv_ok);
        if (!time.equals("")) {
            String[] timeHour = time.split(":");
            int hour = Integer.valueOf(timeHour[0].trim());

            String[] timeMinute = timeHour[1].trim().split(" ");
            String[] timeAMPM = timeMinute[1].trim().split("\n");

            if (timeAMPM[0].trim().equals("PM")){
                hour = hour+12;
            }
            if (timeAMPM[1].trim().equals("off")){
                tgOnOff.setChecked(false);
            }else {
                tgOnOff.setChecked(true);
            }
            timePick.setCurrentHour(hour);
            timePick.setCurrentMinute(Integer.valueOf(timeMinute[0].trim()));
        }

        tv_back.setOnClickListener(view -> {
            dialog.dismiss();
        });

        tv_ok.setOnClickListener(view -> {
            int hour, minute;
            String ONOff = "off";
            String AM_PM ;
            if (tgOnOff.isChecked()){
                ONOff = "on";
            }
            if (Build.VERSION.SDK_INT >= 23 ){
                hour = timePick.getHour();
                minute = timePick.getMinute();
            } else{
                hour = timePick.getCurrentHour();
                minute = timePick.getCurrentMinute();
            }

            if(hour < 12) {
                AM_PM = "AM";
            } else {
                AM_PM = "PM";
                hour=hour-12;
            }
            dialog.dismiss();
            tv_alarmTime.setText(hour +":"+new DecimalFormat("00").format(minute)+" "+AM_PM);
            tv_alrm_toggle.setText(ONOff);
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(START)) {
            drawer.closeDrawer(START);
        } else {
            super.onBackPressed();
        }
    }

    private PercentageChartView batteryChargeView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.getItem(0);
        menuItem.setActionView(R.layout.my_ring);
        View view = menuItem.getActionView();
         batteryChargeView = view.findViewById(R.id.pbMyRing);
        TextView lblProgress = view.findViewById(R.id.lblMyRingProgress);
        lblProgress.setText(String.format("%s", (int) batteryChargeView.getProgress()));


        TextView lblTextMyRing = view.findViewById(R.id.txtMyring);
        lblTextMyRing.setText(String.format("%s", (int) batteryChargeView.getProgress()));

        batteryChargeView.setOnProgressChangeListener(progress -> lblProgress.setText(String.format("%s", (int) progress)));

        view.setOnClickListener(view1 -> startActivity(new Intent(MainActivity.this, MyRingActivity.class)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_ring) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_uart:
                Intent i = new Intent(MainActivity.this, UARTActivity.class);
                    startActivity(i);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(START);
        return true;
    }


    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {

            MainActivity.mService = ((UartService.LocalBinder) rawBinder).getService();
            Log.d(TAG, "onServiceConnected mService= " + MainActivity.mService);
            if (!MainActivity.mService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                //finish();
            }
        }

        public void onServiceDisconnected(ComponentName classname) {
            ////     mService.disconnect(mDevice);
            MainActivity.mService = null;
        }
    };

    private void service_init() {
        Intent bindIntent = new Intent(this, UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }

    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("UARTStatusChangeReceiver");
            final Intent mIntent = intent;
            //*********************//
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                        Log.d(TAG, "UART_CONNECT_MSG");
                        if ( UARTActivity.getInstance() != null )
                        {
                            UARTActivity.getInstance().getBtnConnectDisconnect().setText("Disconnect");
                            UARTActivity.getInstance().getEdtMessage().setEnabled(true);
                            UARTActivity.getInstance().getBtnSend().setEnabled(true);
                            ((TextView) UARTActivity.getInstance().findViewById(R.id.deviceName)).setText(MainActivity.mDevice.getName()+ " - ready");
                            UARTActivity.getInstance().getListAdapter().add("["+currentDateTimeString+"] Connected to: "+ MainActivity.mDevice.getName());
                            UARTActivity.getInstance().getMessageListView().smoothScrollToPosition(UARTActivity.getInstance().getListAdapter().getCount() - 1);
                            UARTActivity.getInstance().setState(UART_PROFILE_CONNECTED);
                        }
                    }
                });
            }
            //*********************//
            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
                if (UARTActivity.getInstance()!=null){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                            Log.d(TAG, "UART_DISCONNECT_MSG");
                            UARTActivity.getInstance().getBtnConnectDisconnect().setText("Connect");
                            UARTActivity.getInstance().getEdtMessage().setEnabled(false);
                            UARTActivity.getInstance().getBtnSend().setEnabled(false);
                            ((TextView) UARTActivity.getInstance().findViewById(R.id.deviceName)).setText("Not Connected");
                            UARTActivity.getInstance().getListAdapter().add("["+currentDateTimeString+"] Disconnected to: "+ MainActivity.mDevice.getName());
                            UARTActivity.getInstance().setState(UART_PROFILE_DISCONNECTED);
                            MainActivity.mService.close();
                            //setUiState();

                        }
                    });
                }
            }
            //*********************//
            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                MainActivity.mService.enableTXNotification();
            }
            //*********************//
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {

                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            String text = new String(txValue, "UTF-8");
                            String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                            System.out.println("["+currentDateTimeString+"] RX: "+text);
                            if (text.contains("BAT")){
                                String filtered = text.replace("BAT","");
                                int value = Integer.parseInt(filtered);
                                batteryChargeView.setProgress(value,true);
                            }
                            if ( UARTActivity.getInstance() != null )
                            {
                                UARTActivity.getInstance().getListAdapter().add("["+currentDateTimeString+"] RX: "+text);
                                UARTActivity.getInstance().getMessageListView().smoothScrollToPosition(UARTActivity.getInstance().getListAdapter().getCount() - 1);
                            }
                            if ( ActivityAnalysisActivity.getInstance() != null )
                            {
//                                String hex = text.substring(1,text.length());
//                                Integer integer = Integer.parseInt(hex,16);

//                                ActivityAnalysisActivity.getInstance().addDataSleep(integer);
                                ActivityAnalysisActivity.getInstance().addLiveData(text);
                            }
                            else if ( AlarmActivity.getInstance() != null )
                            {
                                System.out.println(text);
                                String command = text.substring(0,4);
                                if ( command.equals("alrm") )
                                {
                                    System.out.println("command="+command);
                                    String[] arrData = text.split("-");

                                    String sRepSub = arrData[0].replace("alrm","").trim();
                                    String[] sRepSubData = sRepSub.split("]");
                                    int alarmId = Integer.parseInt(sRepSubData[0].replace("[","").trim());
                                    String sRep = sRepSubData[1];

                                    String sTime = arrData[1];
                                    String sVibrate = arrData[2];
                                    String sTitle = "Alarm";
                                    if ( arrData.length > 3 )
                                        sTitle = arrData[3];

                                    AlarmActivity.getInstance().addAlarmList(alarmId,sTime,sTitle,sRep,sVibrate);
                                }
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                });
            }
            //*********************//
            if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)){
                showMessage("Device doesn't support UART. Disconnecting");
                MainActivity.mService.disconnect();
            }


        }
    };

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
    private Drawable setSaturation(Drawable drawable, float value) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(value);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        drawable.setColorFilter(filter);

        return drawable;
    }


    private void showIntro(){
        BubbleShowCaseBuilder showCase1 = new BubbleShowCaseBuilder(this)
                .backgroundColorResourceId(R.color.colorOrange)
                .arrowPosition(BubbleShowCase.ArrowPosition.TOP)
                .title(getString(R.string.explore_possibilities)).titleTextSize(20)
                .description(getString(R.string.d_explore))
                .targetView(findViewById(R.id.lytMainMyCircles))
                .listener(bubbleShowCaseListener(null, false));

        BubbleShowCaseBuilder showCase2 = new BubbleShowCaseBuilder(this)
                .backgroundColorResourceId(R.color.colorOrange)
                .arrowPosition(BubbleShowCase.ArrowPosition.TOP)
                .title(getString(R.string.quick_access_menu)).titleTextSize(20)
                .description(getString(R.string.d_quick_access))
                .targetView(findViewById(R.id.lytMainQuickAccess))
                .listener(bubbleShowCaseListener(crdMainSleep, false));

        BubbleShowCaseBuilder showCase3 = new BubbleShowCaseBuilder(this)
                .backgroundColorResourceId(R.color.colorOrange)
                .arrowPosition(BubbleShowCase.ArrowPosition.TOP)
                .title(getString(R.string.adaptive_feed)).titleTextSize(20)
                .description(getString(R.string.d_adaptive_feed))
                .targetView(crdMainSleep)
                .listener(bubbleShowCaseListener(null, false));

        BubbleShowCaseBuilder showCase4 = new BubbleShowCaseBuilder(this)
                .backgroundColorResourceId(R.color.colorOrange)
                .arrowPosition(BubbleShowCase.ArrowPosition.BOTTOM)
                .title(getString(R.string.let_friend)).titleTextSize(20)
                .description(getString(R.string.d_let_friend))
                .targetView(findViewById(R.id.lytMainSub))
                .listener(bubbleShowCaseListener(null, true))
                .imageResourceId(R.mipmap.ic_launcher);

        BubbleShowCaseSequence showCaseSequence = new BubbleShowCaseSequence();
        showCaseSequence.addShowCase(showCase1)
                .addShowCase(showCase2)
                .addShowCase(showCase3)
                .addShowCase(showCase4);

        showCaseSequence.show();
    }

    private BubbleShowCaseListener bubbleShowCaseListener(View view, boolean toTop) {
        ScrollView scrollView = findViewById(R.id.scrMain);
        return new BubbleShowCaseListener() {
            @Override
            public void onTargetClick(@NonNull BubbleShowCase bubbleShowCase) {

            }

            @Override
            public void onCloseActionImageClick(@NonNull BubbleShowCase bubbleShowCase) {
                if (view != null) {
                    scrollView.post(() -> scrollView.smoothScrollTo(0, view.getBottom()));
                }
                if (toTop) {
                    scrollView.post(() -> scrollView.smoothScrollTo(0, 0));
                }
            }

            @Override
            public void onBackgroundDimClick(@NonNull BubbleShowCase bubbleShowCase) {

            }

            @Override
            public void onBubbleClick(@NonNull BubbleShowCase bubbleShowCase) {
                if (view != null) {
                    scrollView.post(() -> scrollView.smoothScrollTo(0, view.getBottom()));
                }
                if (toTop) {
                    scrollView.post(() -> scrollView.smoothScrollTo(0, 0));
                }
                bubbleShowCase.dismiss();
            }
        };
    }

}
