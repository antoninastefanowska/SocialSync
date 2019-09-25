package com.antonina.socialsynchro.common.content.attachments;

import com.antonina.socialsynchro.common.database.tables.IDatabaseTable;

import java.io.File;

@SuppressWarnings("WeakerAccess")
public class AudioAttachment extends Attachment {
    private int lengthSeconds;

    public AudioAttachment(IDatabaseTable data) {
        super(data);
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Audio));
    }

    public AudioAttachment(File file) {
        super(file);
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Audio));
    }

    public AudioAttachment() {
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Audio));
    }

    public int getLengthSeconds() { return lengthSeconds; }
}
