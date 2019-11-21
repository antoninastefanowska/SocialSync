package com.antonina.socialsynchro.common.content.posts;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.gui.GUIItem;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnUnpublishedListener;

@SuppressWarnings("WeakerAccess")
public abstract class PostContainer extends GUIItem implements IPost, IDatabaseEntity {
    protected Long internalID;
    protected Post post;

    public abstract Post getPost();

    public abstract void publish(OnPublishedListener listener, OnAttachmentUploadedListener attachmentListener);

    public abstract void unpublish(OnUnpublishedListener listener);

    public abstract boolean isPublished();

    public abstract void synchronize(OnSynchronizedListener listener);

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

    public abstract boolean isParent();
}
