package com.tofa.circular.customclass;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tofa.circular.R;

public class StatusCardView extends LinearLayout {

    private TextView lblLabel, lblValue;
    private ImageView imgStatusIcon;
    private int statusIcon;
    private String label, value;

    public StatusCardView(Context context) {
        this(context, null);
    }

    public StatusCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(getContext(), R.layout.status_card, this);
        lblLabel = findViewById(R.id.lblStatusLabel);
        lblValue = findViewById(R.id.lblStatusValue);
        imgStatusIcon = findViewById(R.id.imgStatusIcon);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StatusCardView, defStyleAttr, 0);

        try {
            label =  attributes.getString(R.styleable.StatusCardView_labelText);
            value = attributes.getString(R.styleable.StatusCardView_labelValue);
            statusIcon = attributes.getResourceId(R.styleable.StatusCardView_statusIcon, -1);

            lblLabel.setText(label != null ? label:"Label");
            lblValue.setText(value != null ? value:"0");
            if(statusIcon != -1) {
                imgStatusIcon.setImageDrawable(context.getResources().getDrawable(statusIcon));
            }
        } finally {
            attributes.recycle();
        }
    }

    public int getStatusIcon() {
        return statusIcon;
    }

    public void setStatusIcon(int statusIcon) {
        this.statusIcon = statusIcon;
        imgStatusIcon.setImageDrawable(getContext().getResources().getDrawable(statusIcon));
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        lblLabel.setText(label);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        lblValue.setText(value);
    }
}
