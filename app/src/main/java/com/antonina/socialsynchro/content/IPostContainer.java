package com.antonina.socialsynchro.content;

public interface IPostContainer {
    Post getPost();
    void publish(OnPublishedListener listener);
    void unpublish(OnUnpublishedListener listener);
    boolean isPublished();
}
