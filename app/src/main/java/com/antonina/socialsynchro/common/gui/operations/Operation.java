package com.antonina.socialsynchro.common.gui.operations;

import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.antonina.socialsynchro.common.gui.GUIItem;

public abstract class Operation extends GUIItem {
    private boolean enabled;
    private transient View.OnClickListener onClickListener;

    public Operation() {
        enabled = true;
    }

    public abstract OperationID getID();

    @Bindable
    public abstract String getName();

    @Bindable
    public abstract Drawable getIcon();

    @Bindable
    public abstract int getIconID();

    public boolean isEnabled() {
        return enabled;
    }

    public void disable() {
        enabled = false;
        notifyGUI();
    }

    public void enable() {
        enabled = true;
        notifyGUI();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyGUI();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Bindable
    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }
}
