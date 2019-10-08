package com.antonina.socialsynchro.services.twitter.rest.requests;

import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterUserAuthorizationStrategy;

public class TwitterCreateContentRequest extends TwitterRequest {
    private final String status;

    protected TwitterCreateContentRequest(String authorizationHeader, String status) {
        super(authorizationHeader);
        this.status = percentEncode(status);
    }

    public String getStatus() {
        return status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static String getRequestEndpoint() {
        return "/statuses/update";
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://api.twitter.com/1.1/statuses/update.json";
        protected String status;
        private String accessToken;
        private String secretToken;

        @Override
        public TwitterCreateContentRequest build() {
            configureAuthorization();
            return new TwitterCreateContentRequest(authorization.buildAuthorizationHeader(), status);
        }

        @Override
        protected void configureAuthorization() {
            authorization = new TwitterUserAuthorizationStrategy()
                    .accessToken(accessToken)
                    .secretToken(secretToken)
                    .requestMethod("POST")
                    .requestURL(REQUEST_URL)
                    .addSignatureParameter("status", status);
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder secretToken(String secretToken) {
            this.secretToken = secretToken;
            return this;
        }
    }
}
