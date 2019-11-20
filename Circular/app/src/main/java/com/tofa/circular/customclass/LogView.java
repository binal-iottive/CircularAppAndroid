package com.tofa.circular.customclass;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tofa.circular.R;

import java.util.List;

public class LogView extends RelativeLayout {

    private TextView lblLogTitle, lblStartLog, lblEndLog, lblLogTime;
    private Button btnLogClose;
    private TimeProgressView tpLog;
    private List<Segment> segments;

    private String title, startLabel, endLabel, startValue, endValue, timeText;

    public LogView(Context context) {
        this(context, null);
    }

    public LogView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public LogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(getContext(), R.layout.logs_view, this);
        lblLogTitle = findViewById(R.id.lblLogTitle);
        lblStartLog = findViewById(R.id.lblStartLog);
        lblEndLog = findViewById(R.id.lblEndLog);
        lblLogTime = findViewById(R.id.lblLogTime);
        btnLogClose = findViewById(R.id.btnLogClose);
        tpLog = findViewById(R.id.tpLog);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LogView, defStyleAttr, 0);

        try {
            title = attributes.getString(R.styleable.LogView_logTitle);
            startLabel = attributes.getString(R.styleable.LogView_startText);
            endLabel = attributes.getString(R.styleable.LogView_endText);
            startValue = attributes.getString(R.styleable.LogView_startValue);
            endValue = attributes.getString(R.styleable.LogView_endValue);
            timeText = attributes.getString(R.styleable.LogView_timeText);

            lblLogTitle.setText(title != null ? title:"Title");
            lblStartLog.setText(startLabel != null ? startLabel:"Start");
            lblEndLog.setText(endLabel != null ? endLabel:"Stop");
            lblLogTime.setText(timeText != null ? timeText:"Time");

            tpLog.setStartText(startValue != null ? startValue:"0");
            tpLog.setEndText(endValue != null ? endValue:"100");
        } finally {
            attributes.recycle();
        }
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
        tpLog.setSegments(segments);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        lblLogTitle.setText(title);
    }

    public String getStartLabel() {
        return startLabel;
    }

    public void setStartLabel(String startLabel) {
        this.startLabel = startLabel;
        lblStartLog.setText(startLabel);
    }

    public String getEndLabel() {
        return endLabel;
    }

    public void setEndLabel(String endLabel) {
        this.endLabel = endLabel;
        lblEndLog.setText(endLabel);
    }

    public String getStartValue() {
        return startValue;
    }

    public void setStartValue(String startValue) {
        this.startValue = startValue;
        tpLog.setStartText(startValue);
    }

    public String getEndValue() {
        return endValue;
    }

    public void setEndValue(String endValue) {
        this.endValue = endValue;
        tpLog.setEndText(endValue);
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
        lblLogTime.setText(timeText);
    }
}
