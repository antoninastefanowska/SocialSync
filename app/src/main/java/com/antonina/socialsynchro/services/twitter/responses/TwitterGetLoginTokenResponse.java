package com.antonina.socialsynchro.services.twitter.responses;

import com.antonina.socialsynchro.base.IErrorResponse;
import com.antonina.socialsynchro.base.IRawResponse;

public class TwitterGetLoginTokenResponse implements IRawResponse, IErrorResponse {
    private String loginToken;
    private String loginSecretToken;
    private boolean callbackConfirmed;
    private String error;

    public String getLoginToken() { return loginToken; }

    public String getLoginSecretToken() { return loginSecretToken; }

    public boolean getCallbackConfirmed() { return callbackConfirmed; }

    @Override
    public void createFromString(String stringResponse) {
        String[] pairs = stringResponse.split("&");
        for (String pair : pairs) {
            String[] keyAndValue = pair.split("=");
            switch (keyAndValue[0]) {
                case "oauth_token":
                    loginToken = keyAndValue[1];
                    break;
                case "oauth_token_secret":
                    loginSecretToken = keyAndValue[1];
                    break;
                case "oauth_callback_confirmed":
                    callbackConfirmed = Boolean.parseBoolean(keyAndValue[1]);
                    break;
            }
        }
    }

    @Override
    public void createFromErrorString(String errorResponse) {
        error = errorResponse;
    }

    @Override
    public String getErrorString() {
        return error;
    }
}
