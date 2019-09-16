package com.antonina.socialsynchro.content.attachments;

public class VideoAttachmentType extends AttachmentType {
    private static VideoAttachmentType instance;

    public static VideoAttachmentType getInstance() {
        if (instance == null)
            instance = new VideoAttachmentType();
        return instance;
    }

    private VideoAttachmentType() { }

    @Override
    public AttachmentTypeID getID() {
        return AttachmentTypeID.Video;
    }

    @Override
    public String getName() {
        return "Video";
    }

    @Override
    public String getIconURL() {
        return null;
    }
}
