package com.antonina.socialsynchro.common.content.attachments;

import com.antonina.socialsynchro.common.database.IDatabaseEntityFactory;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.AttachmentRow;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

public class AttachmentFactory implements IDatabaseEntityFactory {
    private static AttachmentFactory instance;

    public static AttachmentFactory getInstance() {
        if (instance == null)
            instance = new AttachmentFactory();
        return instance;
    }

    private AttachmentFactory() { }

    @Override
    public IDatabaseEntity createFromDatabaseRow(IDatabaseRow data) {
        AttachmentRow attachmentData = (AttachmentRow)data;
        AttachmentTypeID attachmentTypeID = AttachmentTypeID.values()[attachmentData.attachmentTypeID];

        switch (attachmentTypeID) {
            case Image:
                return new ImageAttachment(data);
            case Video:
                return new VideoAttachment(data);
            default:
                return null;
        }
    }

    public Attachment create(AttachmentTypeID typeID) {
        switch (typeID) {
            case Image:
                return new ImageAttachment();
            case Video:
                return new VideoAttachment();
            default:
                return null;
        }
    }
}
