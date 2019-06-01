package com.antonina.socialsynchro.accounts;

public class TwitterAccount {
    private String name;
    private String url;
    private String access_token;
    private String access_token_secret;

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
}
