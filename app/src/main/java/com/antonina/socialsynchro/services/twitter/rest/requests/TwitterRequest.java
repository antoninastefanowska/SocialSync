package com.antonina.socialsynchro.services.twitter.rest.requests;

import com.antonina.socialsynchro.common.rest.BaseRequest;
import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterAuthorizationStrategy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@SuppressWarnings({"WeakerAccess", "StringBufferReplaceableByString"})
public abstract class TwitterRequest extends BaseRequest {

    protected TwitterRequest(String authorizationHeader) {
        super(authorizationHeader);
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

    public static abstract class Builder extends BaseRequest.Builder {
        protected TwitterAuthorizationStrategy authorization;

        @Override
        public abstract TwitterRequest build();
    }
}
