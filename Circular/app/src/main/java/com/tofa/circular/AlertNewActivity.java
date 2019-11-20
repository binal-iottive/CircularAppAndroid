package com.tofa.circular;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;

import com.tofa.circular.adapter.AppListAdapter;

public class AlertNewActivity extends AppCompatActivity {

    private GridView gv_app;
    private AppListAdapter appListAdapter;
    int logos[] = {R.drawable.play_button, R.drawable.play_button, R.drawable.play_button,
            R.drawable.play_button, R.drawable.play_button, R.drawable.play_button,
            R.drawable.play_button, R.drawable.play_button, R.drawable.play_button};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_new);

        gv_app = (GridView) findViewById(R.id.gv_app);
        appListAdapter = new AppListAdapter(getApplicationContext(), logos);
        gv_app.setAdapter(appListAdapter);
    }
}
