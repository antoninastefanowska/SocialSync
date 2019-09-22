package com.antonina.socialsynchro.services.twitter.requests;

import java.util.List;

public class TwitterCreateContentRequest extends TwitterRequest {
    private String status;
    private String mediaIDs;

    private TwitterCreateContentRequest(String authorizationHeader, String status, String mediaIDs) {
        super(authorizationHeader);
        this.status = status;
        this.mediaIDs = mediaIDs;
    }

    public String getStatus() {
        return percentEncode(status);
    }

    public String getMediaIDs() {
        if (mediaIDs != null)
            return percentEncode(mediaIDs);
        else
            return null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private String status;
        private String accessToken;
        private String secretToken;
        private String mediaIDs;

        @Override
        public TwitterCreateContentRequest build() {
            return new TwitterCreateContentRequest(buildUserAuthorizationHeader(), status, mediaIDs);
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

        public Builder mediaIDs(List<String> mediaIDs) {
            if (mediaIDs == null || mediaIDs.size() == 0)
                return this;
            StringBuilder sb = new StringBuilder(mediaIDs.get(0));
            for (int i = 1; i < mediaIDs.size(); i++) {
                sb.append(",");
                sb.append(mediaIDs.get(i));
            }
            this.mediaIDs = sb.toString();
            return this;
        }

        @Override
        protected String getURL() {
            return "https://api.twitter.com/1.1/statuses/update.json";
        }

        @Override
        protected String getAccessToken() {
            return accessToken;
        }

        @Override
        protected String getSecretToken() {
            return secretToken;
        }

        @Override
        protected String getHTTPMethod() {
            return "POST";
        }

        @Override
        protected void collectParameters() {
            if (mediaIDs != null)
                authorizationParameters.put("media_ids", mediaIDs);
            authorizationParameters.put("status", status);
            authorizationParameters.put("oauth_token", getAccessToken());
            super.collectParameters();
            authorizationParameters.remove("status");
            if (mediaIDs != null)
                authorizationParameters.remove("media_ids");
        }
    }
}
