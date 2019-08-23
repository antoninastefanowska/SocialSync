package com.antonina.socialsynchro.content.attachments;

public class AudioAttachmentType implements IAttachmentType {
    private static AudioAttachmentType instance;

    public static AudioAttachmentType getInstance() { return instance; }

    private AudioAttachmentType() { }

    @Override
    public AttachmentTypeID getID() {
        return AttachmentTypeID.Audio;
    }

    @Override
    public String getName() {
        return "Audio";
    }

    @Override
    public String getIconURL() {
        return null;
    }
}
