package com.antonina.socialsynchro.common.model.attachments;

import android.support.v7.app.AppCompatActivity;

import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.activities.VideoGalleryActivity;

public class VideoAttachmentType extends AttachmentType {
    private static VideoAttachmentType instance;

    public static VideoAttachmentType getInstance() {
        if (instance == null)
            instance = new VideoAttachmentType();
        return instance;
    }

    private VideoAttachmentType() { }

    @Override
    public AttachmentTypeID getID() {
        return AttachmentTypeID.Video;
    }

    @Override
    public String getName() {
        return "Video";
    }

    @Override
    public int getIconID() {
        return -1;
    }

    @Override
    public Attachment createAttachment(IDatabaseRow data) {
        return new VideoAttachment(data);
    }

    @Override
    public Attachment createNewAttachment() {
        return new VideoAttachment();
    }

    @Override
    public Class<? extends AppCompatActivity> getGalleryActivityClass() {
        return VideoGalleryActivity.class;
    }
}
