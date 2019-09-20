package com.antonina.socialsynchro.services.twitter.requests;

import android.util.Base64;

import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.services.IRequest;
import com.antonina.socialsynchro.services.APIKey;

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
    private String authorizationHeader;

    protected TwitterRequest(String authorizationHeader) {
        this.authorizationHeader = authorizationHeader;
    }

    public String getAuthorizationHeader() {
        return authorizationHeader;
    }

    protected static String percentEncode(String input) {
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
        private final static String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private final static String LOWER = UPPER.toLowerCase();
        private final static String NUM = "1234567890";
        private final static String ALPHANUM = UPPER + LOWER + NUM;

        private final Comparator<String> encodedComparator = new Comparator<String>() {
            @Override public int compare(String s1, String s2) {
                return percentEncode(s1).compareTo(percentEncode(s2));
            }
        };
        protected Map<String, String> authorizationParameters = new TreeMap<String, String>(encodedComparator);

        public abstract TwitterRequest build();

        protected void collectParameters() {
            authorizationParameters.put("oauth_consumer_key", APIKey.getKey("twitter_key"));
            authorizationParameters.put("oauth_nonce", generateNonce());
            authorizationParameters.put("oauth_signature_method", "HMAC-SHA1");
            authorizationParameters.put("oauth_timestamp", Long.toString(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000));
            authorizationParameters.put("oauth_version", "1.0");
            authorizationParameters.put("oauth_signature", buildSignature());
        }

        protected abstract String getURL();

        protected abstract String getAccessToken();

        protected abstract String getSecretToken();

        protected abstract String getHTTPMethod();

        protected String buildUserAuthorizationHeader() {
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

        protected String buildSignature() {
            StringBuilder sb = new StringBuilder(getHTTPMethod());
            String baseString, signingKey, signature;

            sb.append("&");
            sb.append(percentEncode(getURL()));
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

            sb.append(percentEncode(APIKey.getKey("twitter_secretkey")));
            sb.append("&");
            sb.append(percentEncode(getSecretToken()));

            return sb.toString();
        }
    }
}
