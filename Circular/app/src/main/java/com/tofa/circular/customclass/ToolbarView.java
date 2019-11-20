package com.tofa.circular.customclass;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.tofa.circular.R;

public class ToolbarView extends LinearLayout {

    public ToolbarView(Context context) {
        this(context, null);
    }

    public ToolbarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(!isInEditMode()) {
            if (Build.VERSION.SDK_INT >= 23) {
                ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                ((Activity) context).getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.colorGrayBG));
            }
        }

        inflate(getContext(), R.layout.toolbar, this);
        TextView lblToolbarTitle = findViewById(R.id.lblToolbarTitle);
        ImageView btnToolbarBack = findViewById(R.id.btnToolbarBack);
        ImageView imgToolbarIcon = findViewById(R.id.imgToolbarIcon);

        btnToolbarBack.setOnClickListener(view -> ((Activity) context).finish());

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ToolbarView, defStyleAttr, 0);

        try {
            lblToolbarTitle.setText(attributes.getString(R.styleable.ToolbarView_toolbarTitle));
            int toolbarIcon = attributes.getResourceId(R.styleable.ToolbarView_toolbarIcon, -1);
            if (toolbarIcon != -1){
                imgToolbarIcon.setImageDrawable(context.getResources().getDrawable(toolbarIcon));
            }
        } finally {
            attributes.recycle();
        }
    }
}
