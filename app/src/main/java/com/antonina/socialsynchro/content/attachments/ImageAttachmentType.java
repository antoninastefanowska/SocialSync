package com.antonina.socialsynchro.content.attachments;

public class ImageAttachmentType implements IAttachmentType {
    private static ImageAttachmentType instance;

    public static ImageAttachmentType getInstance() {
        if (instance == null)
            instance = new ImageAttachmentType();
        return instance;
    }

    private ImageAttachmentType() { }

    @Override
    public AttachmentTypeID getID() {
        return AttachmentTypeID.Image;
    }

    @Override
    public String getName() {
        return "Image";
    }

    @Override
    public String getIconURL() {
        return null;
    }
}
