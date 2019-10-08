package com.antonina.socialsynchro.services.twitter.rest.requests;

import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterUserAuthorizationStrategy;

import java.util.List;

public class TwitterCreateContentWithMediaRequest extends TwitterCreateContentRequest {
    private final String mediaIDs;

    public TwitterCreateContentWithMediaRequest(String authorizationHeader, String status, String mediaIDs) {
        super(authorizationHeader, status);
        this.mediaIDs = percentEncode(mediaIDs);
    }

    public String getMediaIDs() {
        return mediaIDs;
    }

    public static String getRequestEndpoint() {
        return "/statuses/update";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterCreateContentRequest.Builder {
        private String mediaIDs;

        @Override
        public TwitterCreateContentWithMediaRequest build() {
            configureAuthorization();
            return new TwitterCreateContentWithMediaRequest(authorization.buildAuthorizationHeader(), status, mediaIDs);
        }

        @Override
        protected void configureAuthorization() {
            super.configureAuthorization();
            ((TwitterUserAuthorizationStrategy)authorization).addSignatureParameter("media_ids", mediaIDs);
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

        public Builder status(String status) {
            return (Builder)super.status(status);
        }

        public Builder accessToken(String accessToken) {
            return (Builder)super.accessToken(accessToken);
        }

        public Builder secretToken(String secretToken) {
            return (Builder)super.secretToken(secretToken);
        }
    }
}