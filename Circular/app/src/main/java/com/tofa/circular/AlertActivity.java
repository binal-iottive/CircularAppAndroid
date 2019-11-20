package com.tofa.circular;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tofa.circular.adapter.AlertsAdapter;
import com.tofa.circular.customclass.AlertModel;

import java.util.ArrayList;

public class AlertActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rlAlerts;
    private TextView tv_addAlert;
    private AlertsAdapter mAdapter;
    private ArrayList<AlertModel> alertList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        rlAlerts = findViewById(R.id.rlAlerts);
        tv_addAlert = findViewById(R.id.tv_addAlert);

        tv_addAlert.setOnClickListener(this);

        alertList.add(new AlertModel("Pnone call"));
        alertList.add(new AlertModel("Pnone call"));
        alertList.add(new AlertModel("Pnone call"));
        mAdapter = new AlertsAdapter(alertList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rlAlerts.setLayoutManager(mLayoutManager);
        rlAlerts.setItemAnimator(new DefaultItemAnimator());
        rlAlerts.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_addAlert:
                Intent intent = new Intent(AlertActivity.this,AlertNewActivity.class);
                startActivity(intent);
                break;
        }
    }
}
