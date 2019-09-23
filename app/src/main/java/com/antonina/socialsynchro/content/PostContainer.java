package com.antonina.socialsynchro.content;

import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.gui.GUIItem;
import com.antonina.socialsynchro.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.gui.listeners.OnUnpublishedListener;

@SuppressWarnings("WeakerAccess")
public abstract class PostContainer extends GUIItem implements IPost, IDatabaseEntity {
    protected Long internalID;
    protected Post post;

    public abstract Post getPost();

    public abstract void publish(OnPublishedListener listener, OnAttachmentUploadedListener attachmentListener);

    public abstract void unpublish(OnUnpublishedListener listener);

    public abstract boolean isPublished();

    @Override
    public Long getInternalID() {
        return internalID;
    }

    protected void setInternalID(Long internalID) {
        this.internalID = internalID;
    }

    protected void setPost(Post post) {
        this.post = post;
    }
}
