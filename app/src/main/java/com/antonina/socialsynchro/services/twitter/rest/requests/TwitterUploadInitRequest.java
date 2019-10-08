package com.antonina.socialsynchro.services.twitter.rest.requests;

import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterUserAuthorizationStrategy;

public class TwitterUploadInitRequest extends TwitterRequest {
    private final String command;
    private final String totalBytes;
    private final String mediaType;

    private TwitterUploadInitRequest(String authorizationHeader, String totalBytes, String mediaType) {
        super(authorizationHeader);
        this.command = percentEncode("INIT");
        this.totalBytes = percentEncode(totalBytes);
        this.mediaType = percentEncode(mediaType);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getTotalBytes() {
        return totalBytes;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getCommand() {
        return command;
    }

    public static String getRequestEndpoint() {
        return "/media/upload";
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://upload.twitter.com/1.1/media/upload.json";
        private String accessToken;
        private String secretToken;
        private String totalBytes;
        private String mediaType;

        @Override
        public TwitterUploadInitRequest build() {
            configureAuthorization();
            return new TwitterUploadInitRequest(authorization.buildAuthorizationHeader(), totalBytes, mediaType);
        }

        @Override
        protected void configureAuthorization() {
            authorization = new TwitterUserAuthorizationStrategy()
                    .accessToken(accessToken)
                    .secretToken(secretToken)
                    .requestMethod("POST")
                    .requestURL(REQUEST_URL)
                    .addSignatureParameter("command", "INIT")
                    .addSignatureParameter("total_bytes", totalBytes)
                    .addSignatureParameter("media_type", mediaType);
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder secretToken(String secretToken) {
            this.secretToken = secretToken;
            return this;
        }

        public Builder totalBytes(long totalBytes) {
            this.totalBytes = String.valueOf(totalBytes);
            return this;
        }

        public Builder mediaType(String mediaType) {
            this.mediaType = mediaType;
            return this;
        }
    }
}
