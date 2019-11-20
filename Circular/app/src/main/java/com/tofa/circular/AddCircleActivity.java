package com.tofa.circular;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ramijemli.percentagechartview.PercentageChartView;
import com.tofa.circular.adapter.AddCircleAdapter;
import com.tofa.circular.customclass.AddCircleItem;

import java.util.ArrayList;
import java.util.List;

public class AddCircleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorGrayBG));
        }

        setContentView(R.layout.activity_add_circle);
        Toolbar mToolbar = findViewById(R.id.toolbarAddCircle);
        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ImageView btnBack = mToolbar.findViewById(R.id.btnAddCircleBack);
        btnBack.setOnClickListener(view -> finish());

        ListView listView = findViewById(R.id.listAddCircle);
        ArrayAdapter<AddCircleItem> adapter = new AddCircleAdapter(this, getItem());
        listView.setAdapter(adapter);
    }

    private List<AddCircleItem> getItem(){
        String soon = getRString(R.string.soon);
        String later = getRString(R.string.later);

        List<AddCircleItem> list = new ArrayList<>();
        list.add(createHeader(getRString(R.string.title_add_circle)));
        list.add(createHeader(getRString(R.string.vibration)));
        list.add(createItem(R.mipmap.ic_alarm, getRString(R.string.alarm_clock), getRString(R.string.d_alarm)));
        list.add(createItem(R.mipmap.ic_alert, getRString(R.string.alert), getRString(R.string.d_alert)));
        list.add(createItem(R.mipmap.ic_medication, getRString(R.string.medication), getRString(R.string.s_medication), soon, later));
        list.add(createItem(R.mipmap.ic_phone, getRString(R.string.t_phone), getRString(R.string.tt_phone), soon, later));
        list.add(createItem(R.mipmap.ic_reminder, getRString(R.string.reminders), getRString(R.string.t_reminder), soon, later));

        list.add(createHeader(getRString(R.string.wellness)));
        list.add(createItem(R.mipmap.ic_sleep, getRString(R.string.sleep_analysis), getRString(R.string.d_sleep)));
        list.add(createItem(R.mipmap.ic_activity, getRString(R.string.activity_analysis), getRString(R.string.d_activity)));
        list.add(createItem(R.mipmap.ic_program, getRString(R.string.programs), getRString(R.string.d_program)));

        list.add(createHeader(getRString(R.string.health)));
        list.add(createItem(R.mipmap.ic_aryth, getRString(R.string.arrhythmia), getRString(R.string.d_arryth), soon, later));
        list.add(createItem(R.mipmap.ic_tension, getRString(R.string.tension_measurement), getRString(R.string.d_tension), soon, later));
        list.add(createItem(R.mipmap.ic_driving, getRString(R.string.driving), getRString(R.string.d_driving), soon, later));
        list.add(createItem(R.mipmap.ic_sleepgray, getRString(R.string.sleep_apnea), getRString(R.string.d_sleep_apnea), soon, later));
        list.add(createItem(R.mipmap.ic_nutrition, getRString(R.string.nutrition), getRString(R.string.d_nutrition), soon, later));

        list.add(createHeader(getRString(R.string.control)));
        list.add(createItem(R.mipmap.ic_emergency, getRString(R.string.emrgency_button), getRString(R.string.d_emergency), soon, later));
        list.add(createItem(R.mipmap.ic_press, getRString(R.string.press_button), getRString(R.string.d_press), soon, later));

        list.add(createHeader(getRString(R.string.nfc)));
        list.add(createItem(R.mipmap.ic_contactless, getRString(R.string.contactless_action), getRString(R.string.d_contactless), soon, later));
        list.add(createItem(R.mipmap.ic_payment, getRString(R.string.contactless_payment), getRString(R.string.d_payment), soon, later));

        list.add(createHeader(""));
        return list;
    }

    private AddCircleItem createHeader(String title){
        AddCircleItem model = new AddCircleItem();
        model.setHeader(true);
        model.setTitle(title);
        return model;
    }

    private AddCircleItem createItem(int icon, String title, String desc, String toggleOn, String toogleOff){
        AddCircleItem model = new AddCircleItem();
        model.setIcon(getResources().getDrawable(icon));
        model.setTitle(title);
        model.setDesc(desc);
        model.setToggleon(toggleOn);
        model.setToggleoff(toogleOff);
        model.setHeader(false);
        return model;
    }

    private AddCircleItem createItem(int icon, String title, String desc){
        AddCircleItem model = new AddCircleItem();
        model.setIcon(getResources().getDrawable(icon));
        model.setTitle(title);
        model.setDesc(desc);
        model.setHeader(false);
        return model;
    }

    private String getRString(int resouce) {
        return getResources().getString(resouce);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.getItem(0);
        menuItem.setActionView(R.layout.my_ring);
        View view = menuItem.getActionView();
        view.findViewById(R.id.txtMyring).setVisibility(View.GONE);
        PercentageChartView pg = view.findViewById(R.id.pbMyRing);
        TextView lblProgress = view.findViewById(R.id.lblMyRingProgress);
        lblProgress.setText(String.format("%s", (int) pg.getProgress()));
        pg.setOnProgressChangeListener(progress -> lblProgress.setText(String.format("%s", (int) progress)));
        view.setOnClickListener(view1 -> startActivity(new Intent(AddCircleActivity.this, MyRingActivity.class)));
        return true;
    }
}
