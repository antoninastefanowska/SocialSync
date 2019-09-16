package com.antonina.socialsynchro.content;

import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.gui.GUIItem;
import com.antonina.socialsynchro.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.gui.listeners.OnUnpublishedListener;

public abstract class PostContainer extends GUIItem implements IPost, IDatabaseEntity {
    protected long internalID;
    protected Post post;

    public abstract Post getPost();

    public abstract void publish(OnPublishedListener listener);

    public abstract void unpublish(OnUnpublishedListener listener);

    public abstract boolean isPublished();

    @Override
    public long getInternalID() {
        return internalID;
    }
}
