package com.antonina.socialsynchro.services.twitter.rest.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TwitterGetRateLimitsResponse extends TwitterResponse {
    private final static int RESET_TIME_SECONDS = 900;

    @SerializedName("resources")
    private Resources resources;

    public List<RequestLimitResponse> getRequestLimits() {
        return resources.getRequestLimits();
    }

    public static class Resources {
        private List<RequestLimitResponse> requestLimits = new ArrayList<>();

        @SerializedName("users")
        private UsersResource usersResource;

        @SerializedName("statuses")
        private StatusesResource statusesResource;

        @SerializedName("application")
        private ApplicationResource applicationResource;

        @SerializedName("account")
        private AccountResource accountResource;

        @SerializedName("media")
        private MediaResource mediaResource;

        public void processResponse() {
            if (usersResource != null)
                requestLimits.addAll(usersResource.getRequestLimits());
            if (statusesResource != null)
                requestLimits.addAll(statusesResource.getRequestLimits());
            if (applicationResource != null)
                requestLimits.addAll(applicationResource.getRequestLimits());
            if (accountResource != null)
                requestLimits.addAll(accountResource.getRequestLimits());
            if (mediaResource != null)
                requestLimits.addAll(mediaResource.getRequestLimits());
        }

        public List<RequestLimitResponse> getRequestLimits() {
            processResponse();
            return requestLimits;
        }
    }

    public abstract static class Resource {
        protected List<RequestLimitResponse> requestLimits = new ArrayList<>();

        public abstract void processResponse();

        public List<RequestLimitResponse> getRequestLimits() {
            processResponse();
            return requestLimits;
        }
    }

    public static class UsersResource extends Resource {
        @SerializedName("/users/show/:id")
        private RequestLimitResponse showUserLimit;

        @Override
        public void processResponse() {
            if (showUserLimit != null) {
                showUserLimit.setEndpoint("/users/show/:id");
                requestLimits.add(showUserLimit);
            }
        }
    }

    public static class StatusesResource extends Resource {
        @SerializedName("/statuses/show/:id")
        private RequestLimitResponse showStatusLimit;

        @Override
        public void processResponse() {
            if (showStatusLimit != null) {
                showStatusLimit.setEndpoint("/statuses/show/:id");
                requestLimits.add(showStatusLimit);
            }
        }
    }

    public static class ApplicationResource extends Resource {
        @SerializedName("/application/rate_limit_status")
        private RequestLimitResponse rateLimitStatusLimit;

        @Override
        public void processResponse() {
            if (rateLimitStatusLimit != null) {
                rateLimitStatusLimit.setEndpoint("/application/rate_limit_status");
                requestLimits.add(rateLimitStatusLimit);
            }
        }
    }

    public static class AccountResource extends Resource {
        @SerializedName("/account/verify_credentials")
        private RequestLimitResponse verifyCredentialsLimit;

        @Override
        public void processResponse() {
            if (verifyCredentialsLimit != null) {
                verifyCredentialsLimit.setEndpoint("/account/verify_credentials");
                requestLimits.add(verifyCredentialsLimit);
            }
        }
    }

    public static class MediaResource extends Resource {
        @SerializedName("/media/upload")
        private RequestLimitResponse mediaUploadLimit;

        @Override
        public void processResponse() {
            if (mediaUploadLimit != null) {
                mediaUploadLimit.setEndpoint("/media/upload");
                requestLimits.add(mediaUploadLimit);
            }
        }
    }

    public static class RequestLimitResponse {
        private String endpoint;

        @SerializedName("limit")
        private int limit;

        @SerializedName("remaining")
        private int remaining;

        @SerializedName("reset")
        private long reset;

        public int getLimit() {
            return limit;
        }

        public int getRemaining() {
            return remaining;
        }

        public long getReset() {
            return reset;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public int getResetTimeSeconds() {
            return RESET_TIME_SECONDS;
        }
    }
}
