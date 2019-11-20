package com.tofa.circular.customclass;


public class WeekData {
    private String days;
    private int[] colorList;

    public WeekData(String days){
        this.days = days;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public int[] getColorList() {
        return colorList;
    }

    public void setColorList(int[] colorList) {
        this.colorList = colorList;
    }
}
