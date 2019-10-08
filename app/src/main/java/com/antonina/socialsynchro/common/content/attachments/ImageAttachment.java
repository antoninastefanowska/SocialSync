package com.antonina.socialsynchro.common.content.attachments;

import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

import java.io.File;

@SuppressWarnings("WeakerAccess")
public class ImageAttachment extends Attachment {
    private int height, width;

    public ImageAttachment(IDatabaseRow data) {
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
