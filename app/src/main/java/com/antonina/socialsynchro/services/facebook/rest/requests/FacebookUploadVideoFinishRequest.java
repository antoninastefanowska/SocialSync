package com.antonina.socialsynchro.services.facebook.rest.requests;

import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookUserAuthorizationStrategy;

public class FacebookUploadVideoFinishRequest extends FacebookRequest {
    private final String pageID;
    private final String uploadSessionID;
    private final String title;
    private final String description;

    private FacebookUploadVideoFinishRequest(String authorizationString, String pageID, String uploadSessionID, String title, String description) {
        super(authorizationString);
        this.pageID = pageID;
        this.uploadSessionID = uploadSessionID;
        this.title = title;
        this.description = description;
    }

    public String getPageID() {
        return pageID;
    }

    public String getUploadSessionID() {
        return uploadSessionID;
    }

    public String getUploadPhase() {
        return "finish";
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends FacebookRequest.Builder {
        private String accessToken;
        private String pageID;
        private String uploadSessionID;
        private String title;
        private String description;

        @Override
        public FacebookUploadVideoFinishRequest build() {
            configureAuthorization();
            return new FacebookUploadVideoFinishRequest(authorization.buildAuthorizationString(), pageID, uploadSessionID, title, description);
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder pageID(String pageID) {
            this.pageID = pageID;
            return this;
        }

        public Builder uploadSessionID(String uploadSessionID) {
            this.uploadSessionID = uploadSessionID;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        private void configureAuthorization() {
            authorization = new FacebookUserAuthorizationStrategy(accessToken);
        }
    }
}
