package com.antonina.socialsynchro.gui;

import android.databinding.BaseObservable;

import com.antonina.socialsynchro.gui.listeners.OnUpdatedListener;

import java.io.Serializable;

public abstract class GUIItem extends BaseObservable implements Serializable {
    private transient boolean selected = false;
    private transient OnUpdatedListener listener;

    public boolean isSelected() { return selected; }

    public void select() { selected = true; }

    public void unselect() { selected = false; }

    public void setListener(OnUpdatedListener listener) {
        this.listener = listener;
    }

    public OnUpdatedListener getListener() {
        return listener;
    }

    protected void notifyListener() {
        if (listener != null)
            listener.onUpdated();
    }
}
