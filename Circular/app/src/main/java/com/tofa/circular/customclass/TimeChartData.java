package com.tofa.circular.customclass;

import android.annotation.SuppressLint;

public class TimeChartData {
    private float start;
    private float end;
    private float targetStart;
    private float targetEnd;
    private TimeChartDataSeparatorType separatorType = TimeChartDataSeparatorType.BOTH;
    private String startValueText;
    private String endValueText;
    private String startLabelText;
    private String endLabelText;
    private int startHourValue, startMinuteValue, endHourValue, endMinuteValue;

    public TimeChartData(float startDegree, float endDegree, TimeChartData targetPie) {
        start = startDegree;
        end = endDegree;
        targetStart = targetPie.getStart();
        targetEnd = targetPie.getEnd();
    }

    @SuppressLint("DefaultLocale")
    public TimeChartData(int startHour, int startMin, int endHour, int endMin, TimeChartDataSeparatorType separatorType) {
        this.separatorType = separatorType;
        start = 270 + startHour * 15 + startMin * 15 / 60;
        end = 270 + endHour * 15 + endMin * 15 / 60;
        while (end < start) {
            end += 360;
        }
        startValueText = String.format("%02d:%02d", startHour, startMin);
        endValueText = String.format("%02d:%02d", endHour, endMin);
        startHourValue = startHour;
        startMinuteValue = startMin;
        endHourValue = endHour;
        endMinuteValue = endMin;
    }

    TimeChartData setTarget(TimeChartData targetPie) {
        targetStart = targetPie.getStart();
        targetEnd = targetPie.getEnd();
        return this;
    }

    public TimeChartDataSeparatorType getSeparatorType() {
        return separatorType;
    }

    public void setSeparatorType(TimeChartDataSeparatorType separatorType) {
        this.separatorType = separatorType;
    }

    float getSweep() {
        return end - start;
    }

    float getStart() {
        return start;
    }

    float getEnd() {
        return end;
    }

    public String getStartValueText() {
        return startValueText;
    }

    public String getEndValueText() {
        return endValueText;
    }

    public String getStartLabelText() {
        return startLabelText;
    }

    public void setStartLabelText(String startLabelText) {
        this.startLabelText = startLabelText;
    }

    public String getEndLabelText() {
        return endLabelText;
    }

    public void setEndLabelText(String endLabelText) {
        this.endLabelText = endLabelText;
    }

    public int getStartHourValue() {
        return startHourValue;
    }

    public int getStartMinuteValue() {
        return startMinuteValue;
    }

    public int getEndHourValue() {
        return endHourValue;
    }

    public int getEndMinuteValue() {
        return endMinuteValue;
    }
}
