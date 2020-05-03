package com.antonina.socialsynchro.common.gui.listeners;

import com.antonina.socialsynchro.common.model.posts.ChildPostContainer;

public interface OnUnpublishedListener {
    void onUnpublished(ChildPostContainer unpublishedPost);
    void onError(ChildPostContainer post, String error);
}
