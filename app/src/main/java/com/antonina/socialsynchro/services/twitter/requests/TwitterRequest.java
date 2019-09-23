package com.antonina.socialsynchro.services.twitter.requests;

import android.util.Base64;

import com.antonina.socialsynchro.services.IRequest;
import com.antonina.socialsynchro.services.APIKey;
import com.antonina.socialsynchro.services.twitter.requests.authorization.TwitterAuthorizationStrategy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

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

        protected abstract void prepareAuthorization();
    }
}
