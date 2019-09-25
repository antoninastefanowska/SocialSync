package com.antonina.socialsynchro.services.twitter.rest.authorization;

import android.util.Base64;

import static com.antonina.socialsynchro.services.twitter.rest.requests.TwitterRequest.percentEncode;

public class TwitterGetBearerTokenAuthorizationStrategy extends TwitterAuthorizationStrategy {

    public TwitterGetBearerTokenAuthorizationStrategy() {
        super();
    }

    @Override
    public String buildAuthorizationHeader() {
        String consumerKey = percentEncode(config.getKey("twitter_consumer_key"));
        String consumerSecretKey = percentEncode(config.getKey("twitter_consumer_secret_key"));
        String credentials = consumerKey + ":" + consumerSecretKey;
        return "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }
}
