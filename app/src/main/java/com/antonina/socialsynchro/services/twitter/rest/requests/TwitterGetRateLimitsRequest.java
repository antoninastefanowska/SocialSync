package com.antonina.socialsynchro.services.twitter.rest.requests;

import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterAuthorizationStrategy;
import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterUserAuthorizationStrategy;

import java.util.ArrayList;
import java.util.List;

public class TwitterGetRateLimitsRequest extends TwitterRequest {
    private final String resources;

    private TwitterGetRateLimitsRequest(String authorizationHeader, String resources) {
        super(authorizationHeader);
        this.resources = percentEncode(resources);
    }

    public String getResources() {
        return resources;
    }

    public static String getRequestEndpoint() {
        return "/application/rate_limit_status";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends TwitterRequest.Builder {
        private final static String REQUEST_URL = "https://api.twitter.com/1.1/application/rate_limit_status.json";
        private List<String> resources = new ArrayList<>();
        private String resourcesString;

        @Override
        public TwitterGetRateLimitsRequest build() {
            configureAuthorization();
            return new TwitterGetRateLimitsRequest(authorization.buildAuthorizationString(), resourcesString);
        }

        @Override
        protected void configureAuthorization() {
            resourcesString = buildResourcesString();
            if (authorization.isUserAuthorization()) {
                ((TwitterUserAuthorizationStrategy)authorization)
                        .requestMethod("GET")
                        .requestURL(REQUEST_URL)
                        .addSignatureParameter("resources", resourcesString);
            }
        }

        private String buildResourcesString() {
            if (resources.size() > 0) {
                StringBuilder sb = new StringBuilder(resources.get(0));
                for (int i = 1; i < resources.size(); i++) {
                    sb.append(",");
                    sb.append(resources.get(i));
                }
                return sb.toString();
            } else {
                return "";
            }
        }

        public Builder addResource(String resource) {
            resources.add(resource);
            return this;
        }

        public Builder authorizationStrategy(TwitterAuthorizationStrategy authorizationStrategy) {
            authorization = authorizationStrategy;
            return this;
        }
    }
}
