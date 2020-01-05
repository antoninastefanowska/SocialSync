package com.antonina.socialsynchro.services.deviantart.rest.responses;

import com.google.gson.annotations.SerializedName;

public class DeviantArtResultResponse extends DeviantArtResponse {
    @SerializedName("success")
    private boolean success;

    public boolean getSuccess() {
        return success;
    }
}
