package com.antonina.socialsynchro.services.callback.responses;

import com.antonina.socialsynchro.base.IErrorResponse;
import com.google.gson.annotations.SerializedName;

public class CallbackGetVerifierResponse implements IErrorResponse {
    @SerializedName("oauth_token")
    private String loginToken;

    @SerializedName("oauth_verifier")
    private String verifier;

    @SerializedName("message")
    private String message;

    public String getLoginToken() { return loginToken; }

    public String getVerifier() { return verifier; }

    public String getMessage() { return message; }

    @Override
    public String getErrorString() {
        return message;
    }
}
