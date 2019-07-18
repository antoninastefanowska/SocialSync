package com.antonina.socialsynchro.services.twitter.requests;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.base.IRequest;
import com.antonina.socialsynchro.utils.APIKey;

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

public abstract class TwitterRequest implements IRequest {
    private final static String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final static String lower = upper.toLowerCase();
    private final static String num = "1234567890";
    private final static String alphanum = upper + lower + num;

    protected String accessToken;
    protected String secretToken;

    private String authorization;
    protected Map<String, String> authorizationParameters;

    protected TwitterRequest() {
        Comparator<String> encodedComparator = new Comparator<String>() {
            @Override public int compare(String s1, String s2) {
                return percentEncode(s1).compareTo(percentEncode(s2));
            }
        };
        authorizationParameters = new TreeMap<String, String>(encodedComparator);
        accessToken = APIKey.getKey(SocialSynchro.getAppContext(), "twitter_token");
        secretToken = APIKey.getKey(SocialSynchro.getAppContext(), "twitter_secrettoken");
    }

    public String getAuthorization() {
        return authorization;
    }

    public abstract void collectParameters();

    protected abstract String getUrl();

    protected void collectBaseParameters() {
        authorizationParameters.put("oauth_consumer_key", APIKey.getKey(SocialSynchro.getAppContext(), "twitter_key"));
        authorizationParameters.put("oauth_nonce", generateNonce());
        authorizationParameters.put("oauth_signature_method", "HMAC-SHA1");
        authorizationParameters.put("oauth_timestamp", Long.toString(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000));
        authorizationParameters.put("oauth_version", "1.0");
        authorizationParameters.put("oauth_signature", buildSignature());
    }

    protected void buildApplicationAuthorizationHeader() {

    }

    protected void buildUserAuthorizationHeader() {
        collectParameters();

        StringBuilder sb = new StringBuilder("OAuth ");
        String divider = "";

        for (Map.Entry<String, String> parameter : authorizationParameters.entrySet()) {
            sb.append(divider);
            divider = ", ";
            sb.append(percentEncode(parameter.getKey()));
            sb.append("=\"");
            sb.append(percentEncode(parameter.getValue()));
            sb.append("\"");
        }
        authorization = sb.toString();
        Log.d("wysylanie", authorization);
    }

    private String generateNonce() {
        int length = 42;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++)
        {
            int index = random.nextInt(alphanum.length());
            sb.append(alphanum.charAt(index));
        }
        return sb.toString();
    }

    private String buildSignature() {
        StringBuilder sb = new StringBuilder("POST");
        String baseString, signingKey, signature;

        sb.append("&");
        sb.append(percentEncode(getUrl()));
        sb.append("&");
        sb.append(percentEncode(buildParameterString()));

        baseString = sb.toString();
        signingKey = buildSigningKey();

        Mac mac;
        SecretKeySpec secret = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");

        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(secret);
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e) {
            Log.d("error", "Couldn't initialize algorithm.");
            return null;
        }

        byte[] encodedBytes = mac.doFinal(baseString.getBytes());
        signature = new String(Base64.encode(encodedBytes, 0));
        signature = signature.substring(0, signature.length() - 1);

        return signature;
    }

    private String buildParameterString() {
        StringBuilder sb = new StringBuilder();
        String divider = "";

        for (Map.Entry<String, String> parameter : authorizationParameters.entrySet()) {
            sb.append(divider);
            divider = "&";
            sb.append(percentEncode(parameter.getKey()));
            sb.append("=");
            sb.append(percentEncode(parameter.getValue()));
        }

        return sb.toString();
    }

    private String buildSigningKey() {
        StringBuilder sb = new StringBuilder();

        sb.append(percentEncode(APIKey.getKey(SocialSynchro.getAppContext(), "twitter_secretkey")));
        sb.append("&");
        sb.append(percentEncode(secretToken));

        Log.d("wysylanie", sb.toString());
        return sb.toString();
    }

    protected String percentEncode(String input) {
        String output = "";

        try {
            output = URLEncoder.encode(input, "UTF-8");
            output = output.replace("+", "%20");
        }
        catch (UnsupportedEncodingException e) {
            Log.d("wysylanie", "Encoding not supported");
        }

        return output;
    }
}
