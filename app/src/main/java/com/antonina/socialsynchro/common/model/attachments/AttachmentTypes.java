package com.antonina.socialsynchro.common.model.attachments;

@SuppressWarnings("WeakerAccess")
public class AttachmentTypes {
    private static final AttachmentType[] attachmentTypes = new AttachmentType[AttachmentTypeID.values().length];
    private static boolean initialized = false;

    private AttachmentTypes() { }

    private static void init() {
        attachmentTypes[AttachmentTypeID.Image.ordinal()] = ImageAttachmentType.getInstance();
        attachmentTypes[AttachmentTypeID.Video.ordinal()] = VideoAttachmentType.getInstance();
        initialized = true;
    }

    public static AttachmentType getAttachmentType(AttachmentTypeID attachmentTypeID) {
        if (!initialized)
            init();
        return attachmentTypes[attachmentTypeID.ordinal()];
    }

    public static AttachmentType getAttachmentType(int attachmentTypeID) {
        if (!initialized)
            init();
        return attachmentTypes[attachmentTypeID];
    }

    public static AttachmentType[] getAttachmentTypes() {
        if (!initialized)
            init();
        return attachmentTypes;
    }
}
