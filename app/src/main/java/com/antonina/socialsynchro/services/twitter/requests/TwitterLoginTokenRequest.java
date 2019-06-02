package com.antonina.socialsynchro.services.twitter.requests;

public class TwitterLoginTokenRequest extends TwitterRequest {
    private final static String CALLBACK_URL = "https://socialsynchro.pythonanywhere.com/callback/post_token";

    public TwitterLoginTokenRequest() {
        super();
        secretToken = "";
        buildUserAuthorizationHeader();
    }

    @Override
    public void collectParameters() {
        authorizationParameters.put("oauth_callback", CALLBACK_URL);
        collectBaseParameters();
    }

    @Override
    protected String getUrl() {
        return "https://api.twitter.com/oauth/request_token";
    }

    public String getCallbackUrl() { return percentEncode(CALLBACK_URL); }
}
