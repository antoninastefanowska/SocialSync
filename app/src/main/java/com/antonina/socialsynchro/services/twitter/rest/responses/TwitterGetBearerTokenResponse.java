package com.antonina.socialsynchro.services.twitter.rest.responses;

import com.google.gson.annotations.SerializedName;

public class TwitterGetBearerTokenResponse extends TwitterResponse {
    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("access_token")
    private String bearerToken;

    public String getTokenType() {
        return tokenType;
    }

    public String getBearerToken() {
        return bearerToken;
    }
}
