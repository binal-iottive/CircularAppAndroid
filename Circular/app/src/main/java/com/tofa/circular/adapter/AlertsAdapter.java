package com.tofa.circular.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tofa.circular.R;
import com.tofa.circular.model.AppAlertModel;
import com.tofa.circular.customclass.Utils;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.MyViewHolder> {

    private ArrayList<AppAlertModel> alertList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAlertType, tvAlertFrom;
        public ImageView iv_1, iv_repeat, ivDeleteAlert;
        public ImageView iv_dot4,iv_dot3,iv_dot2,iv_dot1, iv_dotClose;
        public LinearLayout ll_dots;

        public MyViewHolder(View view) {
            super(view);
            tvAlertType = (TextView) view.findViewById(R.id.tvAlertType);
            tvAlertFrom = (TextView) view.findViewById(R.id.tvAlertFrom);
            iv_1 = (ImageView) view.findViewById(R.id.iv_1);
            iv_repeat = (ImageView) view.findViewById(R.id.iv_repeat);
            ivDeleteAlert = (ImageView) view.findViewById(R.id.ivDeleteAlert);
            iv_dot4 = (ImageView) view.findViewById(R.id.iv_dot4);
            iv_dot3 = (ImageView) view.findViewById(R.id.iv_dot3);
            iv_dot2 = (ImageView) view.findViewById(R.id.iv_dot2);
            iv_dot1 = (ImageView) view.findViewById(R.id.iv_dot1);
            iv_dotClose = (ImageView) view.findViewById(R.id.iv_dotClose);
            ll_dots = (LinearLayout) view.findViewById(R.id.ll_dots);
        }
    }


    public AlertsAdapter(ArrayList<AppAlertModel> alertList, Context context) {
        this.alertList = alertList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_alerts, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AppAlertModel model = alertList.get(position);
        holder.tvAlertType.setText(model.alertType);
        if (model.alertType.equals(context.getString(R.string.facebook))||
                model.alertType.equals(context.getString(R.string.instagram))||
                model.alertType.equals(context.getString(R.string.twitter))||
                model.alertType.equals(context.getString(R.string.apple_mail))){
            holder.tvAlertFrom.setText("Notification");
        }else {
            holder.tvAlertFrom.setText("Form "+model.contactName);
        }
        holder.iv_1.setImageResource(model.imageDrawable);
        if (model.repeatCount==1){
            holder.iv_repeat.setImageResource(R.drawable.dots_1);
        }else  if (model.repeatCount==2){
            holder.iv_repeat.setImageResource(R.drawable.dots_2);
        }else  if (model.repeatCount==3){
            holder.iv_repeat.setImageResource(R.drawable.dots_3);
        }else  if (model.repeatCount==4){
            holder.iv_repeat.setImageResource(R.drawable.dots_4);
        }

        holder.ivDeleteAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertList.remove(position);
                Utils.appAlertArraylist = alertList;
                notifyDataSetChanged();
            }
        });

        holder.iv_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ll_dots.setVisibility(View.VISIBLE);
            }
        });

        holder.iv_dot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertList.get(position).repeatCount = 1;
                holder.ll_dots.setVisibility(View.GONE);
                notifyDataSetChanged();
            }
        });
        holder.iv_dot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertList.get(position).repeatCount = 2;
                holder.ll_dots.setVisibility(View.GONE);
                notifyDataSetChanged();
            }
        });

        holder.iv_dot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertList.get(position).repeatCount = 3;
                holder.ll_dots.setVisibility(View.GONE);
                notifyDataSetChanged();
            }
        });

        holder.iv_dot4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertList.get(position).repeatCount = 4;
                holder.ll_dots.setVisibility(View.GONE);
                notifyDataSetChanged();
            }
        });

        holder.iv_dotClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ll_dots.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }
}
