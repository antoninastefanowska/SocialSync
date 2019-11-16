package com.antonina.socialsynchro.services.facebook.rest.requests;

import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookUserAuthorizationStrategy;

import okhttp3.MultipartBody;

public class FacebookUploadPhotoRequest extends FacebookRequest {
    private final String pageID;
    private final MultipartBody.Part photo;

    private FacebookUploadPhotoRequest(String authorizationHeader, String pageID, MultipartBody.Part photo) {
        super(authorizationHeader);
        this.pageID = pageID;
        this.photo = photo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPageID() {
        return pageID;
    }

    public MultipartBody.Part getPhoto() {
        return photo;
    }

    public boolean getPublished() {
        return false;
    }

    public static class Builder extends FacebookRequest.Builder {
        private String pageID;
        private MultipartBody.Part photo;
        private String accessToken;

        @Override
        public FacebookUploadPhotoRequest build() {
            configureAuthorization();
            return new FacebookUploadPhotoRequest(authorization.buildAuthorizationString(), pageID, photo);
        }

        public Builder pageID(String pageID) {
            this.pageID = pageID;
            return this;
        }

        public Builder photo(MultipartBody.Part photo) {
            this.photo = photo;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        private void configureAuthorization() {
            authorization = new FacebookUserAuthorizationStrategy(accessToken);
        }
    }
}