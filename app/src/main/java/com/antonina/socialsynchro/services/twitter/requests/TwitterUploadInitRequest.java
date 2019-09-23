package com.antonina.socialsynchro.services.twitter.requests;

public class TwitterUploadInitRequest extends TwitterRequest {
    private final String totalBytes;
    private final String mediaType;

    private TwitterUploadInitRequest(String authorizationHeader, String totalBytes, String mediaType) {
        super(authorizationHeader);
        this.totalBytes = totalBytes;
        this.mediaType = mediaType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getTotalBytes() {
        return percentEncode(totalBytes);
    }

    public String getMediaType() {
        return percentEncode(mediaType);
    }

    public String getCommand() {
        return percentEncode("INIT");
    }

    public static class Builder extends TwitterRequest.Builder {
        private String accessToken;
        private String secretToken;
        private String totalBytes;
        private String mediaType;

        @Override
        public TwitterUploadInitRequest build() {
            return new TwitterUploadInitRequest(buildUserAuthorizationHeader(), totalBytes, mediaType);
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

        @Override
        protected String getURL() {
            return "https://upload.twitter.com/1.1/media/upload.json";
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
            authorizationParameters.put("command", "INIT");
            authorizationParameters.put("total_bytes", totalBytes);
            authorizationParameters.put("media_type", mediaType);
            authorizationParameters.put("oauth_token", getAccessToken());
            super.collectParameters();
            authorizationParameters.remove("command");
            authorizationParameters.remove("total_bytes");
            authorizationParameters.remove("media_type");
        }
    }
}
