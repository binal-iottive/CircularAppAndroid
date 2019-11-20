package com.tofa.circular;

import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.tofa.circular.customclass.DialogType;
import com.tofa.circular.customclass.LogView;
import com.tofa.circular.customclass.Segment;
import com.tofa.circular.customclass.TimeChartData;
import com.tofa.circular.customclass.TimeChartDataSeparatorType;
import com.tofa.circular.customclass.TimeChartView;
import com.tofa.circular.dialog.BasicDialog;

import java.util.ArrayList;
import java.util.List;

public class EditLogsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_logs);

        TimeChartView chartView = findViewById(R.id.chartSleepLog);

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

        List<Segment> segments = new ArrayList<>();
        segments.add(new Segment(0, 25, getResources().getColor(R.color.colorBlue)));
        segments.add(new Segment(26, 36, getResources().getColor(R.color.colorBlue)));
        segments.add(new Segment(37, 47, getResources().getColor(R.color.colorBlue)));
        segments.add(new Segment(48, 50, getResources().getColor(R.color.colorBlue)));
        segments.add(new Segment(51, 60, getResources().getColor(R.color.colorBlue)));
        segments.add(new Segment(65, 100, getResources().getColor(R.color.colorBlue)));

        LogView logView1 = findViewById(R.id.lvLog1);
        logView1.setSegments(segments);

        List<Segment> segments2 = new ArrayList<>();
        segments2.add(new Segment(0, 100, getResources().getColor(R.color.colorBlue)));

        LogView logView2 = findViewById(R.id.lvLog2);
        logView2.setSegments(segments2);

        logView1.setOnClickListener(view -> editLog("Log 1 - Sleep start"));
    }

    private TimeChartData createData(int startHour, int startMin, int endHour, int endMin,
                                     TimeChartDataSeparatorType separatorType,
                                     String startLabel, String endLabel) {
        TimeChartData chartData = new TimeChartData(startHour, startMin, endHour, endMin, separatorType);
        chartData.setStartLabelText(startLabel);
        chartData.setEndLabelText(endLabel);
        return chartData;
    }

    private void editLog(String log){
        final BasicDialog basicDialog = new BasicDialog(this);
        basicDialog.setTitle(getResources().getString(R.string.edit_logs));
        basicDialog.setSubTitle(log);
        basicDialog.setDetail(getString(R.string.d_edit_logs));
        basicDialog.setDialogType(DialogType.DIALOG_INFO);
        basicDialog.setBtnPositive(getResources().getString(R.string.ok), vw -> basicDialog.dismiss());
        basicDialog.setBtnNegative(getResources().getString(R.string.back), vw -> basicDialog.dismiss());
        View view = basicDialog.getView();
        TimePicker timePicker = new TimePicker(this);

        basicDialog.show();
    }
}
