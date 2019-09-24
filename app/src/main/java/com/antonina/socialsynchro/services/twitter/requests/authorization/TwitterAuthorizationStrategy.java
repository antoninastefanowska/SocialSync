package com.antonina.socialsynchro.services.twitter.requests.authorization;

import com.antonina.socialsynchro.services.ApplicationConfig;

public abstract class TwitterAuthorizationStrategy {
    protected ApplicationConfig config;

    public TwitterAuthorizationStrategy() {
        config = ApplicationConfig.getInstance();
    }

    public abstract String buildAuthorizationHeader();

    public boolean isUserAuthorization() {
        return false;
    }
}
