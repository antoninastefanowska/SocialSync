package com.antonina.socialsynchro.content.attachments;

public class AttachmentTypes {
    private static final IAttachmentType[] attachments = new IAttachmentType[AttachmentTypeID.values().length];
    private static boolean initialized = false;

    private AttachmentTypes() { }

    private static void init() {
        attachments[AttachmentTypeID.Image.ordinal()] = ImageAttachmentType.getInstance();
        attachments[AttachmentTypeID.Video.ordinal()] = VideoAttachmentType.getInstance();
        attachments[AttachmentTypeID.Audio.ordinal()] = AudioAttachmentType.getInstance();
        initialized = true;
    }

    public static IAttachmentType getAttachmentType(AttachmentTypeID attachmentTypeID) {
        if (!initialized)
            init();
        return attachments[attachmentTypeID.ordinal()];
    }

    public static IAttachmentType getAttachmentType(int attachmentTypeID) {
        if (!initialized)
            init();
        return attachments[attachmentTypeID];
    }

    public static IAttachmentType[] getAttachmentTypes() {
        if (!initialized)
            init();
        return attachments;
    }
}
