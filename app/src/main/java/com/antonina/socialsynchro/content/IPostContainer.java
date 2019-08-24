package com.antonina.socialsynchro.content;

public interface IPostContainer {
    Post getPost();
    void publish(OnPublishedListener listener);
    boolean isPublished();
    void remove(); // TODO: Niepotrzebne chyba.
}
