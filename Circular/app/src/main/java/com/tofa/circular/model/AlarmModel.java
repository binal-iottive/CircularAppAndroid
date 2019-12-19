package com.tofa.circular.model;

import java.io.Serializable;

public class AlarmModel implements Serializable {
    public String alarmId, alrmTime, alrmTitle, alrmRepeatDays, alrmColor , VibrationLevel;
    public boolean isActive;

    public AlarmModel(String alarmId, String alrmTime, String alrmTitle, String alrmRepeatDays, String alrmColor, String isVibrate, boolean isActive) {
        this.alarmId = alarmId;
        this.alrmTime = alrmTime;
        this.alrmTitle = alrmTitle;
        this.alrmRepeatDays = alrmRepeatDays;
        this.alrmColor = alrmColor;
        this.VibrationLevel = isVibrate;
        this.isActive = isActive;
    }
}
