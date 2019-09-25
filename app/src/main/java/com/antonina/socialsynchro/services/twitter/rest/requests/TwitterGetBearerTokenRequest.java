package com.antonina.socialsynchro.services.twitter.rest.requests;

import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterGetBearerTokenAuthorizationStrategy;

public class TwitterGetBearerTokenRequest extends TwitterRequest {
    private final String grantType;

    private TwitterGetBearerTokenRequest(String authorizationHeader) {
        super(authorizationHeader);
        this.grantType = percentEncode("client_credentials");
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getGrantType() {
        return grantType;
    }

    public static class Builder extends TwitterRequest.Builder {

        @Override
        public TwitterGetBearerTokenRequest build() {
            configureAuthorization();
            return new TwitterGetBearerTokenRequest(authorization.buildAuthorizationHeader());
        }

        @Override
        protected void configureAuthorization() {
            authorization = new TwitterGetBearerTokenAuthorizationStrategy();
        }
    }
}
