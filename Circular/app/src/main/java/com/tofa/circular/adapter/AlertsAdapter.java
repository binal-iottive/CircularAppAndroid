package com.tofa.circular.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tofa.circular.R;
import com.tofa.circular.customclass.AlertModel;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.MyViewHolder> {

    private ArrayList<AlertModel> alertList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAlertType;

        public MyViewHolder(View view) {
            super(view);
            tvAlertType = (TextView) view.findViewById(R.id.tvAlertType);
        }
    }


    public AlertsAdapter(ArrayList<AlertModel> alertList) {
        this.alertList = alertList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_alerts, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AlertModel model = alertList.get(position);
        holder.tvAlertType.setText(model.alarmType);
    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }
}
