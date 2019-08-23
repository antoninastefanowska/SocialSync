package com.antonina.socialsynchro.services.twitter.responses;

import com.antonina.socialsynchro.services.IRawResponse;

public class TwitterGetAccessTokenResponse extends TwitterResponse implements IRawResponse {
    private String accessToken;
    private String secretToken;
    private String error;

    public String getAccessToken() { return accessToken; }

    public String getSecretToken() { return secretToken; }

    @Override
    public void createFromString(String stringResponse) {
        String[] pairs = stringResponse.split("&");
        for (String pair : pairs) {
            String[] keyAndValue = pair.split("=");
            switch (keyAndValue[0]) {
                case "oauth_token":
                    accessToken = keyAndValue[1];
                    break;
                case "oauth_token_secret":
                    secretToken = keyAndValue[1];
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
