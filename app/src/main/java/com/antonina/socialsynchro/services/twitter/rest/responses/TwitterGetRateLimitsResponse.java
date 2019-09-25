package com.antonina.socialsynchro.services.twitter.rest.responses;

import com.google.gson.annotations.SerializedName;

public class TwitterGetRateLimitsResponse extends TwitterResponse {
    @SerializedName("resources")
    private Resources resources;

    public Resources getResources() {
        return resources;
    }

    public static class Resources {
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

        public UsersResource getUsersResource() {
            return usersResource;
        }

        public StatusesResource getStatusesResource() {
            return statusesResource;
        }

        public ApplicationResource getApplicationResource() {
            return applicationResource;
        }

        public AccountResource getAccountResource() {
            return accountResource;
        }

        public MediaResource getMediaResource() {
            return mediaResource;
        }
    }

    public static class UsersResource {
        @SerializedName("/users/show/:id")
        private RequestLimit showUserLimit;

        public RequestLimit getShowUserLimit() {
            return showUserLimit;
        }
    }

    public static class StatusesResource {
        @SerializedName("/statuses/show/:id")
        private RequestLimit showStatusLimit;

        public RequestLimit getShowStatusLimit() {
            return showStatusLimit;
        }
    }

    public static class ApplicationResource {
        @SerializedName("/application/rate_limit_status")
        private RequestLimit rateLimitStatusLimit;

        public RequestLimit getRateLimitStatusLimit() {
            return rateLimitStatusLimit;
        }
    }

    public static class AccountResource {
        @SerializedName("/account/verify_credentials")
        private RequestLimit verifyCredentialsLimit;
    }

    public static class MediaResource {
        @SerializedName("/media/upload")
        private RequestLimit mediaUploadLimit;

        public RequestLimit getMediaUploadLimit() {
            return mediaUploadLimit;
        }
    }

    public static class RequestLimit {
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
    }
}
