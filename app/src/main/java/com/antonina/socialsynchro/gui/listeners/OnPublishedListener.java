package com.antonina.socialsynchro.gui.listeners;

import com.antonina.socialsynchro.content.ChildPostContainer;

public interface OnPublishedListener {
    void onPublished(ChildPostContainer publishedPost);
    void onError(ChildPostContainer post, String error);
}
