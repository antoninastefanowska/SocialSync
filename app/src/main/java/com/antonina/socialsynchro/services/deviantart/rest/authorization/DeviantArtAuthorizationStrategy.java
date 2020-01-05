package com.antonina.socialsynchro.services.deviantart.rest.authorization;

import com.antonina.socialsynchro.common.rest.BaseAuthorizationStrategy;

public class DeviantArtAuthorizationStrategy extends BaseAuthorizationStrategy {
    private String accessToken;

    public DeviantArtAuthorizationStrategy(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

    @Override
    public String buildAuthorizationString() {
        return accessToken;
    }
}
