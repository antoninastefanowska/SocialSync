package com.antonina.socialsynchro.common.gui;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.antonina.socialsynchro.common.gui.listeners.OnUpdatedListener;
import com.antonina.socialsynchro.common.gui.messages.Message;

import java.io.Serializable;
import java.util.List;

public abstract class GUIItem extends BaseObservable implements Serializable {
    private transient boolean loading = false;
    private transient boolean selected = false;
    private transient boolean visible = true;
    private transient OnUpdatedListener listener;
    private transient List<Message> messages;

    @Bindable
    public boolean isSelected() { return selected; }

    public void select() { selected = true; }

    public void unselect() { selected = false; }

    public void show() {
        visible = true;
        notifyGUI();
    }

    public void hide() {
        visible = false;
        notifyGUI();
    }

    public void switchVisibility() {
        visible = !visible;
        notifyGUI();
    }

    public void switchSelect() {
        selected = !selected;
        notifyGUI();
    }

    public void setListener(OnUpdatedListener listener) {
        this.listener = listener;
    }

    public OnUpdatedListener getListener() {
        return listener;
    }

    public void notifyGUI() {
        if (listener != null)
            listener.onUpdated(this);
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        notifyGUI();
    }

    @Bindable
    public boolean isLoading() {
        return loading;
    }

    @Bindable
    public boolean isVisible() {
        return visible;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void removeMessage(Message message) {
        messages.remove(message);
    }
}
