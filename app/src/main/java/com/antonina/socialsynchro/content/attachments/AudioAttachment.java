package com.antonina.socialsynchro.content.attachments;

import com.antonina.socialsynchro.database.tables.ITable;

public class AudioAttachment extends Attachment {
    private int lengthSeconds;

    public AudioAttachment(ITable data) {
        super(data);
    }

    public int getLengthSeconds() { return lengthSeconds; }
}
