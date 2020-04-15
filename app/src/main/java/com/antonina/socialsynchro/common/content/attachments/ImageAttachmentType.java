package com.antonina.socialsynchro.common.content.attachments;

import android.support.v7.app.AppCompatActivity;

import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.activities.ImageGalleryActivity;

public class ImageAttachmentType extends AttachmentType {
    private static ImageAttachmentType instance;

    public static ImageAttachmentType getInstance() {
        if (instance == null)
            instance = new ImageAttachmentType();
        return instance;
    }

    private ImageAttachmentType() { }

    @Override
    public AttachmentTypeID getID() {
        return AttachmentTypeID.Image;
    }

    @Override
    public String getName() {
        return "Image";
    }

    @Override
    public int getIconID() {
        return -1;
    }

    @Override
    public Attachment createAttachment(IDatabaseRow data) {
        return new ImageAttachment(data);
    }

    @Override
    public Attachment createNewAttachment() {
        return new ImageAttachment();
    }

    @Override
    public Class<? extends AppCompatActivity> getGalleryActivityClass() {
        return ImageGalleryActivity.class;
    }
}
