package com.antonina.socialsynchro.content.attachments;

public class ImageAttachment implements IAttachment {
    private Object image;

    public int getSizeKb() {
        return 0;
    }

    public int getHeight() {
        return 0;
    }

    public int getWidth() {
        return 0;
    }

    @Override
    public AttachmentID getType() {
        return AttachmentID.Image;
    }
}
