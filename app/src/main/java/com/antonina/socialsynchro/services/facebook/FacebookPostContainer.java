package com.antonina.socialsynchro.services.facebook;

import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.gui.listeners.OnUnpublishedListener;
import com.antonina.socialsynchro.services.IResponse;

public class FacebookPostContainer extends ChildPostContainer {
    private static final int MAX_CONTENT_LENGTH = 63206;

    public FacebookPostContainer(FacebookAccount account) {
        super(account);
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public void setTitle(String title) {
        return;
    }

    @Override
    public void setContent(String content) {
        if (content.length() > MAX_CONTENT_LENGTH) {
            unlock();
            content = content.substring(0, content.length() - 3) + "...";
        }
        super.setContent(content);
    }

    @Override
    public void publish(OnPublishedListener listener, OnAttachmentUploadedListener attachmentListener) {

    }

    @Override
    public void unpublish(OnUnpublishedListener listener) {

    }

    @Override
    public void createFromResponse(IResponse response) {

    }
}
