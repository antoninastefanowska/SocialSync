package com.antonina.socialsynchro.content.attachments;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

public abstract class AttachmentType extends BaseObservable implements Serializable {
    public abstract AttachmentTypeID getID();

    @Bindable
    public abstract String getName();

    @Bindable
    public abstract String getIconURL();
}
