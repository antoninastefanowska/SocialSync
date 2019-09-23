package com.antonina.socialsynchro.content.attachments;

import android.databinding.Bindable;

import com.antonina.socialsynchro.gui.GUIItem;

public abstract class AttachmentType extends GUIItem {
    public abstract AttachmentTypeID getID();

    @Bindable
    public abstract String getName();

    @Bindable
    public abstract String getIconURL();
}
