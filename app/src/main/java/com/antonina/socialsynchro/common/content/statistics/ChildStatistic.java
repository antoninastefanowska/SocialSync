package com.antonina.socialsynchro.common.content.statistics;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ChildStatistic extends Statistic {
    private String name;
    private int value;
    private int barBackgroundID;
    private boolean isLast;

    private ChildGroupStatistic group;
    private int groupIndex;

    private transient Drawable barBackground;

    public ChildStatistic(String name, int value, int barBackgroundID) {
        this.name = name;
        this.value = value;
        this.barBackgroundID = barBackgroundID;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public Drawable getBarBackground() {
        return barBackground;
    }

    public void setGroup(ChildGroupStatistic group) {
        this.group = group;
        this.groupIndex = group.getChildStatistics().indexOf(this);
    }

    public Drawable getIcon() {
        if (group == null)
            return null;
        else
            return group.getIcon();
    }

    public String getIconCaption() {
        if (group == null)
            return null;
        else
            return group.getIconCaption();
    }

    public void setLast(boolean isLast) {
        this.isLast = isLast;
    }

    public boolean isLast() {
        return isLast;
    }

    public void loadBarBackground(Context context) {
        barBackground = context.getResources().getDrawable(barBackgroundID);
    }

    public int getGroupIndex() {
        return groupIndex;
    }
}
