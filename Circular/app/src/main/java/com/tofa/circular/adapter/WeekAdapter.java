package com.tofa.circular.adapter;

import android.app.Activity;
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

import java.util.List;

public class WeekAdapter extends BaseAdapter {
    private Activity mContext;

    private List<WeekData> weekDatas;

    public WeekAdapter(Activity c, List<WeekData> weekDataList) {
        weekDatas = weekDataList;
        mContext = c;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LinearLayout lytWeek;
        TextView dayView;
        if (convertView == null) {
            LayoutInflater vi = mContext.getLayoutInflater();
            v = vi.inflate(R.layout.item_calendar, null);
        }

        dayView = v.findViewById(R.id.date);
        lytWeek = v.findViewById(R.id.lytWeek);

        WeekData weekData = weekDatas.get(position);
        String gridValue = weekData.getDays();

        int[] colorValue = weekData.getColorList();
        if(colorValue != null) {
            if (colorValue.length > 0) {
                for (int i = 0; i < colorValue.length; i++) {
                    View colorView = new ImageView(mContext);
                    colorView.setBackgroundColor(colorValue[i]);
                    lytWeek.addView(colorView);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) colorView.getLayoutParams();
                    params.width = 50;
                    params.height = 15;
                    params.topMargin = 10;
                    params.gravity = Gravity.CENTER;
                }
            }
        }


        dayView.setSingleLine(false);
        dayView.setText(gridValue);
        return v;
    }
}
