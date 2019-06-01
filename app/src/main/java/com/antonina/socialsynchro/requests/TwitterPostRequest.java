package com.antonina.socialsynchro.requests;

import com.antonina.socialsynchro.posts.ChildPostContainer;
import com.antonina.socialsynchro.posts.TwitterPostContainer;

public class TwitterPostRequest extends TwitterRequest {
    private final String status;

    public TwitterPostRequest(ChildPostContainer post) {
        super();
        status = post.getContent();
        buildUserAuthorizationHeader();
    }

    public String getStatus() {
        return percentEncode(status);
    }

    @Override
    public void collectParameters() {
        authorizationParameters.put("status", status);
        collectBaseParameters();
        authorizationParameters.remove("status");
    }

    @Override
    protected String getUrl() {
        return "https://api.twitter.com/1.1/statuses/update.json";
    }
}
