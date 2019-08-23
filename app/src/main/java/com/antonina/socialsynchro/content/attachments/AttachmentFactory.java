package com.antonina.socialsynchro.content.attachments;

import com.antonina.socialsynchro.database.IDatabaseEntityFactory;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.tables.AttachmentTable;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;

public class AttachmentFactory implements IDatabaseEntityFactory {
    private static AttachmentFactory instance;

    public static AttachmentFactory getInstance() {
        if (instance == null)
            instance = new AttachmentFactory();
        return instance;
    }

    private AttachmentFactory() { }

    @Override
    public IDatabaseEntity createFromData(IDatabaseTable data) {
        AttachmentTable attachmentData = (AttachmentTable)data;
        AttachmentTypeID attachmentTypeID = AttachmentTypeID.values()[(int)attachmentData.attachmentTypeID];

        switch(attachmentTypeID) {
            case Image:
                return new ImageAttachment(data);
            case Video:
                return new VideoAttachment(data);
            case Audio:
                return new AudioAttachment(data);
            default:
                return null;
        }
    }
}
