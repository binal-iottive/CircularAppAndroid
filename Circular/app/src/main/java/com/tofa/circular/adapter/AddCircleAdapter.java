package com.tofa.circular.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tofa.circular.R;
import com.tofa.circular.customclass.AddCircleItem;
import com.tofa.circular.customclass.SharedPref;

public class AddCircleAdapter extends ArrayAdapter<AddCircleItem> {
    private final List<AddCircleItem> list;
    private final Activity context;

    public AddCircleAdapter(Activity context, List<AddCircleItem> list) {
        super(context, R.layout.list_add_circle_header, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AddCircleItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;

        AddCircleItem circleItem = list.get(i);
        if(convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            if (circleItem.isHeaderItem()) {
                view = inflator.inflate(R.layout.list_add_circle_header, null);
                final ViewHolder viewHeader = new ViewHolder();
                viewHeader.txtTitle = view.findViewById(R.id.txtAddCircleHeader);
                view.setTag(viewHeader);
            } else {
                view = inflator.inflate(R.layout.list_add_circle, null);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.imgIcon = view.findViewById(R.id.imgAddCircleIcon);
                viewHolder.txtTitle = view.findViewById(R.id.txtAddCircleTitle);
                viewHolder.txtDesc = view.findViewById(R.id.txtAddCircleDesc);
                viewHolder.btnToggle = view.findViewById(R.id.btnAddCircleToggle);
                viewHolder.btnToggle.setOnCheckedChangeListener((compoundButton, b) -> {
                    AddCircleItem element = (AddCircleItem) viewHolder.btnToggle.getTag();
                    element.setSelected(compoundButton.isChecked());
                });
                view.setTag(viewHolder);
                viewHolder.btnToggle.setTag(list.get(i));
            }

        } else {
            view = convertView;
            if (!circleItem.isHeaderItem()){
                ((ViewHolder) view.getTag()).btnToggle.setTag(list.get(i));
            }
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        if(circleItem.isHeaderItem()) {
            holder.txtTitle.setText(circleItem.getTitle());
        } else {

            holder.imgIcon.setImageDrawable(circleItem.getIcon());
            holder.txtTitle.setText(circleItem.getTitle());
            holder.txtDesc.setText(circleItem.getDesc());
            holder.btnToggle.setText(circleItem.getToggleon());
            holder.btnToggle.setTextOn(circleItem.getToggleon());
            holder.btnToggle.setTextOff(circleItem.getToggleoff());
            holder.btnToggle.setChecked(circleItem.getSelected());
        }
        if(!circleItem.isHeaderItem()) {
            holder.btnToggle.setOnCheckedChangeListener((compoundButton, checked) -> {
                String title = list.get(i).getTitle();
                if (title.equals(context.getResources().getString(R.string.alarm_clock))) {
                    SharedPref.setValue(context, SharedPref.PREF_IS_ALARM_CLOCK_CIRCLE, checked);
                } else if (title.equals(context.getResources().getString(R.string.alert))) {
                    SharedPref.setValue(context, SharedPref.PREF_IS_ALERT_CIRCLE, checked);
                } else if (title.equals(context.getResources().getString(R.string.sleep_analysis))) {
                    SharedPref.setValue(context, SharedPref.PREF_IS_SLEEP_ANALYSIS_CIRCLE, checked);
                } else if (title.equals(context.getResources().getString(R.string.activity_analysis))) {
                    SharedPref.setValue(context, SharedPref.PREF_IS_ACTIVITY_ANALYSIS_CIRCLE, checked);
                }
                list.get(i).setSelected(checked);
                notifyDataSetChanged();
            });
        }
        return view;
    }

    static class ViewHolder {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtDesc;
        ToggleButton btnToggle;
    }

}
