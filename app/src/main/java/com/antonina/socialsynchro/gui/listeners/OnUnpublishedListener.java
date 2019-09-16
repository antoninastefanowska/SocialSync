package com.antonina.socialsynchro.gui.listeners;

import com.antonina.socialsynchro.content.ChildPostContainer;

public interface OnUnpublishedListener {
    void onUnpublished(ChildPostContainer unpublishedPost, String error);
}
