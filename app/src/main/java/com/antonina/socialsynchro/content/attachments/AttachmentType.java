package com.antonina.socialsynchro.content.attachments;

import android.databinding.Bindable;

import com.antonina.socialsynchro.gui.SelectableItem;

import java.io.Serializable;

public abstract class AttachmentType extends SelectableItem implements Serializable {
    public abstract AttachmentTypeID getID();

    @Bindable
    public abstract String getName();

    @Bindable
    public abstract String getIconURL();
}
