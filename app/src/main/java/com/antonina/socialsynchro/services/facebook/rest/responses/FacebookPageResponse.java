package com.antonina.socialsynchro.services.facebook.rest.responses;

import com.google.gson.annotations.SerializedName;

public class FacebookPageResponse extends FacebookResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("fan_count")
    private int fanCount;

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getFanCount() {
        return fanCount;
    }
}
