package com.tofa.circular.model;

public class AppAlertModel {
    public int imageDrawable, imageDrawableSelected;
    public boolean isSelected;
    public String alertType;
    public int repeatCount;
    public String contactName, contactNumber;

    public AppAlertModel(int imageDrawableSelected, int imageDrawable, boolean isSelected, int repeatCount) {
        this.imageDrawable = imageDrawable;
        this.imageDrawableSelected = imageDrawableSelected;
        this.isSelected = isSelected;
        this.repeatCount = repeatCount;
    }

    public AppAlertModel(int imageDrawable, String alertType, int repeatCount, String contact, String contactNumber) {
        this.imageDrawable = imageDrawable;
        this.alertType = alertType;
        this.repeatCount = repeatCount;
        this.contactName = contact;
        this.contactNumber = contactNumber;
    }

    public AppAlertModel(int imageDrawable, String alertType,boolean isSelected) {
        this.imageDrawable = imageDrawable;
        this.isSelected = isSelected;
        this.alertType = alertType;
    }
}
