package com.tofa.circular.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tofa.circular.R;

public class AppListAdapter extends BaseAdapter {
    Context context;
    int logos[];
    LayoutInflater inflter;
    public AppListAdapter(Context applicationContext, int[] logos) {
        this.context = applicationContext;
        this.logos = logos;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return logos.length;
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

        viewHolder.iv_app.setImageResource(logos[i]);
        viewHolder.iv_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.iv_select.setVisibility(View.VISIBLE);
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
