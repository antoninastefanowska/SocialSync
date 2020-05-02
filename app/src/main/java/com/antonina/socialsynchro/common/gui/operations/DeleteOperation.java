package com.antonina.socialsynchro.common.gui.operations;

import android.graphics.drawable.Drawable;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.SocialSynchro;

public class DeleteOperation extends Operation {
    @Override
    public OperationID getID() {
        return OperationID.DELETE;
    }

    @Override
    public String getName() {
        return SocialSynchro.getInstance().getResources().getString(R.string.remove);
    }

    @Override
    public Drawable getIcon() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.icon_cross);
    }

    @Override
    public int getIconID() {
        return R.drawable.icon_cross;
    }
}
