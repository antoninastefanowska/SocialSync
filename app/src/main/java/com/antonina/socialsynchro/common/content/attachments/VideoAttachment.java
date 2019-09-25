package com.antonina.socialsynchro.common.content.attachments;

import com.antonina.socialsynchro.common.database.tables.IDatabaseTable;

import java.io.File;

@SuppressWarnings("WeakerAccess")
public class VideoAttachment extends Attachment {
    private int height, width, lengthSeconds;

    public VideoAttachment(IDatabaseTable data) {
        super(data);
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Video));
        // TODO: Wydobyć pozostałe informacje z pliku - dotyczy też pozostałych załączników
    }

    public VideoAttachment(File file) {
        super(file);
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Video));
    }

    public VideoAttachment() {
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Video));
    }

    public int getHeight() { return height; }

    public int getWidth() { return width; }

    public int getLengthSeconds() {
        return lengthSeconds;
    }
}
