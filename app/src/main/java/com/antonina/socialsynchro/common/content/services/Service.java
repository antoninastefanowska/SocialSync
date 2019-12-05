package com.antonina.socialsynchro.common.content.services;

import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

import com.antonina.socialsynchro.common.content.accounts.LoginFlow;
import com.antonina.socialsynchro.common.gui.GUIItem;
import com.antonina.socialsynchro.common.gui.activities.LoginActivity;

public abstract class Service extends GUIItem {
    public abstract ServiceID getID();

    @Bindable
    public abstract String getName();

    public abstract int getIconID();

    @Bindable
    public abstract Drawable getBanner();

    @Bindable
    public abstract int getColor();

    @Bindable
    public abstract int getFontColor();

    @Bindable
    public abstract Drawable getPanelBackground();

    public abstract int getPanelBackgroundID();

    @Bindable
    public abstract Drawable getBackground();

    public abstract LoginFlow createLoginFlow(LoginActivity context);
}
