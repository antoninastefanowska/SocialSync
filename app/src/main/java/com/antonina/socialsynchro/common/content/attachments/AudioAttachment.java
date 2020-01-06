package com.antonina.socialsynchro.common.content.attachments;

import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

import java.io.File;

@SuppressWarnings("WeakerAccess")
public class AudioAttachment extends Attachment {
    public AudioAttachment(IDatabaseRow data) {
        super(data);
    }

    public AudioAttachment(File file) {
        super(file);
    }

    public AudioAttachment() {
    }

    @Override
    protected void extractMetadata() {
    }
}
