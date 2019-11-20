package com.tofa.circular.adapter;

import android.app.Activity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tofa.circular.R;
import com.tofa.circular.listener.OnClickItemListener;

import java.util.List;

public class ListCheckValueAdapter extends ArrayAdapter<Pair<String, String>> {
    private final List<Pair<String, String>> list;
    private final Activity context;
    private int selectedPosition = -1;
    private OnClickItemListener onClickItemListener;


    public ListCheckValueAdapter(Activity context, List<Pair<String, String>> list, int defaultChecked) {
        super(context, R.layout.item_check, list);
        this.context = context;
        this.list = list;
        this.selectedPosition = defaultChecked;
    }

    public ListCheckValueAdapter(Activity context, List<Pair<String, String>> list) {
        super(context, R.layout.item_check, list);
        this.context = context;
        this.list = list;
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
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
    public Pair<String, String> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;

        final Pair<String, String> listCheckItem = list.get(position);
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.item_check, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtText = view.findViewById(R.id.txtNewAlarmLabel);
            viewHolder.chkCheck = view.findViewById(R.id.chkItemCheck);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.chkCheck.setTag(position);
        holder.txtText.setText(listCheckItem.first);
        holder.chkCheck.setChecked(position == selectedPosition);

        holder.chkCheck.setOnClickListener(onStateChangedListener(holder.chkCheck, position));
        view.setOnClickListener(v -> {
            if(!holder.chkCheck.isChecked()) {
                holder.chkCheck.setChecked(true);
                holder.chkCheck.callOnClick();
            }
            if(onClickItemListener != null) {
                onClickItemListener.OnItemClick(listCheckItem);
            }
        });
        return view;
    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return v -> {
            if (checkBox.isChecked()) {
                selectedPosition = position;
            }
            notifyDataSetChanged();
        };
    }

    static class ViewHolder {
        TextView txtText;
        CheckBox chkCheck;
    }
}
