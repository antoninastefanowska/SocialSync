package com.antonina.socialsynchro.common.gui.operations;

import android.graphics.drawable.Drawable;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.SocialSynchro;

public class SynchronizeOperation extends Operation {
    @Override
    public OperationID getID() {
        return OperationID.SYNCHRONIZE;
    }

    @Override
    public String getName() {
        return SocialSynchro.getInstance().getResources().getString(R.string.synchronize);
    }

    @Override
    public Drawable getIcon() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.icon_refresh);
    }

    @Override
    public int getIconID() {
        return R.drawable.icon_refresh;
    }
}
