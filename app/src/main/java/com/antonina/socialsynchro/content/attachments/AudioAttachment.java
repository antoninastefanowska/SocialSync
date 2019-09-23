package com.antonina.socialsynchro.content.attachments;

import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.services.Service;

import java.io.File;
import java.util.Date;

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
