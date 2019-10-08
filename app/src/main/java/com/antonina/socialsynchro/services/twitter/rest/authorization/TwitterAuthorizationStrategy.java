package com.antonina.socialsynchro.services.twitter.rest.authorization;

import com.antonina.socialsynchro.common.rest.BaseAuthorizationStrategy;

public abstract class TwitterAuthorizationStrategy extends BaseAuthorizationStrategy {

    public TwitterAuthorizationStrategy() {
        super();
    }

    public boolean isUserAuthorization() {
        return false;
    }
}
