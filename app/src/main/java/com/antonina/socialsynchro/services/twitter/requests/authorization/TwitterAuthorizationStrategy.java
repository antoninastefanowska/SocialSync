package com.antonina.socialsynchro.services.twitter.requests.authorization;

public abstract class TwitterAuthorizationStrategy {
    public abstract String buildAuthorizationHeader();

    public abstract TwitterAuthorizationStrategy accessToken(String accessToken);

    public abstract TwitterAuthorizationStrategy secretToken(String secretToken);

    public abstract TwitterAuthorizationStrategy requestURL(String requestURL);

    public abstract TwitterAuthorizationStrategy requestMethod(String requestMethod);

    public abstract void addAuthorizationParameter(String name, String value);

    public abstract void addSignatureParameter(String name, String value);
}
