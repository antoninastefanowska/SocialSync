package com.antonina.socialsynchro.services.twitter.requests;

public class TwitterAccessTokenRequest extends TwitterRequest {
    private String verifier;

    public TwitterAccessTokenRequest(String loginToken, String secretLoginToken, String verifier) {
        super();
        accessToken = loginToken;
        secretToken = secretLoginToken;
        this.verifier = verifier;
        buildUserAuthorizationHeader();
    }

    @Override
    public void collectParameters() {
        authorizationParameters.put("oauth_token", accessToken);
        authorizationParameters.put("oauth_verifier", verifier);
        collectBaseParameters();
        authorizationParameters.remove("oauth_verifier");
    }

    @Override
    protected String getUrl() {
        return "https://api.twitter.com/oauth/access_token";
    }

    public String getVerifier() {
        return percentEncode(verifier);
    }
}
