package com.antonina.socialsynchro.services.facebook.rest.authorization;

public class FacebookApplicationAuthorizationStrategy extends FacebookAuthorizationStrategy {

    public FacebookApplicationAuthorizationStrategy() {
        super();
    }

    @Override
    public String buildAuthorizationString() {
        String appID = config.getKey("facebook_app_id");
        String appSecret = config.getKey("facebook_app_secret");
        return appID + "|" + appSecret;
    }
}
