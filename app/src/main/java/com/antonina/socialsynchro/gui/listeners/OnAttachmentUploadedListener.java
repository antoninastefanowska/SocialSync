package com.antonina.socialsynchro.gui.listeners;

import com.antonina.socialsynchro.content.attachments.Attachment;

public interface OnAttachmentUploadedListener {
    void onInitialized(Attachment attachment);
    void onProgress(Attachment attachment);
    void onFinished(Attachment attachment);
    void onError(Attachment attachment, String error);
}
