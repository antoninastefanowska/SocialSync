package com.antonina.socialsynchro.common.content.posts;

import com.antonina.socialsynchro.common.content.statistics.Statistic;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.gui.GUIItem;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnUnpublishedListener;
import com.antonina.socialsynchro.common.gui.operations.Operation;
import com.antonina.socialsynchro.common.gui.operations.OperationID;

import java.util.List;
import java.util.SortedMap;

@SuppressWarnings("WeakerAccess")
public abstract class PostContainer extends GUIItem implements IPost, IDatabaseEntity {
    protected Long internalID;
    protected Post post;
    protected SortedMap<OperationID, Operation> editOperations;
    protected SortedMap<OperationID, Operation> displayOperations;

    public abstract Post getPost();

    public abstract void publish(OnPublishedListener listener, OnAttachmentUploadedListener attachmentListener);

    public abstract void unpublish(OnUnpublishedListener listener);

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

    public abstract Statistic getStatistic();

    public SortedMap<OperationID, Operation> getEditOperations() {
        return editOperations;
    }

    public SortedMap<OperationID, Operation> getDisplayOperations() {
        return displayOperations;
    }
}
