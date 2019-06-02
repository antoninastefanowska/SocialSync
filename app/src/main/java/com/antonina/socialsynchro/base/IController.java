package com.antonina.socialsynchro.base;

import com.antonina.socialsynchro.content.ChildPostContainer;

public interface IController {
    void requestPost(ChildPostContainer post, IAccount account);
    void requestRemove(ChildPostContainer post, IAccount account);
    void requestGetLoginToken();
    void requestGetAccessToken(String loginToken, String secretLoginToken, String verifier);
}
