package com.tofa.circular.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tofa.circular.R;
import com.tofa.circular.listener.OnClickItemListener;

import java.util.ArrayList;
import java.util.List;

public class ListCheckAdapter extends ArrayAdapter<String> {
    private final List<String> list;
    private final Activity context;
    private int selectedPosition = -1;
    private OnClickItemListener onClickItemListener;
    private Boolean isSmall = false;
    private Boolean multiSelect = false;
    private List<Boolean> selectedList;
    private int[] separatorList;

    public ListCheckAdapter(Activity context, List<String> list, int defaultChecked) {
        super(context, R.layout.item_check, list);
        this.context = context;
        this.list = list;
        this.selectedPosition = defaultChecked;
        selectedList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            selectedList.add(false);
        }
    }

    public void setSelectedList(int index,boolean value)
    {
        if ( index >= 0 && index<selectedList.size() )
            selectedList.set(index,value);
    }

    public ListCheckAdapter(Activity context, List<String> list, List<Boolean> defaultChecked) {
        super(context, R.layout.item_check, list);
        this.context = context;
        this.list = list;
        selectedList = defaultChecked;
    }

    public ListCheckAdapter(Activity context, List<String> list) {
        super(context, R.layout.item_check, list);
        this.context = context;
        this.list = list;
        selectedList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            selectedList.add(false);
        }
    }

    public ListCheckAdapter(Activity context, List<String> list, Boolean isSmall, int defaultChecked) {
        super(context, R.layout.item_check, list);
        this.context = context;
        this.list = list;
        this.selectedPosition = defaultChecked;
        this.isSmall = isSmall;
        selectedList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            selectedList.add(false);
        }
    }

    public ListCheckAdapter(Activity context, List<String> list, Boolean isSmall, List<Boolean> defaultChecked) {
        super(context, R.layout.item_check, list);
        this.context = context;
        this.list = list;
        selectedList = defaultChecked;
        this.isSmall = isSmall;
    }

    public ListCheckAdapter(Activity context, List<String> list, Boolean isSmall) {
        super(context, R.layout.item_check, list);
        this.context = context;
        this.list = list;
        this.isSmall = isSmall;
        selectedList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            selectedList.add(false);
        }
    }

    public void setSeparatorList(int[] separatorList){
        this.separatorList = separatorList;
    }

    public void setMultiSelect(Boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public int getSelectedPosition(){
        return selectedPosition;
    }

    public List<Integer> getIntSelectedList() {
        List<Integer> integerList = new ArrayList<>();
        for(int i = 0; i < selectedList.size(); i++){
            integerList.add(i);
        }
        return integerList;
    }

    public List<Boolean> getSelectedList() {
        return selectedList;
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
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;

        final String listCheckItem = list.get(position);
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            int layout;
            if (isSmall) {
                layout = R.layout.item_check_small;
            } else {
                layout = R.layout.item_check;
            }

            view = inflator.inflate(layout, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.txtText = view.findViewById(R.id.txtItemCheckLabel);
            viewHolder.chkCheck = view.findViewById(R.id.chkItemCheck);
            if(separatorList != null) {
                for (int i : separatorList)
                    if (i == position) {
                        View viewSp = view.findViewById(R.id.viewItemCheckSeparator);
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewSp.getLayoutParams();
                        lp.height = 50;
                        break;
                    }
            }
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.txtText.setText(listCheckItem);
        if (!multiSelect) {
            holder.chkCheck.setTag(position);
            holder.chkCheck.setChecked(position == selectedPosition);
        } else {
            holder.chkCheck.setChecked(selectedList.get(position));
        }

        holder.chkCheck.setOnClickListener(onStateChangedListener(holder.chkCheck, position));

        view.setOnClickListener(vw -> {
            if (multiSelect) {
                holder.chkCheck.setChecked(!holder.chkCheck.isChecked());
            } else if (!holder.chkCheck.isChecked()) {
                holder.chkCheck.setChecked(true);
            }
            holder.chkCheck.callOnClick();
            if (onClickItemListener != null) {
                onClickItemListener.OnItemClick(listCheckItem);
            }
        });
        return view;
    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return v -> {
            if (multiSelect) {
                selectedList.set(position, checkBox.isChecked());
            } else if (checkBox.isChecked()) {
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
