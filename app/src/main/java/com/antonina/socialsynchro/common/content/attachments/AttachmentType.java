package com.antonina.socialsynchro.common.content.attachments;

import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;

import com.antonina.socialsynchro.common.gui.GUIItem;

public abstract class AttachmentType extends GUIItem {
    public abstract AttachmentTypeID getID();

    @Bindable
    public abstract String getName();

    @Bindable
    public abstract int getIconID();

    public abstract Class<? extends AppCompatActivity> getGalleryActivityClass();
}
