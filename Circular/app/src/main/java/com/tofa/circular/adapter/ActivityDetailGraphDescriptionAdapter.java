package com.tofa.circular.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tofa.circular.R;
import com.tofa.circular.customclass.GraphUtils;
import com.tofa.circular.model.DetailGraphDescriptionModel;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;

public class ActivityDetailGraphDescriptionAdapter extends BaseAdapter {
    Context context;
    ArrayList<DetailGraphDescriptionModel> arrayList;
    LayoutInflater inflter;
    String listType;
    public ActivityDetailGraphDescriptionAdapter(Context applicationContext, ArrayList<DetailGraphDescriptionModel> arrayList,String listType) {
        this.context = applicationContext;
        this.arrayList = arrayList;
        this.listType = listType;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
       ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_activity_detail_chart, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DetailGraphDescriptionModel model = arrayList.get(i);
        viewHolder.tv_alertType.setText(model.desTitle);
        viewHolder.tv_alertValue.setText(model.desValue);

        if (i%2==0){
            viewHolder.ll_main.setBackgroundColor(context.getResources().getColor(R.color.colorGrayBG));
        }else {

        }

        if (listType.equals(GraphUtils.CHART_ACTION_ACTITY_INTENSITY)) {
            viewHolder.tv_dot_tine.setVisibility(View.GONE);
            if (model.desColor != 0) {
                viewHolder.cv_alertColor.setCardBackgroundColor(context.getResources().getColor(model.desColor));
            } else {
                viewHolder.cv_alertColor.setVisibility(View.GONE);
            }
        }else if (listType.equals(GraphUtils.CHART_ACTION_CALORIES_BURN)
                || listType.equals(GraphUtils.CHART_ACTION_HR_MAX)
                || listType.equals(GraphUtils.CHART_ACTION_ACTIVE_MINUTES)
                || listType.equals(GraphUtils.CHART_ACTION_HRV)) {
            viewHolder.cv_alertColor.setVisibility(View.GONE);
            if (model.desColor == 0) {
                viewHolder.tv_dot_tine.setVisibility(View.GONE);
            } else {
                viewHolder.tv_dot_tine.setVisibility(View.VISIBLE);
                viewHolder.tv_dot_tine.setTextColor(context.getResources().getColor(model.desColor));
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tv_alertType, tv_alertValue, tv_dot_tine;
        CardView cv_alertColor;
        LinearLayout ll_main;

        public ViewHolder(View view) {
            tv_alertType = (TextView) view.findViewById(R.id.tv_alertType);
            tv_alertValue = (TextView) view.findViewById(R.id.tv_alertValue);
            tv_dot_tine = (TextView) view.findViewById(R.id.tv_dot_tine);
            cv_alertColor = (CardView) view.findViewById(R.id.cv_alertColor);
            ll_main = (LinearLayout) view.findViewById(R.id.ll_main);
        }
    }
}
