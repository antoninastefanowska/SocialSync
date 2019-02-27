package com.antonina.socialsync.accounts.controllers;

import com.antonina.socialsync.accounts.IAccount;
import com.antonina.socialsync.posts.IPost;

public interface IController {
    void startGet(String... args);
    void startPost(IPost post, IAccount account);
}
