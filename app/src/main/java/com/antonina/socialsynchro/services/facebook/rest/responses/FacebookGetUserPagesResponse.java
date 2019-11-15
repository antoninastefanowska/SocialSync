package com.antonina.socialsynchro.services.facebook.rest.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FacebookGetUserPagesResponse extends FacebookResponse {
    @SerializedName("data")
    private List<FacebookPageResponse> pages;

    public List<FacebookPageResponse> getPages() {
        return pages;
    }
}
