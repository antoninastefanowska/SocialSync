package com.antonina.socialsynchro.services.facebook.rest.responses;

import com.google.gson.annotations.SerializedName;

public class FacebookIdentifierResponse extends FacebookResponse {
    @SerializedName("id")
    private String id;

    public String getID() {
        return id;
    }
}
