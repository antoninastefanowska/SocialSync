package com.antonina.socialsynchro.services.facebook.rest.responses;

import com.google.gson.annotations.SerializedName;

public class FacebookInspectTokenResponse extends FacebookResponse {
    @SerializedName("data")
    private Data data;

    public class Data {
        @SerializedName("user_id")
        private String userID;

        @SerializedName("expires_at")
        private long expiresAt;

        @SerializedName("issued_at")
        private long issuedAt;

        public String getUserID() {
            return userID;
        }

        public long getExpiresAt() {
            return expiresAt;
        }

        public long getIssuedAt() {
            return issuedAt;
        }
    }

    public String getUserID() {
        return data.getUserID();
    }

    public long getExpiresAt() {
        return data.getExpiresAt();
    }

    public long getIssuedAt() {
        return data.getIssuedAt();
    }
}
