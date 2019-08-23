package com.antonina.socialsynchro.content.attachments;

import com.antonina.socialsynchro.database.tables.IDatabaseTable;

public class AudioAttachment extends Attachment {
    private int lengthSeconds;

    public AudioAttachment(IDatabaseTable data) {
        super(data);
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Audio));
    }

    public AudioAttachment() {
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Audio));
    }

    public int getLengthSeconds() { return lengthSeconds; }
}
