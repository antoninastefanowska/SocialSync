package com.antonina.socialsynchro.services.deviantart.rest.responses;

import com.google.gson.annotations.SerializedName;

public class DeviantArtStashSubmitResponse extends DeviantArtResponse {
    @SerializedName("itemid")
    private String stashID;

    @SerializedName("status")
    private String status;

    public String getStashID() {
        return stashID;
    }

    public String getStatus() {
        return status;
    }
}
