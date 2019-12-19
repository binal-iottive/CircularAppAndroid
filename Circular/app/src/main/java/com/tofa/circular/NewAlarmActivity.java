package com.tofa.circular;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tofa.circular.customclass.DialogType;
import com.tofa.circular.dialog.CheckListDialog;
import com.tofa.circular.dialog.InputDialog;
import com.tofa.circular.nrfUARTv2.UartService;

import java.io.UnsupportedEncodingException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static androidx.core.view.GravityCompat.START;

public class NewAlarmActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] days = new DateFormatSymbols(Locale.getDefault()).getWeekdays();
    private int selectedSnooze;
    private int selectedSmartSnooze;
    private int selectedSmartAlarm;
    private String mode = "";
    public static int vibrationlvl = 0;
    private String alarmid = "";
    private String rep = "";
    Button btnDelete;
    List<Boolean> booleans;
    List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        ImageView btnClose = findViewById(R.id.btnNewAlarmBack);
        btnClose.setOnClickListener(this);

        ImageView btnSubmit = findViewById(R.id.btnNewAlarmApply);
        btnSubmit.setOnClickListener(this);

        findViewById(R.id.btnNewAlarmEditVibration).setOnClickListener(this);
        findViewById(R.id.btnNewAlarmRepeat).setOnClickListener(this);
        findViewById(R.id.btnNewAlarmLabel).setOnClickListener(this);
        findViewById(R.id.btnNewAlarmSnooze).setOnClickListener(this);
        findViewById(R.id.btnNewAlarmSmartSnooze).setOnClickListener(this);
        findViewById(R.id.btnNewAlarmSmartAlarm).setOnClickListener(this);

        btnDelete = findViewById(R.id.btnNewAlarmDelete);


        TextView txtViewTitle = findViewById(R.id.txtNewAlarmTitle);
        TextView txtViewRepeat = findViewById(R.id.txtNewAlarmRepeat);
        txtViewRepeat.setText("Everyday");
        Intent intent = getIntent();
        vibrationlvl = intent.getIntExtra("vibrationlvl", 0);
        rep = intent.getStringExtra("rep");
        alarmid = intent.getStringExtra("id");
        mode = intent.getStringExtra("mode");
        if ( mode.equals("edit") )
        {
            txtViewTitle.setText("Edit Alarm");
            btnDelete.setVisibility(View.VISIBLE);

            String title = intent.getStringExtra("title");
            TextView txtTitle = findViewById(R.id.txtNewAlarmLabel);
            txtTitle.setText(title);

            TimePicker timePick = findViewById(R.id.timePickerNewAlarm);

            String time = intent.getStringExtra("time");
            String[] timeHourMinute = time.split(":");
            timePick.setCurrentHour(Integer.valueOf(timeHourMinute[0]));
            timePick.setCurrentMinute(Integer.valueOf(timeHourMinute[1]));

            String sRepet = "";
            for ( int i=0; i<rep.length(); i++ )
            {
                switch ( rep.charAt(i) )
                {
                    case '1' : sRepet += " Mon"; break;
                    case '2' : sRepet += " Tue";break;
                    case '3' : sRepet += " Wed";break;
                    case '4' : sRepet += " Thu";break;
                    case '5' : sRepet += " Fri";break;
                    case '6' : sRepet += " Sat";break;
                    case '7' : sRepet += " Sun";break;
                }
                if ( rep.charAt(i) == '0' )
                {
                    sRepet = "Everyday";
                    break;
                }
            }

            txtViewRepeat.setText(sRepet);
        }
        else
        {
            txtViewTitle.setText("New Alarm");
            btnDelete.setVisibility(View.GONE);

        }


        list = new ArrayList<>(Arrays.asList(days));
        int idx = list.indexOf("");
        if(idx > -1){
            list.remove(idx);
        }
        list.add(0, getString(R.string.every_day));


        booleans = new ArrayList<>();
        for ( int i=0; i<list.size(); i++ )
        {
            booleans.add(false);
        }

        if ( mode.equals("edit"))
        {
            if ( rep.equals("0") )
            {
                for ( int i=0; i<list.size(); i++ )
                {
                    booleans.set(i,true);
                }
            }
            else
            {
                for ( int j=0; j<rep.length(); j++ )
                {
                    String x = rep.substring(j,j+1);
                    try {
                        Integer index = Integer.parseInt(x)+1;
                        if ( index == 8 )
                            index = 1;
                        booleans.set(index,true);
                    }
                    catch(Exception e)
                    {
                        System.out.println(e);
                    }

                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        System.out.println("view="+view.getId());
        switch (view.getId()) {
            case R.id.btnNewAlarmEditVibration:
                int viblvl = 50;
                if ( mode.equals("edit") )
                    viblvl = vibrationlvl;
                System.out.println("mode="+mode+" vibrate3="+vibrationlvl);

                Intent intent = new Intent(NewAlarmActivity.this, EditVibrationActivity.class);;
                intent.putExtra("viblvl",viblvl);

                startActivity(intent);
                break;
            case R.id.btnNewAlarmRepeat:
                showRepeatDialog();
                break;
            case R.id.btnNewAlarmLabel:
                showLabelDialog();
                break;
            case R.id.btnNewAlarmSnooze:
                showSnoozeDialog();
                break;
            case R.id.btnNewAlarmSmartSnooze:
                showSmartSnoozeDialog();
                break;
            case  R.id.btnNewAlarmSmartAlarm:
                showSmartAlarmDialog();
                break;

            case  R.id.btnNewAlarmBack:
                finish();
                break;

            case  R.id.btnNewAlarmApply:
                final TextView txtlabel = findViewById(R.id.txtNewAlarmLabel);

                TimePicker picker = (TimePicker)findViewById(R.id.timePickerNewAlarm);
                int hour, minute;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = picker.getHour();
                    minute = picker.getMinute();
                } else{
                    hour = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }
                System.out.println("strTx="+rep);

                vibrationlvl = EditVibrationActivity.vibrationLvl;
                String strTx = "alrm"+rep+"-"+hour+":"+minute+"-"+vibrationlvl+"-"+txtlabel.getText();
                System.out.println("strTx="+strTx);

                if ( MainActivity.mService != null && MainActivity.mDevice != null )
                {
                    try {
                        byte[] value = strTx.getBytes("UTF-8");
                        MainActivity.mService.writeRXCharacteristic(value);
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    finish();
                    Toast.makeText(this, "New Alarm has been set", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(this, "Please connect first", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showSmartAlarmDialog(){
        final List<String> list = new ArrayList<>();
        list.add("30 " + getString(R.string.minute) + "s (default)");
        list.add("45 " + getString(R.string.minute) + "s");
        list.add("1 " + getString(R.string.hour));
        list.add("1:15 " + getString(R.string.hour));
        list.add("1:30 " + getString(R.string.hour));
        list.add(getString(R.string.off).toUpperCase());
        final CheckListDialog listDialog = new CheckListDialog(this, list, selectedSmartAlarm);
        listDialog.setTitle(getResources().getString(R.string.smart_alarm) + " window");
        listDialog.setMultiSelect(false);
        listDialog.setSubTitle(getString(R.string.window));
        listDialog.setDetail(getString(R.string.d_smart_alarm));
        listDialog.setDialogType(DialogType.DIALOG_INFO);
        listDialog.setSeparatorList(new int[] {4});
        /*
        listDialog.setBtnPositive(getResources().getString(R.string.ok), view -> {
            TextView lblSooze = findViewById(R.id.txtNewAlarmSmartAlarm);
            selectedSmartAlarm = listDialog.getSelectedPosition();
            lblSooze.setText(list.get(selectedSmartAlarm));
            listDialog.dismiss();
        });
        listDialog.setBtnNegative(getResources().getString(R.string.back), view -> listDialog.dismiss());
        */
        listDialog.show();
    }

    private void showSmartSnoozeDialog(){
        final List<String> list = new ArrayList<>();
        list.add("1 " + getString(R.string.minute));
        list.add("2 " + getString(R.string.minute) + "s");
        list.add("5 " + getString(R.string.minute) + "s (default)");
        list.add("10 " + getString(R.string.minute) + "s");
        list.add("15 " + getString(R.string.minute) + "s");
        list.add(getString(R.string.off).toUpperCase());
        final CheckListDialog listDialog = new CheckListDialog(this, list, selectedSmartSnooze);
        listDialog.setTitle(getResources().getString(R.string.smart_snooze));
        listDialog.setMultiSelect(false);
        listDialog.setSubTitle(getString(R.string.interval));
        listDialog.setDetail(getString(R.string.d_smart_snooze));
        listDialog.setDialogType(DialogType.DIALOG_INFO);
        listDialog.setSeparatorList(new int[] {4});
        listDialog.setBtnPositive(getResources().getString(R.string.ok), view -> {
            TextView lblSooze = findViewById(R.id.txtNewAlarmSmartSnooze);
            selectedSmartSnooze = listDialog.getSelectedPosition();
            lblSooze.setText(list.get(selectedSmartSnooze));
            listDialog.dismiss();
        });
        listDialog.setBtnNegative(getResources().getString(R.string.back), view -> listDialog.dismiss());
        listDialog.show();
    }

    private void showSnoozeDialog() {
        final List<String> list = new ArrayList<>();
        list.add("1 " + getString(R.string.minute));
        list.add("2 " + getString(R.string.minute) + "s");
        list.add("5 " + getString(R.string.minute) + "s (default)");
        list.add("10 " + getString(R.string.minute) + "s");
        list.add("15 " + getString(R.string.minute) + "s");
        list.add(getString(R.string.off).toUpperCase());
        final CheckListDialog listDialog = new CheckListDialog(this, list, selectedSnooze);
        listDialog.setTitle(getResources().getString(R.string.snooze));
        listDialog.setMultiSelect(false);
        listDialog.setSubTitle(getString(R.string.interval));
        listDialog.setDetail(getString(R.string.d_snooze));
        listDialog.setDialogType(DialogType.DIALOG_INFO);
        listDialog.setSeparatorList(new int[] {4});
        listDialog.setBtnPositive(getResources().getString(R.string.ok), view -> {
            TextView lblSooze = findViewById(R.id.txtNewAlarmSnooze);
            selectedSnooze = listDialog.getSelectedPosition();
            lblSooze.setText(list.get(selectedSnooze));
            listDialog.dismiss();
        });
        listDialog.setBtnNegative(getResources().getString(R.string.back), view -> listDialog.dismiss());
        listDialog.show();
    }

    private void showLabelDialog() {
        final InputDialog inputDialog = new InputDialog(this);
        inputDialog.setTitle(getString(R.string.enter_label));
        inputDialog.setDialogType(DialogType.DIALOG_INFO);
        final TextView txtlabel = findViewById(R.id.txtNewAlarmLabel);
        inputDialog.setText(txtlabel.getText().toString());
        inputDialog.setShowSubtitle(false);
        inputDialog.setBtnPositive(getResources().getString(R.string.ok), view -> {
            txtlabel.setText(inputDialog.getText());
            inputDialog.dismiss();
        });
        inputDialog.setBtnNegative(getResources().getString(R.string.back), view -> inputDialog.dismiss());
        inputDialog.show();
    }

    private void showRepeatDialog() {


        final CheckListDialog listDialog = new CheckListDialog(this, list);
        listDialog.setTitle(getResources().getString(R.string.repeat));
        listDialog.setMultiSelect(true);
        listDialog.setShowSubtitle(false);
        listDialog.setDialogType(DialogType.DIALOG_INFO);
        listDialog.setSeparatorList(new int[] {0});


        listDialog.setSelectedList(booleans);


        listDialog.setBtnPositive(getResources().getString(R.string.ok), view -> {
            listDialog.dismiss();
            int i=0;
            rep = "";
            boolean addSunday = false;
            for (boolean entry : listDialog.getSelectedList()) {
                if ( i==0 && entry == true )
                {
                    rep = "0";
                }
                else if ( i==1 && entry == true )
                {
                    addSunday = true;
                }
                else if ( entry )
                {
                    rep += String.valueOf(i-1);
                }
                System.out.println(entry);
                i++;
            }

            if ( addSunday )
                rep += "7";
        });
        listDialog.setBtnNegative(getResources().getString(R.string.back), view -> listDialog.dismiss());

        listDialog.show();
    }

}
