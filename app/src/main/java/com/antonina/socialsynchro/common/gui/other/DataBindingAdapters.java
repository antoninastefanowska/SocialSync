package com.antonina.socialsynchro.common.gui.other;

import android.databinding.BindingAdapter;
import android.widget.Button;

public class DataBindingAdapters {

    @BindingAdapter("android:background")
    public static void setButtonBackground(Button button, int resourceID) {
        button.setBackgroundResource(resourceID);
    }
}
