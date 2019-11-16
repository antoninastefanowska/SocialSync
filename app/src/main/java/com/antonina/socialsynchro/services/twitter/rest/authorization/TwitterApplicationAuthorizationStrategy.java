package com.antonina.socialsynchro.services.twitter.rest.authorization;

public class TwitterApplicationAuthorizationStrategy extends TwitterAuthorizationStrategy {

    public TwitterApplicationAuthorizationStrategy() {
        super();
    }

    @Override
    public String buildAuthorizationString() {
        return "Bearer " + config.getTwitterConfig().getBearerToken();
    }
}
