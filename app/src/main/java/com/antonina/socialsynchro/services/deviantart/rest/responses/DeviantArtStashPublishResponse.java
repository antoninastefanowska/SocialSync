package com.antonina.socialsynchro.services.deviantart.rest.responses;

import com.google.gson.annotations.SerializedName;

public class DeviantArtStashPublishResponse extends DeviantArtResponse {
    @SerializedName("deviationid")
    private String deviationID;

    @SerializedName("url")
    private String url;

    @SerializedName("status")
    private String status;

    public String getDeviationID() {
        return deviationID;
    }

    public String getURL() {
        return url;
    }

    public String getStatus() {
        return status;
    }
}
