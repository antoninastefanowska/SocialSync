package com.antonina.socialsynchro.base;

public interface IAccount {
    String getAccessToken();
    void setAccessToken(String accessToken);
    String getSecretToken();
    void setSecretToken(String secretToken);
}
