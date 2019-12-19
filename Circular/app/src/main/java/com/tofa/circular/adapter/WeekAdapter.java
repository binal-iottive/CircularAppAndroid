package com.tofa.circular.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tofa.circular.R;
import com.tofa.circular.customclass.WeekData;

import java.util.ArrayList;

public class WeekAdapter extends BaseAdapter {
     Context mContext;
     ArrayList<WeekData> weekDatas;
     private ArrayList<Integer> getViewList = new ArrayList<>();

    public WeekAdapter(Context c, ArrayList<WeekData> weekDataList) {
        this.weekDatas = weekDataList;
        this.mContext = c;
    }

    @Override
    public int getCount() {
        return weekDatas.size();
    }
    @Override
    public Object getItem(int i) {
        return weekDatas.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_calendar, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!getViewList.contains(position)) {
            getViewList.add(position);
            if (position==weekDatas.size()-1){
                getViewList = new ArrayList<>();
            }

            WeekData weekData = weekDatas.get(position);
            String gridValue = weekData.getDays();

            int[] colorValue = weekData.getColorList();
            if (colorValue != null) {
                if (colorValue.length > 0) {
                    for (int i = 0; i < colorValue.length; i++) {
                        View colorView = new ImageView(mContext);
                        colorView.setBackgroundColor(colorValue[i]);
                        viewHolder.lytWeek.addView(colorView);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) colorView.getLayoutParams();
                        params.width = 50;
                        params.height = 15;
                        params.topMargin = 10;
                        params.gravity = Gravity.CENTER;
                    }
                }
            }

            viewHolder.dayView.setSingleLine(false);
            viewHolder.dayView.setText(gridValue);
        }
        return convertView;
    }

    private class ViewHolder {
        LinearLayout lytWeek;
        TextView dayView;

        public ViewHolder(View view) {
            dayView = view.findViewById(R.id.date);
            lytWeek = view.findViewById(R.id.lytWeek);
        }
    }
}
