package com.antonina.socialsynchro.content.attachments;

public class AttachmentTypes {
    private static final AttachmentType[] attachments = new AttachmentType[AttachmentTypeID.values().length];
    private static boolean initialized = false;

    private AttachmentTypes() { }

    private static void init() {
        attachments[AttachmentTypeID.Image.ordinal()] = ImageAttachmentType.getInstance();
        attachments[AttachmentTypeID.Video.ordinal()] = VideoAttachmentType.getInstance();
        attachments[AttachmentTypeID.Audio.ordinal()] = AudioAttachmentType.getInstance();
        initialized = true;
    }

    public static AttachmentType getAttachmentType(AttachmentTypeID attachmentTypeID) {
        if (!initialized)
            init();
        return attachments[attachmentTypeID.ordinal()];
    }

    public static AttachmentType getAttachmentType(int attachmentTypeID) {
        if (!initialized)
            init();
        return attachments[attachmentTypeID];
    }

    public static AttachmentType[] getAttachmentTypes() {
        if (!initialized)
            init();
        return attachments;
    }
}
