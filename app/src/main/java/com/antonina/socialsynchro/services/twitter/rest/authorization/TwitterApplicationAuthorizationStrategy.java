package com.antonina.socialsynchro.services.twitter.rest.authorization;

public class TwitterApplicationAuthorizationStrategy extends TwitterAuthorizationStrategy {

    public TwitterApplicationAuthorizationStrategy() {
        super();
    }

    @Override
    public String buildAuthorizationHeader() {
        return "Bearer " + config.getTwitterConfig().getBearerToken();
    }
}
