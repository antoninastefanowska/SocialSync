package com.antonina.socialsynchro.services.twitter.rest.authorization;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.antonina.socialsynchro.services.twitter.rest.requests.TwitterRequest.percentEncode;

@SuppressWarnings("StringBufferReplaceableByString")
public class TwitterUserAuthorizationStrategy extends TwitterAuthorizationStrategy {
    private final static String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final static String LOWER = UPPER.toLowerCase();
    private final static String NUM = "1234567890";
    private final static String ALPHANUM = UPPER + LOWER + NUM;

    private final Map<String, String> authorizationParameters;
    private final Map<String, String> signatureParameters;

    private String secretToken;
    private String requestURL;
    private String requestMethod;

    public TwitterUserAuthorizationStrategy() {
        super();
        authorizationParameters = new TreeMap<>();
        signatureParameters = new TreeMap<>();
    }

    public TwitterUserAuthorizationStrategy(String accessToken, String secretToken) {
        super();
        authorizationParameters = new TreeMap<>();
        signatureParameters = new TreeMap<>();

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

    public TwitterUserAuthorizationStrategy addAuthorizationParameter(String name, String value) {
        String encodedName = percentEncode(name);
        String encodedValue = percentEncode(value);
        authorizationParameters.put(encodedName, encodedValue);
        signatureParameters.put(encodedName, encodedValue);
        return this;
    }

    public TwitterUserAuthorizationStrategy addSignatureParameter(String name, String value) {
        String encodedName = percentEncode(name);
        String encodedValue = percentEncode(value);
        signatureParameters.put(encodedName, encodedValue);
        return this;
    }

    @Override
    public boolean isUserAuthorization() {
        return true;
    }

    @Override
    public String buildAuthorizationHeader() {
        collectRemainingParameters();

        StringBuilder sb = new StringBuilder("OAuth ");
        String divider = "";

        for (Map.Entry<String, String> parameter : authorizationParameters.entrySet()) {
            sb.append(divider);
            divider = ", ";
            sb.append(parameter.getKey());
            sb.append("=\"");
            sb.append(parameter.getValue());
            sb.append("\"");
        }

        return sb.toString();
    }

    private void collectRemainingParameters() {
        addAuthorizationParameter("oauth_consumer_key", config.getKey("twitter_consumer_key"));
        addAuthorizationParameter("oauth_nonce", generateNonce());
        addAuthorizationParameter("oauth_signature_method", "HMAC-SHA1");
        addAuthorizationParameter("oauth_timestamp", Long.toString(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000));
        addAuthorizationParameter("oauth_version", "1.0");
        authorizationParameters.put(percentEncode("oauth_signature"), percentEncode(buildSignature()));
    }

    private String generateNonce() {
        int length = 42;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
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
        try {
            mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secret = new SecretKeySpec(signingKey.getBytes("UTF-8"), "HmacSHA1");
            mac.init(secret);
            byte[] encodedBytes = mac.doFinal(baseString.getBytes("UTF-8"));
            signature = new String(Base64.encode(encodedBytes, 0), "UTF-8");
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        signature = signature.substring(0, signature.length() - 1);

        return signature;
    }

    private String buildParameterString() {
        StringBuilder sb = new StringBuilder();
        String divider = "";

        for (Map.Entry<String, String> parameter : signatureParameters.entrySet()) {
            sb.append(divider);
            divider = "&";
            sb.append(parameter.getKey());
            sb.append("=");
            sb.append(parameter.getValue());
        }

        return sb.toString();
    }

    private String buildSigningKey() {
        StringBuilder sb = new StringBuilder();

        sb.append(percentEncode(config.getKey("twitter_consumer_secret_key")));
        sb.append("&");
        sb.append(percentEncode(secretToken));

        return sb.toString();
    }
}
