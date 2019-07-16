package com.antonina.socialsynchro.services.twitter.requests;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.content.ChildPostContainer;

public class TwitterRemoveContentRequest extends TwitterRequest {
    private final String id;

    public TwitterRemoveContentRequest(ChildPostContainer post, Account account) {
        super();
        id = post.getServiceExternalIdentifier();
        accessToken = account.getAccessToken();
        secretToken = account.getSecretToken();
        buildUserAuthorizationHeader();
    }

    public String getID() { return id; }

    @Override
    public void collectParameters() {
        authorizationParameters.put("oauth_token", accessToken);
        collectBaseParameters();
    }

    @Override
    protected String getUrl() {
        return "https://api.twitter.com/1.1/statuses/destroy/" + id + ".json";
    }
}
