package com.antonina.socialsynchro.services.facebook.rest.authorization;

public class FacebookUserAuthorizationStrategy extends FacebookAuthorizationStrategy {
    private String accessToken;

    public FacebookUserAuthorizationStrategy(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

    @Override
    public String buildAuthorizationString() {
        return accessToken;
    }
}
