package com.antonina.socialsynchro.gui;

import android.databinding.BaseObservable;

import java.io.Serializable;

public abstract class SelectableItem extends BaseObservable implements Serializable {
    private boolean checked = false;

    public boolean isChecked() { return checked; }

    public void check() { checked = true; }

    public void uncheck() { checked = false; }

    public void setChecked(boolean checked) { this.checked = checked; }
}
