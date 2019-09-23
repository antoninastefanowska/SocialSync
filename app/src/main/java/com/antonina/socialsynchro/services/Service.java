package com.antonina.socialsynchro.services;

import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;

import com.antonina.socialsynchro.gui.GUIItem;

public abstract class Service extends GUIItem {
    public abstract ServiceID getID();

    @Bindable
    public abstract String getName();

    @Bindable
    public abstract String getLogoUrl();

    @Bindable
    public abstract String getColorName();

    public abstract Class<? extends AppCompatActivity> getLoginActivityClass();
}
