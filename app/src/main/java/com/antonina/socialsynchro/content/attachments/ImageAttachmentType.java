package com.antonina.socialsynchro.content.attachments;

import android.support.v7.app.AppCompatActivity;

import com.antonina.socialsynchro.gui.activities.ImageGalleryActivity;

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
    public String getIconURL() {
        return null;
    }

    @Override
    public Class<? extends AppCompatActivity> getGalleryActivityClass() {
        return ImageGalleryActivity.class;
    }
}
