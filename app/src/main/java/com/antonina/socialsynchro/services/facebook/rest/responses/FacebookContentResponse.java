package com.antonina.socialsynchro.services.facebook.rest.responses;

import com.google.gson.annotations.SerializedName;

public class FacebookContentResponse extends FacebookResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("message")
    private String message;

    public String getID() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
