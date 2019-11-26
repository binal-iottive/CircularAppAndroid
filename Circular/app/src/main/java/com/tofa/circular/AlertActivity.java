package com.tofa.circular;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tofa.circular.adapter.AlertsAdapter;
import com.tofa.circular.model.AppAlertModel;
import com.tofa.circular.customclass.Utils;

import java.util.ArrayList;

public class AlertActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rlAlerts;
    private TextView tv_addAlert;
    private AlertsAdapter mAdapter;
    private ArrayList<AppAlertModel> alertList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        rlAlerts = findViewById(R.id.rlAlerts);
        tv_addAlert = findViewById(R.id.tv_addAlert);

        tv_addAlert.setOnClickListener(this);

        mAdapter = new AlertsAdapter(Utils.appAlertArraylist,AlertActivity.this);
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
                startActivityForResult(intent, Utils.REQUEST_ADD_ALERT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Utils.REQUEST_ADD_ALERT){
            mAdapter.notifyDataSetChanged();
        }
    }
}
