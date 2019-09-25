package com.antonina.socialsynchro.services.facebook;

import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnUnpublishedListener;
import com.antonina.socialsynchro.common.rest.IResponse;

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
    public void setTitle(String title) { }

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

    @Override
    public void synchronize(OnSynchronizedListener listener) {

    }
}
