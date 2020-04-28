package com.antonina.socialsynchro.common.content.attachments;

import android.graphics.BitmapFactory;

import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;

import java.io.File;

@SuppressWarnings("WeakerAccess")
public class ImageAttachment extends Attachment {
    private int width, height, frameCount;

    public ImageAttachment(IDatabaseRow data) {
        super(data);
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Image));
    }

    public ImageAttachment(File file) {
        super(file);
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Image));
    }

    public ImageAttachment() {
        setAttachmentType(AttachmentTypes.getAttachmentType(AttachmentTypeID.Image));
    }

    @Override
    protected void extractMetadata() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(getFile().getAbsolutePath(), options);
        height = options.outHeight;
        width = options.outWidth;

        if (isGIF()) {
            GifHeaderParser headerParser = new GifHeaderParser();
            headerParser.setData(getBytes());
            GifHeader header = headerParser.parseHeader();
            frameCount = header.getNumFrames();
        } else
            frameCount = 1;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPixelCount() {
        return width * height * frameCount;
    }

    public boolean isGIF() {
        return getFileExtension().equals(".gif");
    }

    public int getFrameCount() {
        return frameCount;
    }
}
