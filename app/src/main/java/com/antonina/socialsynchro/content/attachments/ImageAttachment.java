package com.antonina.socialsynchro.content.attachments;

import com.antonina.socialsynchro.database.tables.ITable;

public class ImageAttachment extends Attachment {
    private int height, width;

    public ImageAttachment(ITable data) {
        super(data);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
