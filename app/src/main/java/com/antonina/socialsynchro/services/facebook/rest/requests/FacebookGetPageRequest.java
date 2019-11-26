package com.antonina.socialsynchro.services.facebook.rest.requests;

import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookAuthorizationStrategy;

public class FacebookGetPageRequest extends FacebookRequest {
    private final String pageID;
    private final String fields;

    private FacebookGetPageRequest(String authorizationString, String pageID, String fields) {
        super(authorizationString);
        this.pageID = pageID;
        this.fields = fields;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPageID() {
        return pageID;
    }

    public String getFields() {
        return fields;
    }

    public static class Builder extends FacebookRequest.Builder {
        private String pageID;

        private StringBuilder fields;
        private String separator;

        public Builder() {
            fields = new StringBuilder();
            separator = "";
        }

        @Override
        public FacebookGetPageRequest build() {
            return new FacebookGetPageRequest(authorization.buildAuthorizationString(), pageID, fields.toString());
        }

        public Builder pageID(String pageID) {
            this.pageID = pageID;
            return this;
        }

        public Builder addField(String field) {
            fields.append(separator);
            fields.append(field);
            if (separator.equals(""))
                separator = ",";
            return this;
        }

        public Builder authorizationStrategy(FacebookAuthorizationStrategy authorizationStrategy) {
            authorization = authorizationStrategy;
            return this;
        }
    }
}
