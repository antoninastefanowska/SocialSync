package com.antonina.socialsynchro.content.attachments;

import com.antonina.socialsynchro.database.tables.IDatabaseTable;

public class ImageAttachment extends Attachment {
    private int height, width;

    public ImageAttachment(IDatabaseTable data) {
        super(data);
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
