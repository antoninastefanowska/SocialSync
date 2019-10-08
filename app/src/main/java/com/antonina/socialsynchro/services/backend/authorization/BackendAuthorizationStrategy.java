package com.antonina.socialsynchro.services.backend.authorization;

import android.util.Base64;

import com.antonina.socialsynchro.common.rest.BaseAuthorizationStrategy;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class BackendAuthorizationStrategy extends BaseAuthorizationStrategy {
    @Override
    public String buildAuthorizationHeader() {
        String consumerKey = config.getKey("backend_consumer_key");
        String consumerSecretKey = config.getKey("backend_consumer_secret_key");
        String timestamp = Long.toString(Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000);
        String hashInput = timestamp + consumerKey;
        String signature;

        try {
            byte[] rawHashInput = hashInput.getBytes("UTF-8");
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec key = new SecretKeySpec(consumerSecretKey.getBytes("UTF-8"), "HmacSHA1");
            mac.init(key);
            byte[] rawSignature = mac.doFinal(hashInput.getBytes("UTF-8"));
            signature = new String(Base64.encode(rawSignature, Base64.NO_WRAP), "UTF-8");
            signature = signature.substring(0, signature.length() - 1);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder sb = new StringBuilder("consumer_key=");
        sb.append(consumerKey);
        sb.append("&timestamp=");
        sb.append(timestamp);
        sb.append("&signature=");
        sb.append(signature);

        return sb.toString();
    }
}
