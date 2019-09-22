package com.antonina.socialsynchro.gui.listeners;

import com.antonina.socialsynchro.content.ChildPostContainer;

public interface OnUnpublishedListener {
    void onUnpublished(ChildPostContainer unpublishedPost);
    void onError(ChildPostContainer post, String error);
}
