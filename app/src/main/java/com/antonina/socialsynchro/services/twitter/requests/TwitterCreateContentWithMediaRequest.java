package com.antonina.socialsynchro.services.twitter.requests;

import java.util.List;

public class TwitterCreateContentWithMediaRequest extends TwitterCreateContentRequest {
    private final String mediaIDs;

    public TwitterCreateContentWithMediaRequest(String authorizationHeader, String status, String mediaIDs) {
        super(authorizationHeader, status);
        this.mediaIDs = mediaIDs;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getMediaIDs() {
        return mediaIDs;
    }

    public static class Builder extends TwitterCreateContentRequest.Builder {
        private String mediaIDs;

        @Override
        public TwitterCreateContentWithMediaRequest build() {
            return new TwitterCreateContentWithMediaRequest(buildUserAuthorizationHeader(), status, mediaIDs);
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

        @Override
        protected void collectParameters() {
            authorizationParameters.put("media_ids", mediaIDs);
            super.collectParameters();
            authorizationParameters.remove("media_ids");
        }
    }
}