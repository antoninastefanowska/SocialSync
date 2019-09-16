package com.antonina.socialsynchro.gui.listeners;

import com.antonina.socialsynchro.content.ChildPostContainer;

public interface OnPublishedListener {
    void onPublished(ChildPostContainer publishedPost, String error);
}
