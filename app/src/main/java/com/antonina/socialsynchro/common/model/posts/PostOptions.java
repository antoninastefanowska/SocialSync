package com.antonina.socialsynchro.common.model.posts;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.gui.GUIItem;

public abstract class PostOptions extends GUIItem implements IDatabaseEntity {
    private Long internalID;
    private ChildPostContainer parentPost;

    @Override
    public Long getInternalID() {
        return internalID;
    }

    protected void setInternalID(Long internalID) {
        this.internalID = internalID;
    }

    public ChildPostContainer getParentPost() {
        return parentPost;
    }

    public void setParentPost(ChildPostContainer parentPost) {
        this.parentPost = parentPost;
    }
}
