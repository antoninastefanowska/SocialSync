package com.antonina.socialsynchro.services.twitter;

import com.antonina.socialsynchro.base.IAccount;

public class TwitterAccount implements IAccount {
    private String name;
    private String url;
    private String accessToken;
    private String secretToken;

    // TODO: uwzględnić limity żądań

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getAccessToken() { return accessToken; }

    @Override
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    @Override
    public String getSecretToken() { return secretToken; }

    @Override
    public void setSecretToken(String secretToken) { this.secretToken = secretToken; }
}
