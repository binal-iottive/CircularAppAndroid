package com.tofa.circular.customclass;

import android.graphics.drawable.Drawable;

public class AddCircleItem {
    private String title, desc, toggleon, toggleoff;
    private Boolean selected, isHeader;
    private Drawable icon;

    public AddCircleItem(){
        selected = true;
        toggleon = "On";
        toggleoff = "Off";
        isHeader = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getToggleon() {
        return toggleon;
    }

    public void setToggleon(String toggleon) {
        this.toggleon = toggleon;
    }

    public String getToggleoff() {
        return toggleoff;
    }

    public void setToggleoff(String toggleoff) {
        this.toggleoff = toggleoff;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Boolean isHeaderItem() {
        return isHeader;
    }

    public void setHeader(Boolean header) {
        isHeader = header;
    }
}
