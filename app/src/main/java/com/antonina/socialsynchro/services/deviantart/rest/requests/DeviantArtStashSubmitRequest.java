package com.antonina.socialsynchro.services.deviantart.rest.requests;

import com.antonina.socialsynchro.services.deviantart.rest.authorization.DeviantArtAuthorizationStrategy;

import okhttp3.MultipartBody;

public class DeviantArtStashSubmitRequest extends DeviantArtRequest {
    private final String title;
    private final String description;
    private final MultipartBody.Part file;

    private DeviantArtStashSubmitRequest(String authorizationString, String title, String description, MultipartBody.Part file) {
        super(authorizationString);
        this.title = title;
        this.description = description;
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public MultipartBody.Part getFile() {
        return file;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DeviantArtRequest.Builder {
        private String accessToken;
        private String title;
        private String description;
        private MultipartBody.Part file;

        @Override
        public DeviantArtStashSubmitRequest build() {
            configureAuthorization();
            return new DeviantArtStashSubmitRequest(authorization.buildAuthorizationString(), title, description, file);
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
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

        public Builder file(MultipartBody.Part file) {
            this.file = file;
            return this;
        }

        private void configureAuthorization() {
            authorization = new DeviantArtAuthorizationStrategy(accessToken);
        }
    }
}
