package com.antonina.socialsynchro.services.backend.responses;

import com.google.gson.annotations.SerializedName;

public class BackendGetFacebookTokenResponse extends BackendResponse {
    @SerializedName("state")
    private String state;

    @SerializedName("token")
    private String token;

    public String getState() {
        return state;
    }

    public String getToken() {
        return token;
    }
}
