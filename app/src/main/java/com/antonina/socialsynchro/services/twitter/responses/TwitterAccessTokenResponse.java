package com.antonina.socialsynchro.services.twitter.responses;

import com.antonina.socialsynchro.base.IResponse;

public class TwitterAccessTokenResponse implements IResponse {
    public TwitterAccessTokenResponse(String responseString) {
        String[] pairs = responseString.split("&");
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

    private String accessToken;

    private String secretToken;

    public String getAccessToken() { return accessToken; }

    public String getSecretToken() { return secretToken; }
}
