package com.antonina.socialsynchro.services.facebook.rest.requests;

import java.util.HashMap;
import java.util.List;

public class FacebookCreateContentWithMediaRequest extends FacebookCreateContentRequest {
    private final HashMap<String, String> mediaIDs;

    private FacebookCreateContentWithMediaRequest(String authorizationHeader, String pageID, String message, HashMap<String, String> mediaIDs) {
        super(authorizationHeader, pageID, message);
        this.mediaIDs = mediaIDs;
    }

    public static Builder builder() {
        return new Builder();
    }

    public HashMap<String, String> getMediaIDs() {
        return mediaIDs;
    }

    public static class Builder extends FacebookCreateContentRequest.Builder {
        private HashMap<String, String> mediaIDs;

        @Override
        public FacebookCreateContentWithMediaRequest build() {
            configureAuthorization();
            return new FacebookCreateContentWithMediaRequest(authorization.buildAuthorizationString(), pageID, message, mediaIDs);
        }

        @Override
        public Builder pageID(String pageID) {
            return (Builder)super.pageID(pageID);
        }

        @Override
        public Builder message(String message) {
            return (Builder)super.message(message);
        }

        @Override
        public Builder accessToken(String accessToken) {
            return (Builder)super.accessToken(accessToken);
        }

        public Builder mediaIDs(List<String> mediaIDs) {
            this.mediaIDs = new HashMap<>();
            for (int i = 0; i < mediaIDs.size(); i++) {
                String key = "attached_media[" + i + "]";
                String value = "{\"media_fbid\":\"" + mediaIDs.get(i) + "\"}";
                this.mediaIDs.put(key, value);
            }
            return this;
        }
    }
}
