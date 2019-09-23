package com.antonina.socialsynchro.services.twitter.requests.authorization;

import android.util.Base64;

import com.antonina.socialsynchro.services.APIKey;

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

import static com.antonina.socialsynchro.services.twitter.requests.TwitterRequest.percentEncode;

public class TwitterUserAuthorizationStrategy extends TwitterAuthorizationStrategy {
    private final static String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final static String LOWER = UPPER.toLowerCase();
    private final static String NUM = "1234567890";
    private final static String ALPHANUM = UPPER + LOWER + NUM;

    private static final Comparator<String> encodedComparator = new Comparator<String>() {
        @Override public int compare(String s1, String s2) {
            return percentEncode(s1).compareTo(percentEncode(s2));
        }
    };
    private final Map<String, String> authorizationParameters;
    private final Map<String, String> signatureParameters;

    private String secretToken;
    private String requestURL;
    private String requestMethod;

    public TwitterUserAuthorizationStrategy() {
        authorizationParameters = new TreeMap<>(encodedComparator);
        signatureParameters = new TreeMap<>(encodedComparator);
    }

    public TwitterUserAuthorizationStrategy(String accessToken, String secretToken) {
        authorizationParameters = new TreeMap<>(encodedComparator);
        signatureParameters = new TreeMap<>(encodedComparator);

        addAuthorizationParameter("oauth_token", accessToken);
        this.secretToken = secretToken;
    }

    public TwitterUserAuthorizationStrategy accessToken(String accessToken) {
        addAuthorizationParameter("oauth_token", accessToken);
        return this;
    }

    public TwitterUserAuthorizationStrategy secretToken(String secretToken) {
        this.secretToken = secretToken;
        return this;
    }

    public TwitterUserAuthorizationStrategy requestURL(String requestURL) {
        this.requestURL = requestURL;
        return this;
    }

    public TwitterUserAuthorizationStrategy requestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    public void addAuthorizationParameter(String name, String value) {
        authorizationParameters.put(name, value);
        signatureParameters.put(name, value);
    }

    public void addSignatureParameter(String name, String value) {
        signatureParameters.put(name, value);
    }

    private void collectRemainingAuthorizationParameters() {
        addAuthorizationParameter("oauth_consumer_key", APIKey.getKey("twitter_key"));
        addAuthorizationParameter("oauth_nonce", generateNonce());
        addAuthorizationParameter("oauth_signature_method", "HMAC-SHA1");
        addAuthorizationParameter("oauth_timestamp", Long.toString(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000));
        addAuthorizationParameter("oauth_version", "1.0");
        addAuthorizationParameter("oauth_signature", buildSignature());
    }

    public String buildAuthorizationHeader() {
        collectRemainingAuthorizationParameters();

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

        return sb.toString();
    }

    private String generateNonce() {
        int length = 42;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++)
        {
            int index = random.nextInt(ALPHANUM.length());
            sb.append(ALPHANUM.charAt(index));
        }
        return sb.toString();
    }

    private String buildSignature() {
        StringBuilder sb = new StringBuilder(requestMethod);
        String baseString, signingKey, signature;

        sb.append("&");
        sb.append(percentEncode(requestURL));
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
            e.printStackTrace();
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

        for (Map.Entry<String, String> parameter : signatureParameters.entrySet()) {
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

        sb.append(percentEncode(APIKey.getKey("twitter_secretkey")));
        sb.append("&");
        sb.append(percentEncode(secretToken));

        return sb.toString();
    }
}
