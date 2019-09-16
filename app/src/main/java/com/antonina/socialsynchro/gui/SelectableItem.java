package com.antonina.socialsynchro.gui;

import android.databinding.BaseObservable;

import java.io.Serializable;

public abstract class SelectableItem extends BaseObservable {
    private boolean selected = false;

    public boolean isSelected() { return selected; }

    public void select() { selected = true; }

    public void unselect() { selected = false; }

    public void setSelected(boolean selected) { this.selected = selected; }
}
