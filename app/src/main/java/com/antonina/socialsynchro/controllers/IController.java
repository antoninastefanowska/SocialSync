package com.antonina.socialsynchro.controllers;

import com.antonina.socialsynchro.accounts.IAccount;
import com.antonina.socialsynchro.posts.ChildPostContainer;
import com.antonina.socialsynchro.posts.IPost;
import com.antonina.socialsynchro.requests.OAuthToken;

public interface IController {
    void requestToken(String credentials);
    void requestGet(String... args);
    void requestPost(ChildPostContainer post);
    void requestRemove(ChildPostContainer post);
    OAuthToken getToken();
}
