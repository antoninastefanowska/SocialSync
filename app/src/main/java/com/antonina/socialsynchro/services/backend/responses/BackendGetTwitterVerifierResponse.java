package com.antonina.socialsynchro.services.backend.responses;

import com.google.gson.annotations.SerializedName;

public class BackendGetTwitterVerifierResponse extends BackendResponse {
    @SerializedName("oauth_token")
    private String loginToken;

    @SerializedName("oauth_verifier")
    private String verifier;

    public String getLoginToken() { return loginToken; }

    public String getVerifier() { return verifier; }
}
