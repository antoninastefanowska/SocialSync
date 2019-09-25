package com.antonina.socialsynchro.common.gui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.antonina.socialsynchro.common.gui.listeners.OnUpdatedListener;

import java.io.Serializable;

public abstract class GUIItem extends BaseObservable implements Serializable {
    private transient boolean loading = false;
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

    public void notifyListener() {
        if (listener != null)
            listener.onUpdated(this);
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyListener();
    }

    @Bindable
    public boolean isLoading() {
        return loading;
    }
}
