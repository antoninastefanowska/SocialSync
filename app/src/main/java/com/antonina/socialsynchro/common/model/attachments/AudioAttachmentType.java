package com.antonina.socialsynchro.common.model.attachments;

import android.support.v7.app.AppCompatActivity;

import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

public class AudioAttachmentType extends AttachmentType {
    private static AudioAttachmentType instance;

    public static AudioAttachmentType getInstance() {
        if (instance == null)
            instance = new AudioAttachmentType();
        return instance;
    }

    private AudioAttachmentType() { }

    @Override
    public AttachmentTypeID getID() {
        return null;
    }

    @Override
    public String getName() {
        return "Audio";
    }

    @Override
    public int getIconID() {
        return -1;
    }

    @Override
    public Attachment createAttachment(IDatabaseRow data) {
        return new AudioAttachment(data);
    }

    @Override
    public Attachment createNewAttachment() {
        return new AudioAttachment();
    }

    @Override
    public Class<? extends AppCompatActivity> getGalleryActivityClass() {
        return null;
    }
}
