package com.antonina.socialsynchro.common.gui.operations;

import android.graphics.drawable.Drawable;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.SocialSynchro;

public class StatisticsOperation extends Operation {
    @Override
    public OperationID getID() {
        return OperationID.STATISTICS;
    }

    @Override
    public String getName() {
        return SocialSynchro.getInstance().getResources().getString(R.string.statistics);
    }

    @Override
    public Drawable getIcon() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.icon_chart);
    }

    @Override
    public int getIconID() {
        return R.drawable.icon_chart;
    }
}
