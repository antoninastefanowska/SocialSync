package com.antonina.socialsynchro.common.gui.listeners;

import com.antonina.socialsynchro.common.model.attachments.Attachment;

public interface OnAttachmentUploadedListener {
    void onInitialized(Attachment attachment);
    void onProgress(Attachment attachment);
    void onFinished(Attachment attachment);
    void onError(Attachment attachment, String error);
}
