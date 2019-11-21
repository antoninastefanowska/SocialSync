package com.antonina.socialsynchro.common.gui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.antonina.socialsynchro.common.gui.listeners.OnUpdatedListener;

import java.io.Serializable;

public abstract class GUIItem extends BaseObservable implements Serializable {
    private transient boolean loading = false;
    private transient boolean selected = false;
    private transient boolean visible = true;
    private transient OnUpdatedListener listener;

    @Bindable
    public boolean isSelected() { return selected; }

    public void select() { selected = true; }

    public void unselect() { selected = false; }

    public void show() {
        visible = true;
        notifyListener();
    }

    public void hide() {
        visible = false;
        notifyListener();
    }

    public void switchVisibility() {
        visible = !visible;
        notifyListener();
    }

    public void switchSelect() {
        selected = !selected;
        notifyListener();
    }

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

    @Bindable
    public boolean isVisible() {
        return visible;
    }
}
