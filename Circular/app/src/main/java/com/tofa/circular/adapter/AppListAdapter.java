package com.tofa.circular.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tofa.circular.R;
import com.tofa.circular.customclass.AppAlertModel;

import java.util.ArrayList;

public class AppListAdapter extends BaseAdapter {
    Context context;
    ArrayList<AppAlertModel> arrayList;
    LayoutInflater inflter;
    int listType;
    public AppListAdapter(Context applicationContext, ArrayList<AppAlertModel> arrayList, int listType) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_app_list, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        AppAlertModel model = arrayList.get(i);


        if (listType==1){
            viewHolder.iv_app.setImageResource(arrayList.get(i).imageDrawable);
            if (model.isSelected){
                viewHolder.iv_select.setVisibility(View.VISIBLE);
            }else {
                viewHolder.iv_select.setVisibility(View.INVISIBLE);
            }

        }else {
            viewHolder.iv_select.setVisibility(View.GONE);
            if (model.isSelected){
                viewHolder.iv_app.setImageResource(arrayList.get(i).imageDrawableSelected);
                viewHolder.iv_app.setBackground(context.getResources().getDrawable(R.drawable.back_app_list_item_orange));
            }else {
                viewHolder.iv_app.setImageResource(arrayList.get(i).imageDrawable);
                viewHolder.iv_app.setBackground(context.getResources().getDrawable(R.drawable.back_app_list_item));
            }
        }

        viewHolder.iv_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (AppAlertModel model1:arrayList){
                    if (model1.isSelected){
                        model1.isSelected = false;
                    }
                }
                arrayList.get(i).isSelected = true;
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView iv_app, iv_select;

        public ViewHolder(View view) {
            iv_app = (ImageView)view.findViewById(R.id.iv_app);
            iv_select = (ImageView) view.findViewById(R.id.iv_select);
        }
    }
}
