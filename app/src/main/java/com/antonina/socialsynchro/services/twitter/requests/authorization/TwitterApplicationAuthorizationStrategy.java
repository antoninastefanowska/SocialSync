package com.antonina.socialsynchro.services.twitter.requests.authorization;

public class TwitterApplicationAuthorizationStrategy extends TwitterAuthorizationStrategy {

    public TwitterApplicationAuthorizationStrategy() {
        super();
    }

    @Override
    public String buildAuthorizationHeader() {
        return "Bearer " + config.getTwitterConfig().getBearerToken();
    }
}
