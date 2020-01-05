package com.antonina.socialsynchro.services.facebook.rest.requests;

import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookUserAuthorizationStrategy;

import okhttp3.MultipartBody;

public class FacebookUploadVideoTransferRequest extends FacebookRequest {
    private final String pageID;
    private final String uploadSessionID;
    private final long startOffset;
    private final MultipartBody.Part fileChunk;

    private FacebookUploadVideoTransferRequest(String authorizationString, String pageID, String uploadSessionID, long startOffset, MultipartBody.Part fileChunk) {
        super(authorizationString);
        this.pageID = pageID;
        this.uploadSessionID = uploadSessionID;
        this.startOffset = startOffset;
        this.fileChunk = fileChunk;
    }

    public String getPageID() {
        return pageID;
    }

    public String getUploadSessionID() {
        return uploadSessionID;
    }

    public long getStartOffset() {
        return startOffset;
    }

    public MultipartBody.Part getFileChunk() {
        return fileChunk;
    }

    public String getUploadPhase() {
        return "transfer";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends FacebookRequest.Builder {
        private String accessToken;
        private String pageID;
        private String uploadSessionID;
        private long startOffset;
        private MultipartBody.Part fileChunk;

        @Override
        public FacebookUploadVideoTransferRequest build() {
            configureAuthorization();
            return new FacebookUploadVideoTransferRequest(authorization.buildAuthorizationString(), pageID, uploadSessionID, startOffset, fileChunk);
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

        public Builder startOffset(long startOffset) {
            this.startOffset = startOffset;
            return this;
        }

        public Builder fileChunk(MultipartBody.Part fileChunk) {
            this.fileChunk = fileChunk;
            return this;
        }

        private void configureAuthorization() {
            authorization = new FacebookUserAuthorizationStrategy(accessToken);
        }
    }
}
