package com.antonina.socialsynchro.content.attachments;

import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;

import com.antonina.socialsynchro.gui.GUIItem;

public abstract class AttachmentType extends GUIItem {
    public abstract AttachmentTypeID getID();

    @Bindable
    public abstract String getName();

    @Bindable
    public abstract String getIconURL();

    public abstract Class<? extends AppCompatActivity> getGalleryActivityClass();
}
