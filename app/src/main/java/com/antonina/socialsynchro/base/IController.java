package com.antonina.socialsynchro.base;

import com.antonina.socialsynchro.content.ChildPostContainer;

public interface IController {
    //TODO: Controller jako klasa abstrakcyjna, nie interfejs - singleton

    void requestPost(ChildPostContainer post, Account account);
    void requestRemove(ChildPostContainer post, Account account);
    void requestGetLoginToken();
    void requestGetAccessToken(String loginToken, String secretLoginToken, String verifier);
}
