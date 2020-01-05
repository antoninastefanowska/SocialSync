package com.antonina.socialsynchro.services.facebook.rest.requests;

import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookUserAuthorizationStrategy;

public class FacebookUploadVideoStartRequest extends FacebookRequest {
    private final String pageID;
    private final long fileSize;


    private FacebookUploadVideoStartRequest(String authorizationString, String pageID, long fileSize) {
        super(authorizationString);
        this.pageID = pageID;
        this.fileSize = fileSize;
    }

    public String getPageID() {
        return pageID;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getUploadPhase() {
        return "start";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends FacebookRequest.Builder {
        private String accessToken;
        private String pageID;
        private long fileSize;

        @Override
        public FacebookUploadVideoStartRequest build() {
            configureAuthorization();
            return new FacebookUploadVideoStartRequest(authorization.buildAuthorizationString(), pageID, fileSize);
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder pageID(String pageID) {
            this.pageID = pageID;
            return this;
        }

        public Builder fileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        private void configureAuthorization() {
            authorization = new FacebookUserAuthorizationStrategy(accessToken);
        }
    }
}
