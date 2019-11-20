package com.tofa.circular.customclass;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.tofa.circular.R;

public class ProgressCardView extends LinearLayout {

    public static class ProgressValueFormater {
        public String getFormattedValue(int value) {
            return String.valueOf(value);
        }
    }

    private TextView lblLabel;
    private TextView lblPrgressValue;
    private TextView lblDetailTitle;
    private TextView lblDetail;
    private ProgressBar pgProgress;
    private RelativeLayout lytDetail;
    private String labelText;
    private String detailText;
    private int progressMax;
    private int progress;
    private ProgressValueFormater valueFormatter;
    private Drawable bgProgress;
    private int detailBackground;

    public ProgressCardView(Context context) {
        this(context, null);
    }

    public ProgressCardView (Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ProgressCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        inflate(getContext(), R.layout.progress_card, this);
        this.lblLabel = findViewById(R.id.lblProgressLabel);
        this.lblDetailTitle = findViewById(R.id.lblDetailTitle);
        this.lblDetail = findViewById(R.id.lblDetail);
        this.lblPrgressValue = findViewById(R.id.lblProgressValue);
        this.pgProgress = findViewById(R.id.pgProgress);
        this.lytDetail = findViewById(R.id.lytProgressDetail);
        Button btnClose = findViewById(R.id.btnDetailClose);
        lytDetail.setVisibility(GONE);

        CardView crdProgress = findViewById(R.id.crdProgress);
        crdProgress.setOnClickListener(view -> {
            if (lytDetail.getVisibility() == GONE) {
                lytDetail.setVisibility(VISIBLE);
            } else {
                lytDetail.setVisibility(GONE);
            }
        });

        btnClose.setOnClickListener(view -> lytDetail.setVisibility(GONE));

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressCardView, defStyle, 0);

        try {
            labelText = attributes.getString(R.styleable.ProgressCardView_labelText);
            detailText = attributes.getString(R.styleable.ProgressCardView_detailText);
            progressMax = attributes.getInt(R.styleable.ProgressCardView_progressMax, 100);
            progress = attributes.getInt(R.styleable.ProgressCardView_progress, 0);
            detailBackground = attributes.getResourceId(R.styleable.ProgressCardView_detailBackground, R.drawable.bg_round_blue);
            int mBgProgress = attributes.getResourceId(R.styleable.ProgressCardView_progressBG, -1);

            if(mBgProgress != -1) {
                bgProgress = context.getResources().getDrawable(mBgProgress);
                pgProgress.setProgressDrawable(bgProgress);
            }

            lblLabel.setText(labelText != null ? labelText:"Label");
            lblDetailTitle.setText(labelText != null ? labelText:"Titel");
            lblDetail.setText(detailText != null ? detailText:"Detail");
            pgProgress.setMax(progressMax);
            pgProgress.setProgress(progress);

            lytDetail.setBackground(context.getResources().getDrawable(detailBackground));

            valueFormatter = new ProgressValueFormater();

            lblPrgressValue.setText(valueFormatter.getFormattedValue(progress));
        } finally {
            attributes.recycle();
        }
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
        this.lblLabel.setText(labelText);
    }

    public String getDetailText() {
        return detailText;
    }

    public void setDetailText(String detailText) {
        this.detailText = detailText;
        lblDetail.setText(detailText);
    }

    public void setProgressMax(int progressMax) {
        this.progressMax = progressMax;
        this.pgProgress.setMax(progressMax);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        this.pgProgress.setProgress(progress);
        this.lblPrgressValue.setText(valueFormatter.getFormattedValue(progress));
    }

    public void setValueFormatter(ProgressValueFormater valueFormatter) {
        this.valueFormatter = valueFormatter;
        this.lblPrgressValue.setText(valueFormatter.getFormattedValue(progress));
    }

    public String getLabelText() {
        return labelText;
    }

    public int getProgressMax() {
        return progressMax;
    }

    public int getProgress() {
        return progress;
    }

    public void setBgProgress(Drawable bgProgress) {
        this.bgProgress = bgProgress;
        this.pgProgress.setProgressDrawable(bgProgress);
    }
}
