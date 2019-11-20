package com.tofa.circular;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.tofa.circular.adapter.WeekAdapter;
import com.tofa.circular.customclass.WeekData;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class AlarmActivity extends AppCompatActivity {

    static AlarmActivity instance;
    public static AlarmActivity getInstance()
    {
        return instance;
    }

    LinearLayout layoutAlarms = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        setContentView(R.layout.activity_alarm);

        List<WeekData> weekData = new ArrayList<>();

        weekData.add(createWeekData("S", null));
        weekData.add(createWeekData("M", new int[] {
                getResources().getColor(R.color.colorGreen),
                getResources().getColor(R.color.colorRed)
        }));
        weekData.add(createWeekData("T", new int[] {
                getResources().getColor(R.color.colorGreen)
        }));
        weekData.add(createWeekData("W", new int[] {
                getResources().getColor(R.color.colorGreen)
        }));
        weekData.add(createWeekData("T", new int[] {
                getResources().getColor(R.color.colorGreen)
        }));
        weekData.add(createWeekData("F", new int[] {
                getResources().getColor(R.color.colorGreen)
        }));
        weekData.add(createWeekData("S", new int[] {
                getResources().getColor(R.color.colorRed)
        }));

        WeekAdapter adapter = new WeekAdapter(this, weekData);
        GridView gridview = findViewById(R.id.grdAlarmDays);
        gridview.setAdapter(adapter);

        LineChart chart = findViewById(R.id.chartAlarmScore);
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 0));
        entries.add(new Entry(1, 2));
        entries.add(new Entry(3, 0));
        entries.add(new Entry(4, 1));
        entries.add(new Entry(5, 0));
        entries.add(new Entry(6, 1));
        entries.add(new Entry(8, 3));
        entries.add(new Entry(10, 1));
        entries.add(new Entry(11, 2));
        entries.add(new Entry(13, 1));
        entries.add(new Entry(15, 2));
        entries.add(new Entry(16, 1));
        entries.add(new Entry(17, 0));
        entries.add(new Entry(18, 1));
        entries.add(new Entry(20, 3));
        entries.add(new Entry(20.5f, 3));

        LineDataSet dataSet = new LineDataSet(entries, "");
        LineData lineData = new LineData(dataSet);

        dataSet.setDrawValues(false);
        dataSet.setLineWidth(5);
        dataSet.setHighlightEnabled(false);
        dataSet.setDrawCircles(false);
        dataSet.setMode(LineDataSet.Mode.STEPPED);

        chart.getLegend().setEnabled(false);
        chart.getDescription().setText("");

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setDrawAxisLine(false);
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawLabels(false);

        YAxis axisRight = chart.getAxisRight();
        axisRight.setDrawLabels(true);
        axisRight.setGranularityEnabled(true);
        axisRight.setAxisMinimum(0);
        axisRight.setAxisMaximum(3);
        axisRight.setGranularity(1);
        axisRight.setLabelCount(4, true);
        axisRight.setTextColor(getResources().getColor(R.color.colorWhite));
        axisRight.setTextSize(10);

        axisRight.setValueFormatter(new ValueFormatter() {
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

        CardView crdOval = findViewById(R.id.crdAlarmOval);
        final CardView crdScore = findViewById(R.id.crdAlarmScore);
        crdOval.setOnClickListener(view -> {
            if(crdScore.getVisibility() == View.GONE) {
                crdScore.setVisibility(View.VISIBLE);
            } else {
                crdScore.setVisibility(View.GONE);
            }
        });

        Button btnClose = findViewById(R.id.btnAlarmClose);
        btnClose.setOnClickListener(view -> crdScore.setVisibility(View.GONE));

        LinearLayout btnNewAlarm = findViewById(R.id.btnNewAlarm);
        btnNewAlarm.setOnClickListener(view -> newAlarm());

        layoutAlarms = findViewById(R.id.container_alarm);
        if(((LinearLayout) layoutAlarms).getChildCount() > 0)
            ((LinearLayout) layoutAlarms).removeAllViews();

        /*
        for ( int i=0; i<2; i++ )
        {
            String idAlarm = Integer.toString(i);
            AddAlarm(idAlarm,"08:00", "Alarm"+idAlarm, "0");
        }
        CardView crdAlarm1 = findViewById(R.id.crdAlarm1);
        crdAlarm1.setOnClickListener(view -> editAlarm("07:00"));

        CardView crdAlarm2 = findViewById(R.id.crdAlarm2);
        crdAlarm2.setOnClickListener(view -> editAlarm("10:20"));

        CardView crdAlarm3 = findViewById(R.id.crdAlarm3);
        crdAlarm3.setOnClickListener(view -> editAlarm("10:50"));
        */

        //new AsyncCaller().execute();
        //pullAlarmList();
    }

    private void pullAlarmList()
    {
        if ( MainActivity.mService != null && MainActivity.mDevice != null )
        {
            String strTx = "alrm";
            try {
                byte[] value = strTx.getBytes("UTF-8");
                MainActivity.mService.writeRXCharacteristic(value);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class AsyncCaller extends AsyncTask<Void, Integer, Void>
    {

        @Override
        protected Void doInBackground(Void... params) {
            for ( int i=0; i<8; i++ )
            {
                try {
                    publishProgress(i);
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... n) {

            String DummyAlarm = "";
            switch ( n[0] )
            {
                case 0 : DummyAlarm = "alrm[00]0-1:15-10-Wake up"; break;
                case 1 : DummyAlarm = "alrm[01]1-0:0-10-My Alarm"; break;
                case 2 : DummyAlarm = "alrm[02]2-2:30-22-Go To School"; break;
                case 3 : DummyAlarm = "alrm[03]3-2:30-33-OK"; break;
                case 4 : DummyAlarm = "alrm[04]4-2:30-25"; break;
                case 5 : DummyAlarm = "alrm[05]5-23:30-75"; break;
                case 6 : DummyAlarm = "alrm[06]6-22:57-50"; break;
                case 7 : DummyAlarm = "alrm[07]123456-23:57-50"; break;
                case 8 : DummyAlarm = "alrm[08]123456-21:57-50"; break;
            }
            if ( DummyAlarm != "" )
            {
                if ( MainActivity.getInstance() != null && MainActivity.getInstance().mService != null )
                    MainActivity.getInstance().mService.forceExtra(DummyAlarm.getBytes());
            }
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

    public void AddAlarm(String idAlarm, String time, String title, String sRep, String vibrationLvl)
    {
        View alarmview = LayoutInflater.from(this).inflate(R.layout.alarm_cardview, null);
        alarmview.setOnClickListener(view -> editAlarm( idAlarm,time,  title, sRep, vibrationLvl));

        TextView textAlarmTime = alarmview.findViewById(R.id.txtAlarmTime);
        textAlarmTime.setText(time);

        TextView textAlarmTitle = alarmview.findViewById(R.id.txtAlarmTitle);
        textAlarmTitle.setText(title);


        String sRepet = "";
        for ( int i=0; i<sRep.length(); i++ )
        {
            switch ( sRep.charAt(i) )
            {
                case '1' : sRepet += " Mon"; break;
                case '2' : sRepet += " Tue";break;
                case '3' : sRepet += " Wed";break;
                case '4' : sRepet += " Thu";break;
                case '5' : sRepet += " Fri";break;
                case '6' : sRepet += " Sat";break;
                case '7' : sRepet += " Sun";break;
            }
            if ( sRep.charAt(i) == '0' )
            {
                sRepet = "Everyday";
                break;
            }
        }

        TextView textAlarmRep = alarmview.findViewById(R.id.txtAlarmRep);
        textAlarmRep.setText(sRepet);

        TextView textVibrationLevel = alarmview.findViewById(R.id.txtAlarmVibrationLevel);
        textVibrationLevel.setText(vibrationLvl);



        layoutAlarms.addView(alarmview);
    }

    private WeekData createWeekData(String s, int[] colors) {
        WeekData weekData = new WeekData(s);
        weekData.setColorList(colors);
        return weekData;
    }

    private void newAlarm() {
        Intent intent = new Intent(this, NewAlarmActivity.class);
        intent.putExtra("mode","new");
        startActivity(intent);
    }

    private void editAlarm(String id, String time, String title, String rep, String vibrationLvl){
        Intent intent = new Intent(this, NewAlarmActivity.class);
        intent.putExtra("id","id");
        intent.putExtra("mode","edit");
        intent.putExtra("time", time);
        intent.putExtra("title", title);
        intent.putExtra("rep", rep);
        Integer vibLvl = Integer.parseInt(vibrationLvl);
        System.out.println("viblvl="+vibLvl);//
        intent.putExtra("vibrationlvl", vibLvl);
        startActivity(intent);
    }
}