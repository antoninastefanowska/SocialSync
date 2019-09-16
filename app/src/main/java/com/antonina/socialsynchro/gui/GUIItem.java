package com.antonina.socialsynchro.gui;

import android.databinding.BaseObservable;

import com.antonina.socialsynchro.gui.listeners.OnUpdatedListener;

import java.io.Serializable;

public abstract class GUIItem extends BaseObservable {
    private boolean selected = false;
    protected OnUpdatedListener listener;

    public boolean isSelected() { return selected; }

    public void select() { selected = true; }

    public void unselect() { selected = false; }

    public void setSelected(boolean selected) { this.selected = selected; }

    public void setListener(OnUpdatedListener listener) {
        this.listener = listener;
    }

    public OnUpdatedListener getListener() {
        return listener;
    }
}
