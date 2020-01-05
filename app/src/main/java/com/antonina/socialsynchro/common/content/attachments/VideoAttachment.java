package com.antonina.socialsynchro.common.content.attachments;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

import java.io.File;

@SuppressWarnings("WeakerAccess")
public class VideoAttachment extends Attachment {
    public VideoAttachment(IDatabaseRow data) {
        super(data);
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Video));
    }

    public VideoAttachment(File file) {
        super(file);
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Video));
    }

    public VideoAttachment() {
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Video));
    }

    public Bitmap getThumbnail() {
        return ThumbnailUtils.createVideoThumbnail(getFile().getPath(), MediaStore.Video.Thumbnails.MICRO_KIND);
    }
}
