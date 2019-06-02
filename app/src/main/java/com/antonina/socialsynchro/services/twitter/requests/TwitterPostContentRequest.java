package com.antonina.socialsynchro.services.twitter.requests;

import com.antonina.socialsynchro.base.IAccount;
import com.antonina.socialsynchro.content.ChildPostContainer;

public class TwitterPostContentRequest extends TwitterRequest {
    private final String status;

    public TwitterPostContentRequest(ChildPostContainer post, IAccount account) {
        super();
        status = post.getContent();
        accessToken = account.getAccessToken();
        secretToken = account.getSecretToken();
        buildUserAuthorizationHeader();
    }

    public String getStatus() {
        return percentEncode(status);
    }

    @Override
    public void collectParameters() {
        authorizationParameters.put("status", status);
        authorizationParameters.put("oauth_token", accessToken);
        collectBaseParameters();
        authorizationParameters.remove("status");
    }

    @Override
    protected String getUrl() {
        return "https://api.twitter.com/1.1/statuses/update.json";
    }
}
