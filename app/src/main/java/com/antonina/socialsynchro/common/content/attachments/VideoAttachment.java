package com.antonina.socialsynchro.common.content.attachments;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

import java.io.File;

@SuppressWarnings("WeakerAccess")
public class VideoAttachment extends Attachment {
    private long durationMilliseconds;
    private int width, height;

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

    @Override
    protected void extractMetadata() {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(getFile().getAbsolutePath());

        String heightString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String widthString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        height = Integer.parseInt(heightString);
        width = Integer.parseInt(widthString);

        String durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        durationMilliseconds = Long.parseLong(durationString);
    }

    public long getDurationMilliseconds() {
        return durationMilliseconds;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getAspectRatio() {
        return (double)width / height;
    }
}
