package com.antonina.socialsynchro.content.attachments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.antonina.socialsynchro.database.tables.IDatabaseTable;

import java.io.File;

public class ImageAttachment extends Attachment {
    private int height, width;

    public ImageAttachment(IDatabaseTable data) {
        super(data);
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Image));
    }

    public ImageAttachment(File file) {
        super(file);
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Image));
    }

    public ImageAttachment() {
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Image));
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
