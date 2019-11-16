package com.antonina.socialsynchro.common.rest;

import com.antonina.socialsynchro.common.utils.ApplicationConfig;

public abstract class BaseAuthorizationStrategy {
    protected ApplicationConfig config;

    public BaseAuthorizationStrategy() {
        config = ApplicationConfig.getInstance();
    }

    public abstract String buildAuthorizationString();
}
