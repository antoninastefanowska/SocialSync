package com.antonina.socialsynchro.services.backend.responses;

import com.antonina.socialsynchro.common.rest.IResponse;
import com.google.gson.annotations.SerializedName;

public class BackendGetTwitterVerifierResponse implements IResponse {
    private String undefinedError;

    @SerializedName("oauth_token")
    private String loginToken;

    @SerializedName("oauth_verifier")
    private String verifier;

    @SerializedName("message")
    private String message;

    public String getLoginToken() { return loginToken; }

    public String getVerifier() { return verifier; }

    @Override
    public String getErrorString() {
        if (message == null)
            return undefinedError;
        else
            return message;
    }

    @Override
    public void setUndefinedError(String undefinedError) {
        this.undefinedError = undefinedError;
    }
}
