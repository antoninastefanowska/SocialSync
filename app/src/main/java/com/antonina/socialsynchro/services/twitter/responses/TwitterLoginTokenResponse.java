package com.antonina.socialsynchro.services.twitter.responses;

import com.antonina.socialsynchro.base.IResponse;

public class TwitterLoginTokenResponse implements IResponse {
    public TwitterLoginTokenResponse(String responseString) {
        String[] pairs = responseString.split("&");
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
            }
        }
    }

    private String loginToken;

    private String loginSecretToken;

    private boolean callbackConfirmed;

    public String getLoginToken() { return loginToken; }

    public String getLoginSecretToken() { return loginSecretToken; }

    public boolean getCallbackConfirmed() { return callbackConfirmed; }
}
