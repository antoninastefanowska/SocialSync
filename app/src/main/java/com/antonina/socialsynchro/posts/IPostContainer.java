package com.antonina.socialsynchro.posts;

public interface IPostContainer {
    IPost getPost();
    void publish();
    void remove();
}
