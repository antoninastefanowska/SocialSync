package com.antonina.socialsynchro.accounts.controllers;

import com.antonina.socialsynchro.accounts.IAccount;
import com.antonina.socialsynchro.posts.IPost;

public interface IController {
    void startGet(String... args);
    void startPost(IPost post, IAccount account);
}
