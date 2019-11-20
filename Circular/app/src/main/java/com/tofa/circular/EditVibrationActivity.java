package com.tofa.circular;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tofa.circular.adapter.ListCheckAdapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class EditVibrationActivity extends AppCompatActivity implements View.OnClickListener{
    public static int vibrationLvl = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorGrayBG));
        }

        setContentView(R.layout.activity_edit_vibration);

        List<String> list = new ArrayList<>();
        list.add(getString(R.string.alert_default));
        list.add(getString(R.string.heartbeat));
        list.add(getString(R.string.quick));
        list.add(getString(R.string.rapid));
        list.add(getString(R.string.sos));
        list.add(getString(R.string.staccato));
        list.add(getString(R.string.symphony));

        ListCheckAdapter listCheckValueAdapter = new ListCheckAdapter(this, list, 0);

        ListView listView = findViewById(R.id.listviewVibrationType);

        listView.setAdapter(listCheckValueAdapter);

        ImageView btnApply = findViewById(R.id.btnNewAlarmApply);
        btnApply.setOnClickListener(this);

        ImageView btnBack = findViewById(R.id.btnNewAlarmBack);
        btnBack.setOnClickListener(this);

        Intent intent = getIntent();
        vibrationLvl = intent.getIntExtra("viblvl", 100);

        System.out.println("vib2="+vibrationLvl);
        SeekBar seekBar = findViewById(R.id.seekBarVibrationIntensity);
        seekBar.setProgress(vibrationLvl);


    }


    //View.OnClickListener listenerFinish = view -> finish();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNewAlarmApply:

                SeekBar seekBar = findViewById(R.id.seekBarVibrationIntensity);
                vibrationLvl =seekBar.getProgress();
                NewAlarmActivity.vibrationlvl = vibrationLvl;
                finish();
                break;
            case R.id.btnNewAlarmBack:
                finish();
                break;
        }
    }
}
