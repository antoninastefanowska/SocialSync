package com.antonina.socialsynchro.services.deviantart.rest.responses;

import com.google.gson.annotations.SerializedName;

public class DeviantArtAccessTokenResponse extends DeviantArtResponse {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("status")
    private String status;

    @SerializedName("expires_in")
    private long expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getStatus() {
        return status;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}
