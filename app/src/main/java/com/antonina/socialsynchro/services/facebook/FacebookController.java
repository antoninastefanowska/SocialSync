package com.antonina.socialsynchro.services.facebook;

import com.antonina.socialsynchro.base.IAccount;
import com.antonina.socialsynchro.base.IController;
import com.antonina.socialsynchro.content.ChildPostContainer;

public class FacebookController implements IController {
    private static final String BASE_URL = "https://graph.facebook.com/v2.8/";

    @Override
    public void requestPost(ChildPostContainer post, IAccount account) {

    }

    @Override
    public void requestRemove(ChildPostContainer post, IAccount account) {

    }

    @Override
    public void requestGetLoginToken() {

    }

    @Override
    public void requestGetAccessToken(String loginToken, String secretLoginToken, String verifier) {

    }
}
