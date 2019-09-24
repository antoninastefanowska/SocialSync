package com.antonina.socialsynchro.services.twitter.requests;

import com.antonina.socialsynchro.services.IRequest;
import com.antonina.socialsynchro.services.twitter.requests.authorization.TwitterAuthorizationStrategy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@SuppressWarnings({"WeakerAccess", "StringBufferReplaceableByString"})
public abstract class TwitterRequest implements IRequest {
    private final String authorizationHeader;

    protected TwitterRequest(String authorizationHeader) {
        this.authorizationHeader = authorizationHeader;
    }

    public String getAuthorizationHeader() {
        return authorizationHeader;
    }

    public static String percentEncode(String input) {
        String output = "";

        try {
            output = URLEncoder.encode(input, "UTF-8");
            output = output.replace("+", "%20");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return output;
    }

    protected abstract static class Builder {
        protected TwitterAuthorizationStrategy authorization;

        public abstract TwitterRequest build();

        protected abstract void configureAuthorization();
    }
}
